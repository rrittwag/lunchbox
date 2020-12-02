package domain.logic

import java.net.URL
import java.time.{DayOfWeek, LocalDate}
import java.time.format.DateTimeFormatter

import domain.models.{LunchOffer, LunchProvider}
import domain.util.Html
import domain.util.Html._
import infrastructure.OcrClient
import org.joda.money.{CurrencyUnit, Money}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex

/**
 * Mittagsangebote von Feldküche Rühlow ermitteln.
 */
class LunchResolverFeldkueche(
    dateValidator: DateValidator,
    ocrClient: OcrClient) extends LunchResolver {

  sealed abstract class Weekday(val name: String, val order: Int)

  object Weekday {
    case object MONTAG extends Weekday("Montag", 0)
    case object DIENSTAG extends Weekday("Dienstag", 1)
    case object MITTWOCH extends Weekday("Mittwoch", 2)
    case object DONNERSTAG extends Weekday("Donnerstag", 3)
    case object FREITAG extends Weekday("Freitag", 4)

    val weekdaysValues = List[Weekday](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = weekdaysValues
  }

  override def resolve: Future[Seq[LunchOffer]] =
    Future { resolveImageLinks(new URL("https://www.feldkuechebkarow.de/speiseplan")) }
      .flatMap(imageUrls => resolveFromImageLinks(imageUrls))

  private[logic] def resolveImageLinks(htmlUrl: URL): Seq[URL] = {
    val siteAsXml = Html.load(htmlUrl)

    val divsWithContent = (siteAsXml \\ "div") filter hasId("content_area")
    val links = (divsWithContent \\ "a").map(_ \@ "href")
    links.map { link => new URL(link) }.toSeq
  }

  private def resolveFromImageLinks(imageUrls: Seq[URL]): Future[Seq[LunchOffer]] = Future.sequence {
    imageUrls.map(imageUrl => resolveTextFromImageLink(imageUrl))
  }.map(_.flatten)

  private def resolveTextFromImageLink(imageUrl: URL): Future[Seq[LunchOffer]] =
    ocrClient.doOCR(imageUrl).map(resolveOffersFromText)

  private[logic] def resolveOffersFromText(rawText: String): Seq[LunchOffer] = {
    val contentAsLines = rawText.split('\n').map(correctOcrErrors)
    val mondayOpt = resolveMonday(contentAsLines)

    val rawOffers =
      if (Weekday.values.exists(weekday => contentAsLines.contains(weekday.name)))
        resolveOffersWith3rowSplit(contentAsLines)
      else
        resolveOffersWith2rowSplit(contentAsLines)

    for (
      (weekday, name, price) <- rawOffers;
      monday <- mondayOpt
    ) yield LunchOffer(0, name, monday.plusDays(weekday.order), price, LunchProvider.FELDKUECHE.id)
  }

  private def resolveMonday(contentAsLines: Seq[String]): Option[LocalDate] =
    for (
      wochenplanZeile <- contentAsLines.find(_.contains("Wochenplan"));
      day <- parseDay(wochenplanZeile);
      monday = day.`with`(DayOfWeek.MONDAY) if dateValidator.isValid(monday)
    ) yield monday

  private def resolveOffersWith2rowSplit(contentAsLines: Seq[String]): Seq[(Weekday, String, Money)] = {
    val weekdaysRegex = s"(${Weekday.values.map(_.name).mkString("|")}) .*"
    val offerTexts = contentAsLines.filter(_.matches(weekdaysRegex)).filterNot(_.startsWith("Montag bis"))

    def splitWeekdayAndName(text: String): Option[(Weekday, String)] = {
      val (weekdayName, lunchName) = text.span(_ != ' ')
      Weekday.values.find(_.name == weekdayName) match {
        case Some(weekday) => Some(weekday, lunchName.replaceAll(""" \d{1,},\d{2} €""", "").trim)
        case _ => None
      }
    }

    val prices = contentAsLines.filter(_.matches(""".*\d{1,},(\d{2}) €""")).flatMap(parsePrice)

    offerTexts
      .flatMap(splitWeekdayAndName)
      .zip(prices)
      .map{ case ((weekday, name), price) => (weekday, name, price) }
  }

  private def resolveOffersWith3rowSplit(contentAsRows: Seq[String]): Seq[(Weekday, String, Money)] = {
    def isWeekday(text: String) = Weekday.values.map(_.name).contains(text)
    val (weekdayTextRows, followingRows) =
      contentAsRows
        .filter(_.nonEmpty)
        .dropWhile(line => !isWeekday(line))
        .span(isWeekday)

    val weekdayRows = weekdayTextRows.flatMap(row => Weekday.values.find(_.name == row))
    val prices = followingRows.filter(_.matches(""".*\d{1,},(\d{2}) €""")).flatMap(parsePrice)

    weekdayRows
      .zip(followingRows)
      .zip(prices)
      .map{ case ((weekday, name), price) => (weekday, name, price) }
  }

  private def correctOcrErrors(line: String) =
    line.trim
      .replaceAll("Wochenglan", "Wochenplan")
      .replaceAll("‚", ",")
      .replaceAll("""l\)""", "0")
      .replaceAll("""(\d{1,},\d{2}) [cCeEs]""", "$1 €")
      //      .replaceAll("4,00 €", "4,80 €") // besser falsch zu hohe 4,80 als zu niedrige 4,00
      .replaceAll("Kanonen-", "Kartoffeln")
      .replaceAll("ﬂ", "fl")
      .replaceAll("ﬁ", "fi")
      .replaceAll("Wochen.lan", "Wochenplan")
      .replaceAll("Wochen.Ian", "Wochenplan")
      .replaceAll("eintopl", "eintopf")
      .replaceAll("eintop", "eintopf")
      .replaceAll("eintof", "eintopf")
      .replaceAll("eintopff", "eintopf")
      .replaceAll("Erhsen", "Erbsen")
      .replaceAll("Chili con Garne", "Chili con Carne")
      .replaceAll("—", "-")

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   *
   * @param text auszuwertender Text
   * @return
   */
  private def parseDay(text: String): Option[LocalDate] = text match {
    case r""".*(\d{2}\.\d{2}\.\d{2})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yy")
    case _ => None
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param text auszuwertender Text
   * @return
   */
  private def parsePrice(text: String): Option[Money] = text match {
    case r""".*(\d{1,})$major,(\d{2})$minor €""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString)))
    } catch {
      case exc: Throwable => None
    }

}
