package domain.logic

import java.net.URL
import java.time.{DayOfWeek, LocalDate}
import java.time.format.DateTimeFormatter

import domain.models.LunchOffer
import domain.models.LunchProvider.TABBOULEH
import domain.util.Html
import domain.util.Html._
import org.apache.commons.lang3.StringEscapeUtils
import org.joda.money.{CurrencyUnit, Money}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.matching.Regex
import scala.xml._
import parsing._

class LunchResolverTabbouleh(util: DateValidator) extends LunchResolver {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  sealed abstract class Weekday(val name: String, val order: Int)

  object Weekday {
    case object MONTAG extends Weekday("Montag", 0)
    case object DIENSTAG extends Weekday("Dienstag", 1)
    case object MITTWOCH extends Weekday("Mittwoch", 2)
    case object DONNERSTAG extends Weekday("Donnerstag", 3)
    case object FREITAG extends Weekday("Freitag", 4)

    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = List[Weekday](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
  }

  override def resolve: Future[Seq[LunchOffer]] =
    Future(resolve(new URL("https://www.restaurant-tabbouleh.de/menu/")))

  private[logic] def resolve(url: URL): Seq[LunchOffer] = {
    val siteAsXml = Html.load(url)

    val pricesAsString =
      (siteAsXml \\ "h2")
        .map(_.text)
        .find(_.contains("€")).getOrElse("")
    // 2 Preise: [0] = vegetarisch, [1] = fleischlich
    val prices = pricesAsString.split("/").map(parsePrice).flatten

    val contentDivOpt = (siteAsXml \\ "div") find hasId("av_section_2")

    val offersOpt = for (contentDiv <- contentDivOpt) yield {
      val wochenplanHeaders = (contentDiv \\ "div") filter hasClass("el_before_av_toggle_container")
      val wochenplanBodies = (contentDiv \\ "div") filter hasClass("togglecontainer")

      val day2wochenNode = wochenplanHeaders.map(parseDate).zip(wochenplanBodies)
      day2wochenNode.collect{
        case (Some(day), wochenNode) => resolveWochenangebote(day, wochenNode, prices)
      }.flatten
    }
    offersOpt.getOrElse(Seq())
  }

  private[logic] def resolveWochenangebote(dayOfWeek: LocalDate, wochenNode: Node, prices: Seq[Money]): Seq[LunchOffer] = {
    val offers = for (
      tagesNode <- (wochenNode \\ "section");
      weekday <- parseWeekday(tagesNode, dayOfWeek)
    ) yield {
      val gerichte = (tagesNode \\ "_").filter(hasClass("toggle_wrap")).text.split("\n")
        .map(_.replaceAll(" ", " ").replaceAll("  ", " ").trim)
        .filter(_.nonEmpty)
      gerichte.map(name => LunchOffer(0, name, weekday, findPrice(name, prices), TABBOULEH.id)).toSeq
    }
    offers.flatten
  }

  private def parseWeekday(tagesNode: Node, dayOfWeek: LocalDate): Option[LocalDate] = {
    val monday = dayOfWeek.`with`(DayOfWeek.MONDAY)
    val weekdayName = (tagesNode \\ "_").filter(hasClass("toggler")).text.trim
    Weekday.values.find(w => w.name == weekdayName).map(w => monday.plusDays(w.order))
  }

  private def findPrice(name: String, prices: Seq[Money]): Money = {
    if (prices.isEmpty) Money.ofMinor(CurrencyUnit.EUR, 0)
    else if (prices.size == 1) prices(0)
    else {
      val vegetarisches = Seq("vegetarisch", "vegan")
      val fleischiges = Seq("fleisch", "hähnchen", "geschnetzel", "schnitzel", "barsch", "fisch", "hack", "wurst", "lachs", "gulasch", "rinderfrikadelle", "brust", "roulade")
      val lowerName = name.toLowerCase
      if (vegetarisches.exists(t => lowerName.contains(t))) prices(0)
      else if (fleischiges.exists(t => lowerName.contains(t))) prices(1)
      else prices(0)
    }
  }

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   *
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parseDate(node: Node): Option[LocalDate] = node.text match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case _ => None
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parsePrice(text: String): Option[Money] = text match {
    case r""".*(\d{1,})$major[.,](\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString)))
    } catch {
      case exc: Throwable => None
    }
}
