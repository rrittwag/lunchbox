package info.rori.lunchbox.server.akka.scala.domain.logic

import java.io.FileNotFoundException
import java.net.URL

import info.rori.lunchbox.server.akka.scala.domain.model._
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.util.PDFTextStripper
import org.htmlcleaner.{CleanerProperties, HtmlCleaner}
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

import scala.util.matching.Regex

class LunchResolverHotelAmRing extends LunchResolver {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  sealed abstract class PdfSection(val sectionStartPattern: String, val order: Int)

  object PdfSection {
    case object MONTAG extends PdfSection("Montag", 0)
    case object DIENSTAG extends PdfSection("Dienstag", 1)
    case object MITTWOCH extends PdfSection("Mittwoch", 2)
    case object DONNERSTAG extends PdfSection("Donnerstag", 3)
    case object FREITAG extends PdfSection("Freitag", 4)
    case object SALAT_DER_WOCHE extends PdfSection("Salat der Woche", 0)
    case object FOOTER extends PdfSection("Alle Gerichte beinhalt", 0)

    val weekdaysValues = List[PdfSection](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = weekdaysValues ++ List(SALAT_DER_WOCHE, FOOTER)
  }

  case class OfferRow(name: String, priceOpt: Option[Money]) {
    def merge(otherRow: OfferRow): OfferRow = {
      val newName = List(name, otherRow.name).filter(!_.isEmpty).mkString(" ")
      OfferRow(newName, priceOpt.orElse(otherRow.priceOpt))
    }

    def isValid = !name.isEmpty && priceOpt.isDefined
  }


  override def resolve: Seq[LunchOffer] = {
    val pdfLinks = resolvePdfLinks(new URL("http://www.hotel-am-ring.de/restaurant-rethra.html"))

    pdfLinks.flatMap(relativePdfPath =>
      resolveFromPdf(new URL("http://www.hotel-am-ring.de/" + relativePdfPath))
    )
  }

  private[logic] def resolvePdfLinks(htmlUrl: URL): List[String] = {
    val props = new CleanerProperties
    props.setCharset("utf-8")

    val rootNode = new HtmlCleaner(props).clean(htmlUrl)
    val links = rootNode.evaluateXPath("//a/@href").map { case n: String => n}.toSet

    links.filter(_ matches """.*/Mittagspause_.+(\d{2}.\d{2}.\d{2,4})\D*.pdf""").toList
  }

  private[logic] def resolveFromPdf(pdfUrl: URL): List[LunchOffer] = {
    val optMonday = parseMondayFromUrl(pdfUrl)

    val pdfContent = extractPdfContent(pdfUrl)

    val section2StartIndex = PdfSection.values.map { section =>
      pdfContent.indexOf(section.sectionStartPattern) match {
        case index if index > -1 => Some((section, index))
        case _ => None
      }
    }.flatten

    val section2content = section2StartIndex.sliding(2).toList.map {
      case List((sec1, idx1), (sec2, idx2)) => (sec1, pdfContent.substring(idx1, idx2))
    }

    val section2offers = section2content.map {
      case (section, secContent) => (section, parseOffersFromSectionString(secContent, section, optMonday))
    }

    // Wochenangebote über die Woche verteilen
    val (wochenOffers, tagesOffers) = section2offers.partition(_._1 == PdfSection.SALAT_DER_WOCHE)
    val tagesOffersList = tagesOffers.flatMap(_._2)

    val multipliedWochenOffers = multiplyWochenangebote(wochenOffers.flatMap(_._2), tagesOffersList.map(_.day))
    tagesOffersList ++ multipliedWochenOffers
  }

  private[logic] def parseMondayFromUrl(pdfUrl: URL): Option[LocalDate] = pdfUrl.getFile match {
    case r""".*(\d{2}.\d{2}.)\d{2,4}$fridayString\D*.pdf""" =>
      parseDay(fridayString).map { friday =>
        val weekOfYear = friday.getWeekOfWeekyear
        LocalDate.now.withWeekOfWeekyear(weekOfYear).withDayOfWeek(1)
      }
    case _ => None
  }

