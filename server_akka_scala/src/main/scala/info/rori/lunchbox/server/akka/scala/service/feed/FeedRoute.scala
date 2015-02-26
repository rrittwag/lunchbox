package info.rori.lunchbox.server.akka.scala.service.feed

import akka.http.marshalling._
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.{LunchProvider, LunchOffer}
import info.rori.lunchbox.server.akka.scala.domain.service.{LunchOfferService => LOS}
import info.rori.lunchbox.server.akka.scala.domain.service.{LunchProviderService => LPS}
import info.rori.lunchbox.server.akka.scala.service.{HttpRoute, HttpXmlConversions}
import org.apache.commons.lang3.StringEscapeUtils
import org.joda.time.{DateTimeZone, LocalDate, LocalTime}

import scala.concurrent.ExecutionContext
import scala.xml.Node

object AtomFeedConversion extends HttpXmlConversions {
  implicit def defaultNodeSeqMarshaller(implicit ec: ExecutionContext):ToEntityMarshaller[Node] = atomFeedMarshaller
}

trait FeedRoute
  extends HttpRoute {

  import AtomFeedConversion._

  private def offersService = context.actorSelection("/user/app/domain/LunchOfferService")
  private def providersService = context.actorSelection("/user/app/domain/LunchProviderService")

  def feedRoute =
    logRequest(context.system.name) {
      path("feed") {
        (get & parameters('location.?)) { optLocation =>
          complete {
            val providerReqMsg = optLocation match {
              case Some(location) => LPS.GetByLocation(location)
              case None => LPS.GetAll
            }
            val offerFuture = offersService.ask(LOS.GetAll).mapTo[LOS.MultiResult]
            val providerFuture = providersService.ask(providerReqMsg).mapTo[LPS.MultiResult]

            offerFuture.zip(providerFuture)
              .map {
                case (offerResMsg, providerResMsg) =>
                  val allOffers = offerResMsg.offers
                  val providers = providerResMsg.providers
                  val providerIDs = providers.map(_.id)
                  val offersForProviders = allOffers.filter(offer => providerIDs.contains(offer.provider))
                  val offersTilToday = offersForProviders.filter(_.day.compareTo(LocalDate.now()) <= 0)
                  createLunchOfferAtomFeed(offersTilToday, providers)
              }
            //  .recoverOnError(s"feed") // TODO: recover
          }
        }
      }
    }

  def createLunchOfferAtomFeed(offers: Seq[LunchOffer], providers: Set[LunchProvider]): ToResponseMarshallable =
    <feed xmlns="http://www.w3.org/2005/Atom">
      <id>urn:uuid:8bee5ffa-ca9b-44b4-979b-058e32d3a157</id>
      <title>Lunchbox - Mittagsangebote</title>
      <updated>{toISODateTimeString(todayMidnight)}</updated>
      {
        val offersGroupedAndSortedByDay = offers.groupBy(_.day).toList.sortWith((x,y) => x._1.compareTo(y._1) > 0)
        for ((day, offersForDay) <- offersGroupedAndSortedByDay) yield {
          <entry>
            <id>{"urn:date:" + day.toString}</id>
            <title>{toWeekdayDateString(day)}</title>
            <content type="html">{toHtmlContent(offersForDay, providers)}</content>
            <updated>{toISODateTimeString(todayMidnight)}</updated>
          </entry>
        }
      }
    </feed>

  def toHtmlContent(offers: Seq[LunchOffer], providers: Set[LunchProvider]) =
    for ((providerId, provOffers) <- offers.groupBy(_.provider)) yield {
      val providerName = providers.find(_.id == providerId).get.name
      <table border="0">
        <tr><th>{StringEscapeUtils.escapeHtml4(providerName)}</th><th></th></tr>
        {
          for (offer <- provOffers) yield {
            val moneyStr = "%d,%02d €".format(offer.price.getAmountMajorInt, offer.price.getMinorPart)
            <tr>
              <td>{StringEscapeUtils.escapeHtml4(offer.name)}</td>
              <td>{StringEscapeUtils.escapeHtml4(moneyStr)}</td>
            </tr>
          }
        }
      </table>
    }

  def toISODateTimeString(date:LocalDate):String = {
    val time = new LocalTime(0L, timeZoneBerlin)
    date.toLocalDateTime(time).toString
  }

  def toWeekdayDateString(date: LocalDate):String = {
    val weekdayStrings = Array("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag")
    weekdayStrings(date.getDayOfWeek-1) + ", " + date.toString("dd.MM.yyyy")
  }

  def todayMidnight = LocalDate.now(timeZoneBerlin)

  def timeZoneBerlin = DateTimeZone.forID("Europe/Berlin")
}
