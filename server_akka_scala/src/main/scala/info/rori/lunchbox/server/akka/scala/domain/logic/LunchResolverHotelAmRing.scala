package info.rori.lunchbox.server.akka.scala.domain.logic

import java.io.FileNotFoundException
import java.net.URL

import grizzled.slf4j.Logging
import info.rori.lunchbox.server.akka.scala.domain.model._
import info.rori.lunchbox.server.akka.scala.domain.util.{TextLine, PDFTextGroupStripper}
import org.apache.pdfbox.pdmodel.PDDocument
import org.htmlcleaner.{CleanerProperties, HtmlCleaner}
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.matching.Regex


class LunchResolverHotelAmRing(dateValidator: DateValidator) extends LunchResolver with Logging {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  sealed abstract class PdfSection(val sectionStartPattern: String, val order: Int)

  object PdfSection {
    case object HEADER extends PdfSection("<<Header>>", 0)
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

  case class OfferRow(name: String, priceOpt: Option[Money], startsWithBoldText: Boolean = false) {
    def merge(otherRow: OfferRow): OfferRow = {
      val newName = Seq(name, otherRow.name).filter(!_.isEmpty).mkString(" ")
      OfferRow(newName, priceOpt.orElse(otherRow.priceOpt), startsWithBoldText)
    }

    def isValid = !name.isEmpty && priceOpt.isDefined
  }


  override def resolve: Future[Seq[LunchOffer]] =
    Future { resolvePdfLinks(new URL("http://www.hotel-am-ring.de/restaurant-rethra.html")) }
      .flatMap(relativePdfPaths => resolveFromPdfs(relativePdfPaths))

  private[logic] def resolvePdfLinks(htmlUrl: URL): Seq[String] = {
    val props = new CleanerProperties
    props.setCharset("utf-8")

    val rootNode = new HtmlCleaner(props).clean(htmlUrl)
    val links = rootNode.evaluateXPath("//a/@href").map { case n: String => n}.toSet

    links.filter(_ matches """.*/Mittagspause_.+.pdf""").toList
  }

  private def resolveFromPdfs(relativePdfPaths: Seq[String]): Future[Seq[LunchOffer]] = {
    val listOfFutures = relativePdfPaths.map( relativePdfPath =>
      Future { resolveFromPdf(new URL("http://www.hotel-am-ring.de/" + relativePdfPath)) }
    )
    Future.sequence(listOfFutures).map(listOfLists => listOfLists.flatten)
  }

  private[logic] def resolveFromPdf(pdfUrl: URL): Seq[LunchOffer] = {
    val pdfContent = extractPdfContent(pdfUrl)

    parseMondayFromContent(pdfContent)
      .filter(dateValidator.isValid)
      .map(resolveFromPdfContent(pdfContent, _))
      .getOrElse(Nil)
  }

  private[logic] def resolveFromPdfContent(pdfContent: Seq[TextLine], monday: LocalDate): Seq[LunchOffer] = {
    val section2content = groupBySection(pdfContent)

    val section2offers = section2content.map {
      case (section, secContent) => (section, parseOffersFromSectionLines(secContent, section, monday))
    }

    // Wochenangebote über die Woche verteilen
    val (wochenOffers, tagesOffers) = section2offers.toList.partition(_._1 == PdfSection.SALAT_DER_WOCHE)
    val tagesOffersList = tagesOffers.flatMap(_._2)

    val multipliedWochenOffers = multiplyWochenangebote(wochenOffers.flatMap(_._2), tagesOffersList.map(_.day))
    tagesOffersList ++ multipliedWochenOffers
  }

  private[logic] def parseMondayFromContent(lines: Seq[TextLine]): Option[LocalDate] = parseMondayByTexts(lines.map(_.toString))

