package controllers.feed

import java.net._
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.{Inject, Singleton}

import akka.pattern.ask
import akka.util.Timeout
import domain.DomainApi
import domain.models.{LunchOffer, LunchProvider}
import domain.services.{LunchOfferService => OfferRepo, LunchProviderService => ProviderRepo}
import java.time.{LocalDate, ZoneId}

import play.api.mvc._
import util.feed._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FeedController @Inject() (domain: DomainApi)(implicit exec: ExecutionContext) extends PlayFeedController {

  implicit val timeout = Timeout(5.seconds)

  def feed(optLocation: Option[String]): Action[AnyContent] = AtomFeedAction {
    optLocation match {
      case None | Some("") => Future.successful(NotFound)
      case Some(location) => feed(location)
    }
  }

  private def feed(location: String): Future[Result] = {
    val offerFuture = domain.lunchOfferService.ask(OfferRepo.GetAll).mapTo[OfferRepo.MultiResult]
    val providerFuture = domain.lunchProviderService.ask(ProviderRepo.GetByLocation(location)).mapTo[ProviderRepo.MultiResult]

    for (
      offerResMsg <- offerFuture;
      providerResMsg <- providerFuture
    ) yield {
      val atomFeed = createAtomModel(location, offerResMsg.offers, providerResMsg.providers)
      Ok(views.xml.atomfeed(atomFeed))
    }
  }

  private def createAtomModel(
    location: String,
    offers: Seq[LunchOffer],
    providers: Set[LunchProvider]
  ) = {

    val providerIDs = providers.map(_.id)
    val offersForProviders = offers.filter(offer => providerIDs.contains(offer.provider))
    val offersTilToday = offersForProviders.filter(_.day.compareTo(LocalDate.now) <= 0)

    val entries =
      for ((day, offersForDay) <- offersGroupedAndSortedByDay(offersTilToday)) yield AtomFeedEntry(
        new URI(s"urn:date:${day.toString}"),
        DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.GERMAN).format(day),
        Author("Lunchbox"),
        Content(views.html.lunchday(offersForDay, providers)),
        toISODateTime(day),
        toISODateTime(day)
      )

    AtomFeed(
      new URI("urn:uuid:8bee5ffa-ca9b-44b4-979b-058e32d3a157"),
      s"Mittagstisch $location",
      new URL(s"http://lunchbox.rori.info/feed?location=$location"),
      toISODateTime(LocalDate.now),
      entries
    )

  }

  private def offersGroupedAndSortedByDay(offers: Seq[LunchOffer]) =
    offers
      .groupBy(_.day).toList
      .sortWith((x, y) => x._1.compareTo(y._1) > 0)

  private def toISODateTime(date: LocalDate) = {
    def timeZoneBerlin = ZoneId.of("Europe/Berlin")
    date.atStartOfDay(timeZoneBerlin).toOffsetDateTime
  }

}
