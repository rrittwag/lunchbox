package info.rori.lunchbox.server.akka.scala

import akka.actor._
import akka.event.Logging
import info.rori.lunchbox.server.akka.scala.domain.DomainRoot
import info.rori.lunchbox.server.akka.scala.service.ServiceRoot

object Application extends App {
  val system = ActorSystem("lunchbox-server")
  val log = Logging.apply(system, getClass)

  system.actorOf(ApplicationRoot.props, ApplicationRoot.Name)

  system.awaitTermination()
}


/**
 * Erzeugt und überwacht DomainRoot & ServiceRoot
 */
object ApplicationRoot {
  val Name = "app"

  def props = Props(new ApplicationRoot)

  case object Shutdown
}

class ApplicationRoot
  extends Actor
  with ActorLogging {

  import ApplicationRoot._

  val domainRoot = context.actorOf(DomainRoot.props, DomainRoot.Name)
  val serviceRoot = context.actorOf(ServiceRoot.props, ServiceRoot.Name)

  override def receive = {
    case Shutdown => context.system.shutdown()
    case msg => log.warning("unhandled message in ApplicationRoot: " + msg)
  }
}