  private[logic] def parseMondayByTexts(lines: Seq[String]): Option[LocalDate] = {
    // alle Datumse aus PDF ermitteln
    val days = lines.flatMap( line => parseDay(line.toString) )
    val mondays = days.map(_.withDayOfWeek(1))
    // den Montag der am häufigsten verwendeten Woche zurückgeben
    mondays match {
      case Nil => None
      case _ => Option(mondays.groupBy(identity).maxBy(_._2.size)._1)
    }
  }

  private def groupBySection(lines: Seq[TextLine]): Map[PdfSection, Seq[TextLine]] = {
    var result = Map[PdfSection, Seq[TextLine]]()

    var currentSection: PdfSection = PdfSection.HEADER
    var linesForSection = Seq[TextLine]()

    for (line <- lines) {
      PdfSection.values.find(sec => line.toString.contains(sec.sectionStartPattern)) match {
        case Some(newSection) =>
          result += currentSection -> linesForSection
          currentSection = newSection
          linesForSection = Nil
          if (containsPrice(line)) linesForSection :+= line // die "Salat der Woche"-Titelzeile enthält neuerdings den Preis
        case None =>
          linesForSection :+= line
      }
    }

    result
  }

  private def parseOffersFromSectionLines(sectionLines: Seq[TextLine], section: PdfSection, monday: LocalDate): Seq[LunchOffer] = {
    var rows = appendBoldTextInfo(sectionLines).flatMap { case (line, startsWithBoldText) =>
      line.toString.trim match {
        case r"""(.+)$text(\d{1,}[.,]\d{2})$priceString *€""" =>
          Some(OfferRow(parseName(text), parsePrice(priceString), startsWithBoldText))
        case "" =>
          None // leere Zeile entfernen
        case text =>
          Some(OfferRow(parseName(text), None, startsWithBoldText))
      }
    }

    rows = rows match {
      // Mittwochs-Titelzeile "Buffettag" mit nächster Zeile mergen
      case first :: second :: remain if section == PdfSection.MITTWOCH =>
        OfferRow(s"${first.name}: ${second.name}", first.priceOpt.orElse(second.priceOpt)) :: remain
      // "Salat der Woche: " beim Salat der Woche voranstellen, wenn es denn noch nicht vorhanden ist
      case first :: remain if section == PdfSection.SALAT_DER_WOCHE && !first.name.startsWith("Salat der Woche") =>
        OfferRow(s"Salat der Woche: ${first.name}", first.priceOpt) :: remain
      case r => r
    }

    rows =
      if (section == PdfSection.SALAT_DER_WOCHE)
        mergeRowsToOneRow(rows, section)
      else if (section == PdfSection.MITTWOCH && rows.exists(_.name contains "Buffet"))
        mergeRowsToOneRow(rows, section)
      else if (rows.exists(_.startsWithBoldText))
        mergeRowsByBoldText(rows, section)
      else
        mergeRowsUnformatted(rows, section)

    rows.map(row => LunchOffer(0, row.name, monday.plusDays(section.order), row.priceOpt.get, LunchProvider.HOTEL_AM_RING.id))
  }

  /**
   * Gibt für jede Zeile an, ob sie fettgedruckt beginnt.
   * @param sectionLines Zeilen
   * @return Tupels aus Zeile und bool'schem Wert "Zeile startet fettgedruckt".
   */
  private def appendBoldTextInfo(sectionLines: Seq[TextLine]): Seq[(TextLine, Boolean)] = {
    val positions = sectionLines.flatMap(line => line.texts.flatMap(_.positions))
    val boldFontOpt = positions.find(_.getCharacter == "€").map(_.getFont) // Der Preis ist immer fett gedruckt => Referenz-Font
    val usesMultipleFonts = boldFontOpt.exists(boldFont => positions.exists(_.getFont != boldFont))
    sectionLines.map { line =>
      line.texts.flatMap(_.positions).filter(!_.toString.trim.isEmpty) match {
        case firstChar :: remain =>
          (line, usesMultipleFonts && boldFontOpt.contains(firstChar.getFont))
        case _ =>
          (line, false)
      }
    }
  }

