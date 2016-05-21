package domain.logic

import java.net.URL

import domain.models.{LunchProvider, LunchOffer}
import org.apache.commons.lang3.StringEscapeUtils
import org.htmlcleaner.{CleanerProperties, HtmlCleaner, TagNode}
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.matching.Regex

class LunchResolverSaltNPepper(dateValidator: DateValidator) extends LunchResolver {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  sealed abstract class OfferSection(val title: String, val mondayOffset: Int)

  object OfferSection {
    case object MONTAG extends OfferSection("Montag", 0)
    case object DIENSTAG extends OfferSection("Dienstag", 1)
    case object MITTWOCH extends OfferSection("Mittwoch", 2)
    case object DONNERSTAG extends OfferSection("Donnerstag", 3)
    case object FREITAG extends OfferSection("Freitag", 4)
    case object WOCHENANGEBOT extends OfferSection("Unser Wochenangebot", -1)

    val weekdaysValues = List[OfferSection](MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    // TODO: improve with macro, see https://github.com/d6y/enumeration-examples & http://underscore.io/blog/posts/2014/09/03/enumerations.html
    val values = weekdaysValues :+ WOCHENANGEBOT
  }

  override def resolve: Future[Seq[LunchOffer]] =
    Future { resolve(new URL("http://www.partyservice-rohde.de/bistro-angebot-der-woche/")) }

  private[logic] def resolve(url: URL): Seq[LunchOffer] = {
    val props = new CleanerProperties
    props.setCharset("UTF-8")
    val rootNode = new HtmlCleaner(props).clean(url)

    val divs = rootNode.evaluateXPath("//div[@class='wpb_text_column wpb_content_element']").map { case n: TagNode => n }
    val mondayOpt = resolveMonday(divs)

    mondayOpt.map(resolveOffers(divs, _)).getOrElse(Nil)
  }

  private def resolveMonday(nodes: Seq[TagNode]): Option[LocalDate] =
    nodes
      .map(parseName(_).replaceAll("\n", ""))
      .find(_.contains(" Uhr"))
      .flatMap(parseDay)
      .map(toMonday)

  private def resolveOffers(nodes: Seq[TagNode], monday: LocalDate): Seq[LunchOffer] = {
    var section2node = Map[OfferSection, TagNode]()

    for (node <- nodes) {
      val h4Opt = node.evaluateXPath("//h4").map { case n: TagNode => n }.headOption
      val title = h4Opt.map(parseName).getOrElse("")

      for (section <- OfferSection.values)
        if (section.title == title)
          section2node += section -> node
    }

    val (dayNodes, weekNodes) = section2node.partition(section => OfferSection.weekdaysValues.contains(section._1))
    val weekdayOffers = resolveWeekdayOffers(dayNodes, monday)
    weekdayOffers ++ resolveWeekOffers(weekNodes, weekdayOffers.map(_.day).toSet)
  }

  private def resolveWeekdayOffers(section2node: Map[OfferSection, TagNode], monday: LocalDate): Seq[LunchOffer] = {
    var result = Seq[LunchOffer]()
    for ((section, node) <- section2node) {
      val pureOffers = resolveSectionOffers(section, node)
      result ++= pureOffers.map(_.copy(day = monday.plusDays(section.mondayOffset)))
    }
    result
  }

  private def resolveWeekOffers(section2node: Map[OfferSection, TagNode], days: Set[LocalDate]): Seq[LunchOffer] = {
    var result = Seq[LunchOffer]()
    for (
      (section, node) <- section2node;
      pureOffers = resolveSectionOffers(section, node);
      weekday <- days
    ) {
      result ++= pureOffers.map(offer => offer.copy(name = s"Wochenangebot: ${offer.name}", day = weekday))
    }
    result
  }

  private def resolveSectionOffers(section: OfferSection, node: TagNode): Seq[LunchOffer] = {
    val tds = node.evaluateXPath("//td").map { case n: TagNode => n }
    tds.grouped(2).flatMap {
      case Array(nameNode, priceNode) =>
        parsePrice(priceNode).map { price =>
          val name = parseName(nameNode) match {
            case r"""Tipp: (.*)$text""" => text
            case text => text
          }
          LunchOffer(0, name, LocalDate.now(), price, LunchProvider.SALT_N_PEPPER.id)
        }
      case _ => None
    }.toSeq
  }

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   *
   * @param text Text
   * @return
   */
  private def parseDay(text: String): Option[LocalDate] = text match {
    case r""".*(\d{2}\.\d{2}\.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case _ => None
  }

  private def toMonday(day: LocalDate): LocalDate = day.withDayOfWeek(1)

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parsePrice(node: TagNode): Option[Money] = node.getText match {
    case r""".*(\d{1,})$major[\.,](\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

  private def parseName(node: TagNode): String = {
    val pureText = StringEscapeUtils.unescapeHtml4(node.getText.toString.trim)
    pureText.replaceAll("\n", " ").replaceAll("  ", " ")
  }

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(DateTimeFormat.forPattern(dateFormat).parseLocalDate(dateString))
    } catch {
      case exc: Throwable => None
    }
}
