package info.rori.lunchbox.server.akka.scala.domain.logic

import java.io.FileNotFoundException
import java.net.URL

import info.rori.lunchbox.server.akka.scala.domain.model._
import org.apache.pdfbox.pdmodel.PDDocument
import org.htmlcleaner.{CleanerProperties, HtmlCleaner}
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

import scala.util.matching.Regex

class LunchResolverAokCafeteria extends LunchResolver {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  case class TextLine(y: Float, texts: Seq[TextGroup]) {
    def oneTextMatches(regex: String): Boolean = texts.exists(_.toString.matches(regex))

    def allTextsMatch(regex: String): Boolean = texts.forall(_.toString.matches(regex))
  }

  sealed abstract class PdfSection(val name: String, val order: Int)

  object PdfSection {
    case object TABLE_HEADER extends PdfSection("", -1)
    case object MONTAG extends PdfSection("Montag", 0)
    case object DIENSTAG extends PdfSection("Dienstag", 1)
    case object MITTWOCH extends PdfSection("Mittwoch", 2)
    case object DONNERSTAG extends PdfSection("Donnerstag", 3)
    case object FREITAG extends PdfSection("Freitag", 4)

    val weekdayValues = List[PdfSection](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)

    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = TABLE_HEADER :: weekdayValues
  }

  case class OfferRow(name: String, priceOpt: Option[Money]) {
    def merge(otherRow: OfferRow): OfferRow = {
      val newName = List(name, otherRow.name).filter(!_.isEmpty).mkString(" ")
      OfferRow(newName, priceOpt.orElse(otherRow.priceOpt))
    }

    def isValid = !name.isEmpty && priceOpt.isDefined
  }


  override def resolve: Seq[LunchOffer] = {
    val pdfLinks = resolvePdfLinks(new URL("http://www.hotel-am-ring.de/aok-cafeteria.html"))

    pdfLinks.flatMap(relativePdfPath =>
      resolveFromPdf(new URL("http://www.hotel-am-ring.de/" + relativePdfPath))
    )
  }

  private[logic] def resolvePdfLinks(htmlUrl: URL): List[String] = {
    val props = new CleanerProperties
    props.setCharset("utf-8")

    val rootNode = new HtmlCleaner(props).clean(htmlUrl)
    val links = rootNode.evaluateXPath("//a/@href").map { case n: String => n}.toSet

    links.filter(_ matches """.*/AOK_.+(\d{2}.\d{2}.)\D*.pdf""").toList
  }

  private[logic] def resolveFromPdf(pdfUrl: URL): List[LunchOffer] = {
    val optMonday = parseMondayFromUrl(pdfUrl)

    val textGroups = extractPdfContent(pdfUrl)

    val lines = groupByLine(textGroups)
    val section2lines = groupBySection(lines)

    var offers = List[LunchOffer]()

    section2lines.get(PdfSection.TABLE_HEADER) match {
      case Some(List(priceHeader)) =>
        val xCoords = priceHeader.texts.map(_.xMid)
        val prices = priceHeader.texts.flatMap( e => parsePrice(e.toString) )
        for (weekday <- PdfSection.weekdayValues;
             lines <- section2lines.get(weekday);
             (x, price) <- xCoords.zip(prices))
          parseDayOffer(lines, x, weekday, optMonday, price).foreach(offers :+= _)

      case None => // TODO: logging!
    }

    offers
  }

  private[logic] def parseMondayFromUrl(pdfUrl: URL): Option[LocalDate] = pdfUrl.getFile match {
    case r""".*(\d{2}.\d{2}.)$fridayString\D*.pdf""" => parseDay(fridayString).map(_.minusDays(4))
    case _ => None
  }

  private def groupByLine(texts: Seq[TextGroup]): List[TextLine] = {
    var result = Map[Float, Seq[TextGroup]]()
    for (text <- texts) {
      result.keys.find(text.yIn) match {
        case Some(key) => result = result.updated(key, result(key) :+ text)
        case None => result += text.yMid -> Seq(text)
      }
    }
    result.toList.map(e => TextLine(e._1, e._2.sortBy(_.xMin))).sortBy(_.y)
  }

  private def groupBySection(lines: List[TextLine]): Map[PdfSection, List[TextLine]] = {
    var result = Map[PdfSection, List[TextLine]]()

    for (priceHeader <- lines.find(_.allTextsMatch("""^\d{1,},\d{2} *€$""")))
      result += PdfSection.TABLE_HEADER -> List(priceHeader)

    for (List(header) <- result.get(PdfSection.TABLE_HEADER)) {
      val linesBelowHeader = lines.filter(_.y > header.y)

      var linesForDay = List[TextLine]()
      for (line <- linesBelowHeader) {
        if (line.oneTextMatches(""".*Zusatzstoffe.*""")) {
          val weekdayOpt = findWeekdaySection(linesForDay)
          weekdayOpt.foreach( day => result += day -> linesForDay )
          linesForDay = Nil
        } else if (line.oneTextMatches(""".*(Nährwerte|Kohlenhydrate).*""")) {
          // ignore line
        } else {
          linesForDay :+= line
        }
      }
    }

    result
  }

  def parseDayOffer(lines: List[TextLine], xRef: Float, weekday: PdfSection, mondayOpt: Option[LocalDate], price: Money): Option[LunchOffer] = {
    mondayOpt match {
      case Some(monday) =>
        val day = monday.plusDays(weekday.order)
        val name = lines.flatMap(_.texts.filter(_.xIn(xRef))).mkString(" ")
        Some(LunchOffer(0, parseName(name), day, price, LunchProvider.AOK_CAFETERIA.id) )
      case None => None
    }
  }

  private def findWeekdaySection(lines: List[TextLine]): Option[PdfSection] = {
    val weekdaysRegex = PdfSection.weekdayValues.map(_.name).mkString("^(", "|", ")$")
    val weekdayTextGroupOpt = lines.flatMap(_.texts.find(_.toString.matches(weekdaysRegex))).headOption
    weekdayTextGroupOpt.flatMap(e => parseWeekdaySection(e.toString))
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
    case r""".*(\d{1,})$major,(\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }


  private def parseName(text: String): String = text.trim.replaceAll("  ", " ")

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(DateTimeFormat.forPattern(dateFormat).parseLocalDate(dateString))
    } catch {
      case exc: Throwable => None
    }

  private def parseWeekdaySection(weekdayString: String): Option[PdfSection] =
    PdfSection.weekdayValues.find(_.name == weekdayString)

  private def extractPdfContent(pdfUrl: URL): Seq[TextGroup] = {
    var optPdfDoc: Option[PDDocument] = None
    var pdfContent = Seq[TextGroup]()

    try {
      optPdfDoc = Some(PDDocument.load(pdfUrl))
      for (pdfDoc <- optPdfDoc) {
        val stripper = new PDFTextGroupStripper
        pdfContent = stripper.getTextGroups(pdfDoc)
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
