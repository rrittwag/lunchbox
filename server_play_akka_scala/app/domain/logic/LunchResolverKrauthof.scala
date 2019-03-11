package domain.logic

import java.io.FileNotFoundException
import java.net.URL
import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}

import domain.models.{LunchOffer, LunchProvider}
import domain.util.{PDFTextGroupStripper, TextLine}
import domain.util.Html
import org.apache.pdfbox.pdmodel.PDDocument
import org.joda.money.{CurrencyUnit, Money}
import util.PlayLogging

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex

class LunchResolverKrauthof(dateValidator: DateValidator) extends LunchResolver with PlayLogging {

  sealed abstract class PdfSection(val sectionStartPattern: String, val order: Int)

  object PdfSection {
    case object HEADER extends PdfSection("<<Header>>", 0)
    case object MONTAG extends PdfSection("Montag", 0)
    case object DIENSTAG extends PdfSection("Dienstag", 1)
    case object MITTWOCH extends PdfSection("Mittwoch", 2)
    case object DONNERSTAG extends PdfSection("Donnerstag", 3)
    case object FREITAG extends PdfSection("Freitag", 4)
    case object FOOTER extends PdfSection("<<Footer>>", 0)

    val weekdaysValues = List[PdfSection](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = weekdaysValues // ++ List(SALAT_DER_WOCHE, FOOTER)
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
    Future { resolvePdfLinks(new URL("https://www.daskrauthof.de/karte")) }
      .flatMap(pdfPaths => resolveFromPdfs(pdfPaths))

  private[logic] def resolvePdfLinks(htmlUrl: URL): Seq[String] = {
    val siteAsXml = Html.load(htmlUrl)

    val links = (siteAsXml \\ "a").map(_ \@ "href")
    links.filter(_ matches """.*/KRAUTHOF.*.pdf""").toList
  }

  private def resolveFromPdfs(pdfPaths: Seq[String]): Future[Seq[LunchOffer]] = {
    val listOfFutures = pdfPaths.map(pdfPath =>
      Future { resolveFromPdf(new URL(pdfPath)) })
    Future.sequence(listOfFutures).map(listOfLists => listOfLists.flatten)
  }

  private[logic] def resolveFromPdf(pdfUrl: URL): Seq[LunchOffer] = {
    val pdfContent = extractPdfContent(pdfUrl)

    parseMondayFromContent(pdfContent)
      .filter(dateValidator.isValid)
      .map(resolveFromPdfContent(pdfContent, _))
      .getOrElse(Nil)
  }

  private[logic] def resolveFromPdfContent(pdfContent: Seq[String], monday: LocalDate): Seq[LunchOffer] = {
    var rows = Seq[OfferRow]()
    var currentRow: Option[OfferRow] = None

    def finishOffer() = {
      currentRow.foreach(row => if (row.isValid) rows :+= row)
      currentRow = None
    }

    for (line <- pdfContent) {
      line.trim match {
        case r"""(.+)$text(\d{1,}[.,]\d{2})$priceString *€\*""" =>
          finishOffer()
          currentRow = Some(OfferRow(parseName(text), parsePrice(priceString)))
        case "" | r""".*inklusive Tagesgetränk.*""" =>
          finishOffer()
        case text =>
          val thisRow = OfferRow(parseName(text), None)
          currentRow = currentRow match {
            case Some(offer) => Some(offer.merge(thisRow))
            case None => Some(thisRow)
          }
      }
    }

    PdfSection.weekdaysValues.flatMap(
      weekday => rows.map(
        row => LunchOffer(0, row.name, monday.plusDays(weekday.order), row.priceOpt.get, LunchProvider.DAS_KRAUTHOF.id)))
  }

  private def extractPdfContent(pdfUrl: URL): Seq[String] = {
    var optPdfDoc: Option[PDDocument] = None
    var pdfContent = Seq[TextLine]()

    try {
      optPdfDoc = Some(PDDocument.load(pdfUrl))
      for (pdfDoc <- optPdfDoc) {
        val stripper = new PDFTextGroupStripper
        pdfContent = stripper.getTextLines(pdfDoc)
      }
    } catch {
      case fnf: FileNotFoundException => logger.error(s"file $pdfUrl not found")
      case t: Throwable => logger.error(s"Fehler beim Einlesen von $pdfUrl", t)
    } finally {
      optPdfDoc.foreach(_.close())
    }
    pdfContent.map(_.toString)
  }

  private[logic] def parseMondayFromContent(lines: Seq[String]): Option[LocalDate] = {
    // alle Datumse aus PDF ermitteln
    val days = lines.flatMap(line => parseDay(line.toString))
    val mondays = days.map(_.`with`(DayOfWeek.MONDAY))
    // den Montag der am häufigsten verwendeten Woche zurückgeben
    mondays match {
      case Nil => None
      case _ => Option(mondays.groupBy(identity).maxBy(_._2.size)._1)
    }
  }

  private def parseDay(dayString: String): Option[LocalDate] = dayString match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case r""".*(\d{2}.\d{2}.\d{2})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yy")
    case r""".*(\d{2}.\d{2})$dayString.*""" =>
      val yearToday = LocalDate.now.getYear
      val year = if (LocalDate.now.getMonthValue == 12 && dayString.endsWith("01")) yearToday + 1 else yearToday
      parseLocalDate(dayString + "." + year.toString, "dd.MM.yyyy")
    case _ => None
  }

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString)))
    } catch {
      case exc: Throwable => None
    }

  private def parseName(text: String): String = text.trim
    .replaceAll("  ", " ")
    .replaceAll("–", "-")
    .replaceAll(""" *[\│\|] *""", ", ")
    .replaceAll(" I ", ", ")
    .replaceAll(" *,", ",")
    .trim

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param priceString String im Format "*0,00*"
   * @return
   */
  private def parsePrice(priceString: String): Option[Money] = priceString match {
    case r""".*(\d{1,})$major[.,](\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

}
