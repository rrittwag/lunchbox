package services.feed

import javax.inject.{Inject, Singleton}

import akka.pattern.ask
import akka.util.Timeout
import domain.DomainApi
import domain.services.{LunchOfferService => OfferRepo, LunchProviderService => ProviderRepo}
import org.joda.time.LocalDate
import play.api.mvc._
import util.PlayFeedController

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FeedController @Inject()(domain: DomainApi)
                              (implicit exec: ExecutionContext) extends PlayFeedController {

  implicit val timeout = Timeout(5.seconds)

  def feed(optLocation: Option[String]): Action[AnyContent] = AtomFeedAction {
    optLocation match {
      case None | Some("") => Future.successful(NotFound)
      case Some(location) => feed(location)
    }
  }

  private def feed(location: String) = {
    val offerFuture = domain.lunchOfferService.ask(OfferRepo.GetAll).mapTo[OfferRepo.MultiResult]
    val providerFuture = domain.lunchProviderService.ask(ProviderRepo.GetByLocation(location)).mapTo[ProviderRepo.MultiResult]

    offerFuture.zip(providerFuture)
      .map {
        case (offerResMsg, providerResMsg) =>
          val allOffers = offerResMsg.offers
          val providers = providerResMsg.providers
          val providerIDs = providers.map(_.id)
          val offersForProviders = allOffers.filter(offer => providerIDs.contains(offer.provider))
          val offersTilToday = offersForProviders.filter(_.day.compareTo(LocalDate.now) <= 0)
          Ok(views.xml.atomfeed(location, offersTilToday, providers))
      }
    //  .recoverOnError(s"feed") // TODO: recover
  }
}
