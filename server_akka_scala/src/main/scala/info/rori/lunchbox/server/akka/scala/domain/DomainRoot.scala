package info.rori.lunchbox.server.akka.scala.domain

import akka.actor._

/**
 * Erstellt und überwacht die Domain-Services.
 */
object DomainRoot {
  val Name = "domain"

  def props = Props(new DomainRoot)
}

class DomainRoot
  extends Actor
  with ActorLogging {

  override def receive: Receive = {
    case msg => log.warning("unhandled message in DomainRoot: " + msg)
  }
}
