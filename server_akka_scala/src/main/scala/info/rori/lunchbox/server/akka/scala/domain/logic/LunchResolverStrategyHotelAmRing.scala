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

class LunchResolverStrategyHotelAmRing extends LunchResolverStrategy {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  sealed abstract class PdfSection(val sectionStartPattern: String, val order: Int)

  object PdfSection {
    case object MONTAG extends PdfSection("Montag, ", 0)
    case object DIENSTAG extends PdfSection("Dienstag, ", 1)
    case object MITTWOCH extends PdfSection("Mittwoch, ", 2)
    case object DONNERSTAG extends PdfSection("Donnerstag, ", 3)
    case object FREITAG extends PdfSection("Freitag, ", 4)
    case object SALAT_DER_WOCHE extends PdfSection("Salat der Woche", 6)
    case object FOOTER extends PdfSection("Alle Gerichte beinhalten", 7)

    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = List[PdfSection](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG, SALAT_DER_WOCHE, FOOTER)
  }


  override def resolve: Seq[LunchOffer] = {
    val props = new CleanerProperties
    props.setCharset("utf-8")
    val rootNode = new HtmlCleaner(props).clean(new URL("http://www.hotel-am-ring.de/restaurant-rethra.html"))

    val links = rootNode.evaluateXPath("//a/@href").map { case n: String => n}.toSet
    val linksToPdf = links.filter( _ matches """.*/Mittagspause_.+(\d{2}.\d{2}.\d{2,4})\D*.pdf""" ).toSeq

    linksToPdf.flatMap( relativePdfPath =>
      resolveFromPdf(new URL("http://www.hotel-am-ring.de/" + relativePdfPath))
    )
  }

  private def resolveFromPdf(pdfUrl: URL):Seq[LunchOffer] = {
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

    section2content.flatMap {
      case (section, secContent) => parseOffersFromSectionString(secContent, section, optMonday)
    }
  }

  def parseMondayFromUrl(pdfUrl: URL) : Option[LocalDate] = pdfUrl.getFile match {
    case r""".*(\d{2}.\d{2}.\d{2,4})$fridayString\D*.pdf""" => parseDay(fridayString).map(_.minusDays(4))
    case _ => None
  }

  def parseOffersFromSectionString(sectionContent: String, section: PdfSection, optMonday: Option[LocalDate]): List[LunchOffer] = {
    val offersContent = sectionContent.substring(sectionContent.indexOf("\n") + 1)
    offersContent.split("€").flatMap(offerString => parseOfferFromOfferString(offerString, section, optMonday)).toList
  }

  def parseOfferFromOfferString(offerString: String, section: PdfSection, optMonday: Option[LocalDate]): List[LunchOffer] = {
    var rows = offerString.split("\n")

    if (section == PdfSection.MITTWOCH)
      rows = s"${parseName(rows.head)}:" +: rows.tail

    val offerStringWithoutWhitespaces = rows.map(parseName).mkString(" ")

    offerStringWithoutWhitespaces match {
      case r"""(.+)$text(\d{1,},\d{2})$priceString {0,1}""" =>
        for (day <- daysForOffer(optMonday, section))
        yield LunchOffer(0, parseName(text), day, parsePrice(priceString).get, LunchProvider.HOTEL_AM_RING.id)
      case _ => Nil
    }
  }

  private def daysForOffer(optMonday: Option[LocalDate], section: PdfSection): List[LocalDate] = {
    var dayNumbers = List(section.order)
    if (section == PdfSection.SALAT_DER_WOCHE)
      dayNumbers = List(0, 1, 2, 3, 4) // TODO: Feiertage müssen außen vor bleiben

    for (curDayNr <- dayNumbers;
         monday <- optMonday) yield monday.plusDays(curDayNr)
  }

  private def parseDay(dayString: String): Option[LocalDate] = dayString match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case r""".*(\d{2}.\d{2}.\d{2})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yy")
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


  private def parseName(text: String): String = text.toString.trim.replaceAll("  ", " ")

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(DateTimeFormat.forPattern(dateFormat).parseLocalDate(dateString))
    } catch {
      case exc: Throwable => None
    }

  private def extractPdfContent(pdfUrl: URL):String = {
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
      optPdfDoc.map(_.close())
    }
    pdfContent
  }
}
/*
object RunStrategy extends App {
  System.out.println(new LunchResolverStrategyHotelAmRing().resolve)
}
*/