  private def mergeRowsToOneRow(rows: Seq[OfferRow], section: PdfSection): Seq[OfferRow] = {
    var mergedRowOpt: Option[OfferRow] = None
    rows.foreach(row =>
      mergedRowOpt match {
        case None => mergedRowOpt = Some(row)
        case Some(mergedRow) => mergedRowOpt = Some(mergedRow.merge(row))
      }
    )
    mergedRowOpt.fold(Seq[OfferRow]())(Seq(_))
  }

  private def mergeRowsByBoldText(rows: Seq[OfferRow], section: PdfSection): Seq[OfferRow] = {
    // Rows mergen
    var mergedRows = Seq[OfferRow]()
    var curMergedRowOpt: Option[OfferRow] = None
    rows.foreach(row =>
      curMergedRowOpt match {
        case None =>
          if (row.startsWithBoldText)
            curMergedRowOpt = Some(row)
        case Some(curMergedRow) =>
          if (row.startsWithBoldText) {
            if (curMergedRow.isValid) mergedRows :+= curMergedRow
            curMergedRowOpt = Some(row)
          } else
            curMergedRowOpt = Some(curMergedRow.merge(row))
      }
    )
    curMergedRowOpt.foreach(curMergedRow => if (curMergedRow.isValid) mergedRows :+= curMergedRow)
    mergedRows
  }

  private def mergeRowsUnformatted(rows: Seq[OfferRow], section: PdfSection): Seq[OfferRow] = {
    // Rows mergen
    var mergedRows = Seq[OfferRow]()
    var curMergedRowOpt: Option[OfferRow] = None
    rows.foreach(row =>
      curMergedRowOpt match {
        case None => curMergedRowOpt = Some(row)
        case Some(curMergedRow) =>
          curMergedRow.priceOpt match {
            case None =>
              curMergedRowOpt = Some(curMergedRow.merge(row))
            case Some(money) =>
              // wenn nächste Zeile leer oder ebenfalls mit Preis belegt, neues Offer beginnen
              if (row.name.isEmpty || row.priceOpt.isDefined) {
                mergedRows :+= curMergedRow
                curMergedRowOpt = Some(row)
              }
              // wenn die Zeilen auf eine Fortsetzung hindeuten, mergen
              else if (Seq(",", " mit", " an").exists(curMergedRow.name.endsWith) || row.name(0).isLower || "&(".contains(row.name(0))) {
                curMergedRowOpt = Some(curMergedRow.merge(row))
              }
              // andernfalls neues Offer beginnen
              else {
                mergedRows :+= curMergedRow
                curMergedRowOpt = Some(row)
              }
          }
      }
    )
    curMergedRowOpt.foreach(curMergedRow => if (curMergedRow.isValid) mergedRows :+= curMergedRow)
    mergedRows
  }

  private def containsPrice(line: TextLine) = line.toString.trim.matches(""".+\d{1,}[.,]\d{2} *€""")

  private def multiplyWochenangebote(wochenOffers: Seq[LunchOffer], dates: Seq[LocalDate]): Seq[LunchOffer] = {
    val sortedDates = dates.toSet[LocalDate].toList.sortBy(_.toDate)
    wochenOffers.flatMap ( offer => sortedDates.map( date => offer.copy(day = date)) )
  }

  private def parseDay(dayString: String): Option[LocalDate] = dayString match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case r""".*(\d{2}.\d{2}.\d{2})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yy")
    case r""".*(\d{2}.\d{2})$dayString.*""" =>
      val yearToday = LocalDate.now.getYear
      val year = if (LocalDate.now.getMonthOfYear == 12 && dayString.endsWith("01")) yearToday + 1 else yearToday
      parseLocalDate(dayString + "." + year.toString, "dd.MM.yyyy")
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

  private def extractPdfContent(pdfUrl: URL): Seq[TextLine] = {
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
    pdfContent
  }
}