  private def parseOffersFromSectionString(sectionContent: String, section: PdfSection, optMonday: Option[LocalDate]): List[LunchOffer] = {
    val _ :: offersContent = sectionContent.split("\n").map(_.trim).toList

    var rows = offersContent.map {
      case r"""(.+)$text(\d{1,}[.,]\d{2})$priceString {0,1} *€""" => OfferRow(parseName(text), parsePrice(priceString))
      case text => OfferRow(parseName(text), None)
    }

    rows = rows match {
      // Mittwochs-Titelzeile "Buffettag" mit nächster Zeile mergen
      case first :: second :: remain if section == PdfSection.MITTWOCH =>
        OfferRow(s"${first.name}: ${second.name}", first.priceOpt.orElse(second.priceOpt)) :: remain
      // "Salat der Woche: " voranstellen, wenn dem so ist
      case first :: remain if section == PdfSection.SALAT_DER_WOCHE =>
        OfferRow(s"Salat der Woche: ${first.name}", first.priceOpt) :: remain
      case r => r
    }

    // Rows mergen
    var mergedRows = List[OfferRow]()
    var curMergedRowOpt: Option[OfferRow] = None
    rows.foreach(row =>
      curMergedRowOpt match {
        case None => curMergedRowOpt = Some(row)
        case Some(mixedRow) =>
          mixedRow.priceOpt match {
            case None =>
              curMergedRowOpt = Some(mixedRow.merge(row))
            case Some(money) =>
              // wenn nächste Zeile leer oder ebenfalls mit Preis belegt, neues Offer beginnen
              if (row.name.isEmpty || row.priceOpt.isDefined) {
                mergedRows :+= mixedRow
                curMergedRowOpt = Some(row)
              }
              // wenn die Zeilen auf eine Fortsetzung hindeuten, mergen
              else if (Seq(",", " mit", " an").exists(mixedRow.name.endsWith) || row.name(0).isLower || "&(".contains(row.name(0))) {
                curMergedRowOpt = Some(mixedRow.merge(row))
              }
              // andernfalls neues Offer beginnen
              else {
                mergedRows :+= mixedRow
                curMergedRowOpt = Some(row)
              }
          }
      }
    )
    curMergedRowOpt.foreach(mixedRow => if (mixedRow.isValid) mergedRows :+= mixedRow)

    for (row <- mergedRows;
         day <- optMonday.map(_.plusDays(section.order)))
    yield LunchOffer(0, row.name, day, row.priceOpt.get, LunchProvider.HOTEL_AM_RING.id)
  }

  private def multiplyWochenangebote(wochenOffers: Seq[LunchOffer], dates: Seq[LocalDate]): Seq[LunchOffer] = {
    val sortedDates = dates.toSet[LocalDate].toList.sortBy(_.toDate)
    wochenOffers.flatMap ( offer => sortedDates.map( date => offer.copy(day = date)) )
  }

  private def parseDay(dayString: String): Option[LocalDate] = dayString match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case r""".*(\d{2}.\d{2}.\d{2})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yy")
    case r""".*(\d{2}.\d{2}.)$dayString.*""" =>
      val yearToday = LocalDate.now.getYear
      val year = if (LocalDate.now.getMonthOfYear == 12 && dayString.endsWith("01.")) yearToday + 1 else yearToday
      parseLocalDate(dayString + year.toString, "dd.MM.yyyy")
    case _ => None
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   * @param priceString String im Format "*0,00*"
   * @return
   */
  private def parsePrice(priceString: String): Option[Money] = priceString match {
    case r""".*(\d{1,})$major[.,](\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }


  private def parseName(text: String): String = text.trim.replaceAll("  ", " ").replaceAll("–", "-")

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(DateTimeFormat.forPattern(dateFormat).parseLocalDate(dateString))
    } catch {
      case exc: Throwable => None
    }

  private def extractPdfContent(pdfUrl: URL): String = {
    var optPdfDoc: Option[PDDocument] = None
    var pdfContent = ""

    try {
      optPdfDoc = Some(PDDocument.load(pdfUrl))
      for (pdfDoc <- optPdfDoc) {
        val stripper = new PDFTextStripper
        pdfContent = stripper.getText(pdfDoc)
      }
    } catch {
      case fnf: FileNotFoundException => System.out.println(s"file $pdfUrl not found") // TODO: loggen
      case t: Throwable => System.out.println(t.getMessage) // TODO: loggen
    } finally {
      optPdfDoc.foreach(_.close())
    }
    pdfContent
  }
}
