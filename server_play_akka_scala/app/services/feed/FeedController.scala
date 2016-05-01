package services.feed

import javax.inject.{Inject, Singleton}

import akka.pattern.ask
import akka.util.Timeout
import domain.DomainApi
import domain.services.LunchOfferService.{GetAll, MultiResult}
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
    domain.lunchOfferService.ask(GetAll).mapTo[MultiResult]
      .map(resultMsg => Ok(views.xml.atom(location, resultMsg.offers)))
  }
}
