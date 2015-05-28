package info.rori.lunchbox.server.akka.scala.service.feed

import akka.http.scaladsl.marshalling._
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.{LunchProvider, LunchOffer}
import info.rori.lunchbox.server.akka.scala.domain.service.{LunchOfferService => LOS}
import info.rori.lunchbox.server.akka.scala.domain.service.{LunchProviderService => LPS}
import info.rori.lunchbox.server.akka.scala.service.{HttpRoute, HttpXmlConversions}
import org.joda.time.{DateTimeZone, LocalDate}

import scala.concurrent.ExecutionContext
import scala.xml.Node

object AtomFeedConversion extends HttpXmlConversions {
  implicit def defaultNodeSeqMarshaller(implicit ec: ExecutionContext): ToEntityMarshaller[Node] = atomFeedMarshaller
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
                  val offersTilToday = offersForProviders.filter(_.day.compareTo(LocalDate.now) <= 0)
                  createLunchOfferAtomFeed(offersTilToday, providers, optLocation)
              }
            //  .recoverOnError(s"feed") // TODO: recover
          }
        }
      }
    }

  def createLunchOfferAtomFeed(offers: Seq[LunchOffer], providers: Set[LunchProvider], optLocation: Option[String]): ToResponseMarshallable =
    <feed xmlns="http://www.w3.org/2005/Atom">
      <id>urn:uuid:8bee5ffa-ca9b-44b4-979b-058e32d3a157</id>
      <title>{ optLocation.foldLeft("Mittagstisch")(_ + " " + _) }</title>
      <link rel="self" href={ optLocation.foldLeft("http://lunchbox.rori.info/feed")(_ + "?location=" + _) /* TODO: in Config schieben */ }/>
      <updated>{toISODateTimeString(LocalDate.now)}</updated>
      {
        val offersGroupedAndSortedByDay = offers.groupBy(_.day).toList.sortWith((x,y) => x._1.compareTo(y._1) > 0)
        for ((day, offersForDay) <- offersGroupedAndSortedByDay) yield {
          <entry>
            <id>{"urn:date:" + day.toString}</id>
            <title>{toWeekdayDateString(day)}</title>
            <author>
              <name>Lunchbox</name>
            </author>
            <content type="html">{
              val offersAsHtml = scala.xml.Utility.trim(toHtml(offersForDay, providers))
              scala.xml.Unparsed(cdata(offersAsHtml)) }
            </content>
            <published>{toISODateTimeString(day)}</published>
            <updated>{toISODateTimeString(day)}</updated>
          </entry>
        }
      }
    </feed>

  def toHtml(offers: Seq[LunchOffer], providers: Set[LunchProvider]) = {
    <div>
      <style type="text/css">
        { "table { border:0px; } td { vertical-align: top; } span { white-space: nowrap; } tr { padding-bottom: 1.5em; }" }
      </style>
      {
      for ((providerId, provOffers) <- offers.groupBy(_.provider)) yield {
        val providerName = providers.find(_.id == providerId).get.name
        <table>
          <tr>
            <th>{providerName}</th>
            <th></th>
          </tr>
          {
          for (offer <- provOffers) yield {
            val moneyStr = "%d,%02d €".format(offer.price.getAmountMajorInt, offer.price.getMinorPart)
            <tr>
              <td>{offer.name}</td>
              <td><span>{moneyStr}</span></td>
            </tr>
          }
          }
        </table>
      }
      }
    </div>
  }

  def toISODateTimeString(date: LocalDate): String = date.toDateTimeAtStartOfDay(timeZoneBerlin).toString

  def toWeekdayDateString(date: LocalDate): String = {
    val weekdayStrings = Array("Montag", "Dienstag", "Mittwoch", "Donnerstag", "Freitag", "Samstag", "Sonntag")
    weekdayStrings(date.getDayOfWeek-1) + ", " + date.toString("dd.MM.yyyy")
  }

  def cdata(data: Any): String = s"<![CDATA[$data]]>"

  def timeZoneBerlin = DateTimeZone.forID("Europe/Berlin")
}
