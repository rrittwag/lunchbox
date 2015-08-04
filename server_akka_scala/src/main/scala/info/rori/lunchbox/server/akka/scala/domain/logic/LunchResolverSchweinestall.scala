package info.rori.lunchbox.server.akka.scala.domain.logic

import java.net.URL

import info.rori.lunchbox.server.akka.scala.domain.model.LunchOffer
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider.SCHWEINESTALL
import org.apache.commons.lang3.StringEscapeUtils
import org.htmlcleaner.{CleanerProperties, TagNode, HtmlCleaner}
import org.joda.money.{CurrencyUnit, Money}
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

import util.matching.Regex

class LunchResolverSchweinestall(util: DateValidator) extends LunchResolver {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  override def resolve: Future[Seq[LunchOffer]] =
    Future(resolve(new URL("http://www.schweinestall-nb.de/index.php?id=159")))

  private[logic] def resolve(url: URL): Seq[LunchOffer] = {
    var result = Seq[LunchOffer]()

    val props = new CleanerProperties
    props.setCharset("iso-8859-1")
    val rootNode = new HtmlCleaner(props).clean(url)

    // Die Tabelle 'cal_content' enthält die Wochenangebote
    val tdsInOffersTable = rootNode.evaluateXPath("//table[@id='cal_content']//td").map { case n: TagNode => n}

    for (fiveTDsForOneOffer <- tdsInOffersTable.grouped(5) /* je 5 td-Elemente sind ein Offer, aber ... */
         if fiveTDsForOneOffer.length >= 3) {
      // ... nur die ersten 3 td sind nützlich
      val Array(firstTD, secondTD, thirdTD, _*) = fiveTDsForOneOffer

      for (day <- parseDay(firstTD);
           price <- parsePrice(secondTD);
           name <- parseName(thirdTD))
        result :+= LunchOffer(0, name, day, price, SCHWEINESTALL.id)
    }
    result
  }

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parseDay(node: TagNode): Option[LocalDate] = node.getText match {
    case r""".*(\d{2}.\d{2}.\d{4})$dayString.*""" => parseLocalDate(dayString, "dd.MM.yyyy")
    case _ => None
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   * @param node HTML-Node mit auszuwertendem Text
   * @return
   */
  private def parsePrice(node: TagNode): Option[Money] = node.getText match {
    case r""".*(\d{1,})$major[.,](\d{2})$minor.*""" => Some(Money.ofMinor(CurrencyUnit.EUR, major.toInt * 100 + minor.toInt))
    case _ => None
  }

  private def parseName(node: TagNode): Option[String] = Some(StringEscapeUtils.unescapeHtml4(node.getText.toString.trim))

  private def parseLocalDate(dateString: String, dateFormat: String): Option[LocalDate] =
    try {
      Some(DateTimeFormat.forPattern(dateFormat).parseLocalDate(dateString))
    } catch {
      case exc: Throwable => None
    }
}
