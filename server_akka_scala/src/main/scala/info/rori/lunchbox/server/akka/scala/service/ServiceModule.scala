package info.rori.lunchbox.server.akka.scala.service

import akka.actor._

/**
 * Erstellt und überwacht die Services, die Daten nach außen bereitstellen.
 */
object ServiceModule {
  val Name = "service"

  def props = Props(new ServiceModule)
}

class ServiceModule
  extends Actor
  with ActorLogging {

  override val supervisorStrategy = SupervisorStrategy.stoppingStrategy

  val actorRef = context.actorOf(HttpService.props(), HttpService.Name)
  context.watch(actorRef)

  override def receive: Receive = {
    case Terminated(actorRef) =>
      log.warning("Shutting down, because {} has been terminated!", actorRef.path)
      context.system.shutdown()
  }
}
