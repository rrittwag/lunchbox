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
import util.PlayDateTimeHelper._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FeedController @Inject() (domain: DomainApi)(implicit exec: ExecutionContext) extends PlayFeedController {

  implicit val timeout = Timeout(5.seconds)

  def feed(optLocation: Option[String]): Action[AnyContent] = AtomFeedAction { implicit request =>
    optLocation match {
      case None | Some("") => Future.successful(NotFound)
      case Some(location) => feed(location)
    }
  }

  private def feed(location: String)(implicit request: RequestHeader): Future[Result] = {
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
  )(implicit request: RequestHeader) = {

    val providerIDs = providers.map(_.id)
    val offersForProviders = offers.filter(offer => providerIDs.contains(offer.provider))
    val offersTilToday = offersForProviders.filter(_.day <= LocalDate.now)

    val entries =
      for (
        (day, offersForDay) <- offersTilToday.groupBy(_.day).toList.sortWith(_._1 > _._1)
      ) yield AtomFeedEntry(
        new URI(s"urn:date:${day.toString}"),
        toWeekdayString(day),
        Author("Lunchbox"),
        Content(views.html.lunchday(offersForDay, providers)),
        toDateTime(day),
        toDateTime(day)
      )

    AtomFeed(
      new URI("urn:uuid:8bee5ffa-ca9b-44b4-979b-058e32d3a157"),
      s"Mittagstisch $location",
      new URL(routes.FeedController.feed(Some(location)).absoluteURL),
      toDateTime(LocalDate.now),
      entries
    )

  }

  private def toDateTime(date: LocalDate) = {
    def timeZoneBerlin = ZoneId.of("Europe/Berlin")
    date.atStartOfDay(timeZoneBerlin).toOffsetDateTime
  }

  private def toWeekdayString(date: LocalDate) =
    DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.GERMAN).format(date)

}
