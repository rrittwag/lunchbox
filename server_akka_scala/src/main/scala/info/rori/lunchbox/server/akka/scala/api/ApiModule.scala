package info.rori.lunchbox.server.akka.scala.api

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Erstellt und überwacht die Services, die Daten nach außen bereitstellen.
 */
object ApiModule {
  val Name = "service"

  def props = Props(new ApiModule)
}

class ApiModule
  extends Actor
  with ActorLogging {

  override val supervisorStrategy = SupervisorStrategy.stoppingStrategy

  val config = ConfigFactory.load()

  private val host = config.getString("http.interface")
  private val port = config.getInt("http.port")
  val httpServiceRef = context.actorOf(HttpService.props(host, port), HttpService.Name)
  context.watch(httpServiceRef)

  override def receive: Receive = {
    case Terminated(actorRef) =>
      log.warning("Shutting down, because {} has been terminated!", actorRef.path)
      context.system.shutdown()
  }
}
