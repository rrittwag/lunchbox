package info.rori.lunchbox.server.akka.scala

import akka.actor._
import akka.event.Logging
import info.rori.lunchbox.server.akka.scala.domain.DomainModule
import info.rori.lunchbox.server.akka.scala.service.ServiceModule

object Application extends App {
  val system = ActorSystem("lunchbox-server")
  val log = Logging(system, getClass)

  system.actorOf(ApplicationModule.props, ApplicationModule.Name)

  system.awaitTermination()
}


/**
 * Erzeugt und überwacht DomainRoot & ServiceRoot
 */
object ApplicationModule {
  val Name = "app"

  def props = Props(new ApplicationModule)

  case object Shutdown
}

class ApplicationModule
  extends Actor
  with ActorLogging {

  import ApplicationModule._

  val domainRoot = context.actorOf(DomainModule.props, DomainModule.Name)
  val serviceRoot = context.actorOf(ServiceModule.props, ServiceModule.Name)

  override def receive = {
    case Shutdown => context.system.shutdown()
    case msg => log.warning("unhandled message in ApplicationRoot: " + msg)
  }
}