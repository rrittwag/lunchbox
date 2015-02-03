package info.rori.lunchbox.server.akka.scala

import akka.actor._
import akka.event.Logging
import info.rori.lunchbox.server.akka.scala.resources.HttpService

object Application extends App {
  val system = ActorSystem("lunchbox-server")
  val log = Logging.apply(system, getClass)

  system.actorOf(Reaper.props, Reaper.Name)

  system.awaitTermination()
}


object Reaper {
  val Name = "reaper"
  def props = Props(new Reaper)
}

class Reaper
  extends Actor
  with ActorLogging {

  override val supervisorStrategy = SupervisorStrategy.stoppingStrategy
  context.watch(createHttpService())

  override def receive: Receive = {
    case Terminated(actorRef) =>
      log.warning("Shutting down, because {} has been terminated!", actorRef.path)
      context.system.shutdown()
  }

  protected def createHttpService(): ActorRef = {
    context.actorOf(HttpService.props(), HttpService.Name)
  }
}