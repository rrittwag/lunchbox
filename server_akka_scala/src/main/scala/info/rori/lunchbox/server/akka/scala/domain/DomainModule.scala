package info.rori.lunchbox.server.akka.scala.domain

import akka.actor._
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService

/**
 * Erstellt und überwacht die Domain-Services.
 */
object DomainModule {
  val Name = "domain"

  def props = Props(new DomainModule)
}

class DomainModule
  extends Actor
  with ActorLogging {

  val lunchProviderService = context.actorOf(LunchProviderService.props, LunchProviderService.Name)

  override def receive: Receive = {
    case msg => log.warning("unhandled message in DomainRoot: " + msg)
  }
}
