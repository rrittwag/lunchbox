package domain.logic

import java.net.URL

import domain.models.LunchOffer
import domain.models.LunchProvider.SCHWEINESTALL
import domain.util.Html
import domain.util.Html._
import org.apache.commons.lang3.StringEscapeUtils
import org.joda.money.{CurrencyUnit, Money}
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex
import scala.xml._
import parsing._

class LunchResolverSchweinestall(util: DateValidator) extends LunchResolver {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  override def resolve: Future[Seq[LunchOffer]] =
    Future(resolve(new URL("http://www.schweinestall-nb.de/index.php?id=159")))

  private[logic] def resolve(url: URL): Seq[LunchOffer] = {
    var result = Seq[LunchOffer]()

    val siteAsXml = Html.load(url, "iso-8859-1")

    // Die Tabelle 'cal_content' enthält die Wochenangebote
    val offersTable = (siteAsXml \\ "table") filter hasId("cal_content")
    val tdsInOffersTable = offersTable \\ "td"

    for (
      fiveTDsForOneOffer <- tdsInOffersTable.grouped(5) /* je 5 td-Elemente sind ein Offer, aber ... */ if fiveTDsForOneOffer.length >= 3
    ) {
      // ... nur die ersten 3 td sind nützlich
      val Seq(firstTD, secondTD, thirdTD, _*) = fiveTDsForOneOffer

      for (
        day <- parseDay(firstTD);
        price <- parsePrice(secondTD);
        name <- parseName(thirdTD)
      ) result :+= LunchOffer(0, name, day, price, SCHWEINESTALL.id)
    }
    result
  }

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   *
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parseDay(node: Node): Option[LocalDate] = node.text match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case _ => None
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parsePrice(node: Node): Option[Money] = node.text match {
    case r""".*(\d{1,})$major[.,](\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

  private def parseName(node: Node): Option[String] =
    Some(
      node.text.trim
        .replaceAll("„", "")
        .replaceAll("“", ""))

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString)))
    } catch {
      case exc: Throwable => None
    }
}
