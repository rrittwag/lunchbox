package domain.logic

import java.net.URL

import domain.models._
import infrastructure.{FacebookClient, OcrClient}
import org.joda.money.{CurrencyUnit, Money}
import java.time.{DayOfWeek, LocalDate}
import java.time.format.DateTimeFormatter

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex
import play.api.libs.json._

case class Wochenplan(monday: LocalDate, mittagsplanImageId: String)

/**
 * Mittagsangebote von Gesundheitszentrum Springpfuhl über deren Facebook-Seite ermitteln.
 */
class LunchResolverGesundheitszentrum(
    dateValidator: DateValidator,
    facebookClient: FacebookClient,
    ocrClient: OcrClient) extends LunchResolver {

  sealed abstract class PdfSection(val sectionStartPattern: String, val order: Int)

  object PdfSection {
    case object HEADER extends PdfSection("<<Header>>", 0)
    case object MONTAG extends PdfSection("Montag", 0)
    case object DIENSTAG extends PdfSection("Dienstag", 1)
    case object MITTWOCH extends PdfSection("Mittwoch", 2)
    case object DONNERSTAG extends PdfSection("Donnerstag", 3)
    case object FREITAG extends PdfSection("Freitag", 4)
    case object FOOTER extends PdfSection("Für unseren L", 0)

    val weekdaysValues = List[PdfSection](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = List(HEADER) ++ weekdaysValues ++ List(FOOTER)
  }

  case class OfferRow(name: String, priceOpt: Option[Money]) {
    def merge(otherRow: OfferRow): OfferRow = {
      val newName = Seq(name, otherRow.name).filter(!_.isEmpty).mkString(" ")
      OfferRow(newName, priceOpt.orElse(otherRow.priceOpt))
    }

    def isValid = !name.isEmpty && priceOpt.isDefined
  }

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  override def resolve: Future[Seq[LunchOffer]] =
    // von der Facebook-Seite der Kantine die Posts als JSON abfragen (beschränt auf Text und Anhänge)
    facebookClient.query("181190361991823/posts?fields=message,attachments")
      .map(facebookPosts => parseWochenplaene(facebookPosts).takeWhile(isWochenplanRelevant))
      .flatMap(wochenplaene => resolveOffersFromWochenplaene(wochenplaene))

  private[logic] def parseWochenplaene(facebookPostsAsJson: String): Seq[Wochenplan] =
    for (
      post <- (Json.parse(facebookPostsAsJson) \ "data").as[Seq[JsValue]];
      postText <- (post \ "message").asOpt[String];
      day <- parseDay(postText.replaceAll("\\n", "|")); // im Text steckt das Datum der Woche
      monday = day.`with`(DayOfWeek.MONDAY);
      imageAttachment = findImageAttachment(post);
      imageId <- (imageAttachment \ "target" \ "id").asOpt[String]
    ) yield Wochenplan(monday, imageId)

  private def findImageAttachment(post: JsValue): JsValue = {
    val attachments = (post \ "attachments" \ "data")(0)
    (attachments \ "subattachments").toOption match {
      case None => attachments // nur 1 Anhang: das Bild mit dem Mittagsplan
      case Some(subattachments) => (subattachments \ "data")(0) // mehrere Anhänge: das 1. Bild hat den Mittagsplan
    }
  }

  private[logic] def resolveOffersFromWochenplaene(wochenplaene: Seq[Wochenplan]): Future[Seq[LunchOffer]] = {
    val listOfFutures = wochenplaene.map(wochenplan => resolveOffersFromWochenplan(wochenplan))
    Future.sequence(listOfFutures).map(listOfLists => listOfLists.flatten.distinct)
  }

  private[logic] def resolveOffersFromWochenplan(plan: Wochenplan): Future[Seq[LunchOffer]] =
    facebookClient.query(s"${plan.mittagsplanImageId}?fields=images")
      .flatMap(facebookImageJson => doOcr(parseUrlOfBiggestImage(facebookImageJson)))
      .map(ocrText => resolveOffersFromText(plan.monday, ocrText))

  private[logic] def doOcr(urlOpt: Option[URL]): Future[String] = urlOpt match {
    case Some(imageUrl) => ocrClient.doOCR(imageUrl)
    case None => Future.successful("")
  }

  private[logic] def parseUrlOfBiggestImage(facebookImageJson: String): Option[URL] = {
    val sizedImagesAsJson = (Json.parse(facebookImageJson) \ "images").as[Seq[JsValue]]
    val biggestImageAsJson = sizedImagesAsJson.maxBy(imageAsJson => (imageAsJson \ "height").as[Int])
    val imageUrlAsStringOpt = (biggestImageAsJson \ "source").asOpt[String]
    imageUrlAsStringOpt.map(new URL(_))
  }

  private[logic] def resolveOffersFromText(monday: LocalDate, text: String): Seq[LunchOffer] =
    groupBySection(text)
      .filterKeys(PdfSection.weekdaysValues.contains)
      .toSeq.flatMap { case (section, lines) => resolveOffersFromSection(section, lines, monday) }

  private def groupBySection(text: String): Map[PdfSection, Seq[String]] = {
    var result = Map[PdfSection, Seq[String]]()

    var currentSection: PdfSection = PdfSection.HEADER
    var linesForSection = Seq[String]()

    for (
      unformattedLine <- text.split('\n');
      line = unformattedLine.trim
        .replaceAll("Nlittwoch", "Mittwoch")
        .replaceAll("Freng", "Freitag")
    ) {
      PdfSection.values.find(sec => line.startsWith(sec.sectionStartPattern)) match {
        case Some(newSection) =>
          result += currentSection -> linesForSection
          currentSection = newSection
          linesForSection = Seq(line.replaceFirst(newSection.sectionStartPattern, "").trim)
        case None =>
          linesForSection :+= line
      }
    }

    result
  }

  private def resolveOffersFromSection(section: PdfSection, lines: Seq[String], monday: LocalDate): Seq[LunchOffer] = {
    val rows = resolveOfferRows(lines)
    rows.flatMap {
      case OfferRow(name, Some(price)) =>
        Some(LunchOffer(0, name, monday.plusDays(section.order), price, LunchProvider.GESUNDHEITSZENTRUM.id))
      case _ =>
        None
    }
  }

  private def resolveOfferRows(lines: Seq[String]): Seq[OfferRow] = {
    var result = Seq[OfferRow]()
    var currentRow: Option[OfferRow] = None

    def mergeRow(newRow: OfferRow) = currentRow match {
      case Some(row) => currentRow = Some(row.merge(newRow))
      case None => currentRow = Some(newRow)
    }

    for (
      line <- lines.takeWhile(l => !isEndingSection(l))
        .map(l => removeUnnecessaryText(correctOcrErrors(l)))
    ) {
      if (line.matches("""^[F\d]\.?( .*)?$""")) {
        currentRow.foreach(row => result :+= row)
        currentRow = None
      }

      line.replaceFirst("""^[F\d]\.?""", "") match {
        case r"""(.*)$name (\d{1,},\d{2})$priceStr""" =>
          mergeRow(OfferRow(cleanName(name), parsePrice(priceStr)))
        case name =>
          mergeRow(OfferRow(cleanName(name), None))
      }
    }
    currentRow.foreach(row => result :+= row)
    result
  }

  private def correctOcrErrors(line: String) =
    line.trim
      .replaceAll("‚", ",")
      .replaceAll("""^[fF][\.,]* """, "F. ")
      .replaceAll("""^(\d)[\.,]+ """, "$1. ")
      .replaceAll("""^l[\.,]+ """, "1. ")
      .replaceAll("""^(\d)A """, "$1. ")
      .replaceAll("^Zl ", "2. ")
      .replaceAll("""L,(\d{2}) ?€?$""", "4,$1")
      .replaceAll("""(\d{1,}) ?[\.,] ?(\d{2}) ?[€g]?$""", "$1,$2")
      .replaceAll("""(\d)(\d{2}) ?[€g]?$""", "$1,$2")
      .replaceAll(""" 1,(\d{2})$""", " 4,$1") // für 1,**€ gibt's nix mehr, OCR-Fehler für 4,**€
      .replaceAll("ﬂ", "fl")
      .replaceAll("ﬁ", "fi")
      .replaceAll("IVi", "M")
      .replaceAll("IVl", "M")
      .replaceAll("""([a-zA-ZäöüßÄÖÜ])II""", "$1ll")
      .replaceAll("""([a-zA-ZäöüßÄÖÜ])I""", "$1l")
      .replaceAll("artoffei", "artoffel")
      .replaceAll("uiasch", "ulasch")
      .replaceAll("oiikorn", "ollkorn")
      .replaceAll("Kiöße", "Klöße")
      .replaceAll("kcai", "kcal")
      .replaceAll("Müiier", "Müller")
      .replaceAll("oreiie", "orelle")
      .replaceAll("Saiz", "Salz")
      .replaceAll("([kK]oh)i", "$1l")
      .replaceAll("([mM])iich", "$1ilch")
      .replaceAll("udein", "udeln")
      .replaceAll("zeriassen", "zerlassen")
      .replaceAll("Bitteki", "Bifteki")
      .replaceAll("([bB])iatt", "$1latt")
      .replaceAll("([zZ]wiebe)i", "$1l")
      .replaceAll("chnitzei", "chnitzel")
      .replaceAll("SCHNlTZ", "SCHNITZ")
      .replaceAll("SCHNITZELTAG", "Schnitzeltag") // Wieso rumschreien?
      .replaceAll("uflauf", "uflauf")
      .replaceAll("utiauf", "uflauf")
      .replaceAll("ufiauf", "uflauf")
      .replaceAll("uflaufmit", "uflauf mit")
      .replaceAll("Fiahm", "Rahm")
      .replaceAll("Fiei", "Rei")
      .replaceAll("Fiührei", "Rührei")
      .replaceAll("Blatispinat", "Blattspinat")
      .replaceAll("‘/2", "1/2")
      .replaceAll("au/3er", "außer")
      .replaceAll("""auf([A-Z])""", "auf $1")
      .replaceAll("Fiind", "Rind")
      .replaceAll("Fiotkohl", "Rotkohl")
      .replaceAll("CeVapcici", "Cevapcici")
      .replaceAll("kc[nu]l", "kcal")
      .replaceAll("""([^s])e-Hackfleisch""", "$1Käse-Hackfleisch") // fuckin bad OCR
      .replaceAll("""([^J])agerschnitzel"""", "$1\"Jägerschnitzel\"")
      .replaceAll("Matjesmpt ", "Matjestopf ")
      .replaceAll("Reibeküse", "Reibekäse")
      .replaceAll("falisch", "fälisch")
      .replaceAll("lll ", "!!! ")
      .replaceAll(" lll", " !!!")
      .replaceAll("ScharfeseKartoffelePaprikszurry", "Scharfes Kartoffel-Paprika-Curry")
      .replaceAll("Möhren7Zucchiniinntopf", "Möhren-Zucchini-Eintopf")
      .replaceAll("Häl111cl1e11sclu1itzel", "Hähnchenschnitzel")
      .replaceAll("Pfeffelralnnsauce", "Pfefferrahmsauce")

  private def removeUnnecessaryText(text: String) =
    text.trim
      .replaceAll("""^FlTNESS [fF\d]\.* """, "F. ")
      .replaceAll("""^FlTNESS """, "")
      .replaceAll(""" ,?[unm]; """, " ")
      .replaceAll(""" \d+ kcal""", "")
      .replaceAll(""" [a-zA-Z]+kcal""", " ")
      .replaceAll(" kcal", "")
      .replaceAll(" 2 ?und ", " und ")
      .replaceAll(" 2 ?mit ", " mit ")
      .replaceAll(" , ", ", ")
      .replaceAll(" 3.14 ", " ")
      .replaceAll(" 1,14m ", " ")
      .replaceAll(""" \d+[a-zA-Z]+ """, " ")
      .replaceAll(""" [a-zA-Z]+\d+ """, " ")
      .replaceAll(""" [a-zA-Z] """, " ")
      .replaceAll(""" [A-Z][^ ]+[A-Z] """, " ")
      .replaceAll(""" \|4 """, " ")
      .replaceAll(""" [Agl\(‘!]{1,5} +(\d\,\d\d)$""", " $1")
      .replaceAll(""" [A-Z]{2}[^ ]* +(\d\,\d\d)$""", " $1")
      .replaceAll("!!!", "")
      .replaceAll("Amame", "")
      .replaceAll("Acu", "")
      .replaceAll(""" [^ ]+![^ ]+ """, "")
      .replaceAll(""" [^ ]+![^ ]+$""", "")
      .replaceAll(""" [A-Za-z]+[A-Z0-9!].* +(\d\,\d\d)$""", " $1")
      .replaceAll(""" [0-9 |l]+$""", "").trim // schlecht OCR-ed Zusatzstoffe

  private def isEndingSection(line: String): Boolean =
    line.startsWith("ACHTUNG") || line.startsWith("Wir wünschen") ||
      line.startsWith("Öffnungszeiten") || line.startsWith("Alle Spelsen")

  private def cleanName(text: String) = text.replaceAll(" 2 *$", "").trim

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   * @param text auszuwertender Text
   * @return
   */
  private def parseDay(text: String): Option[LocalDate] = text match {
    case r""".*(\d{2}\.\d{2}\.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case _ => None
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   * @param text auszuwertender Text
   * @return
   */
  private def parsePrice(text: String): Option[Money] = text match {
    case r""".*(\d{1,})$major,(\d{2})$minor""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

  private def isWochenplanRelevant(wochenplan: Wochenplan): Boolean = dateValidator.isValid(wochenplan.monday)

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString)))
    } catch {
      case exc: Throwable => None
    }

}
