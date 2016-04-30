package services.feed

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import domain.services.LunchProviderService
import util.PlayFeedController

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class FeedController @Inject()(system: ActorSystem)
                              (implicit exec: ExecutionContext) extends PlayFeedController {

//  val domainService = system.actorOf(LunchProviderService.props, "lunchProvider-actor")
  implicit val timeout = Timeout(5.seconds)

  def feed(optLocation: Option[String]) = AtomFeedAction {
    optLocation match {
      case None | Some("") => Future.successful(NotFound)
      case Some(location) => Future.successful(Ok(views.xml.atom(location)))
    }
  }

}
