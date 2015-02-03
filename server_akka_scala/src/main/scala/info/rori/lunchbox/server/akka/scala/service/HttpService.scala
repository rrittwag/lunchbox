package info.rori.lunchbox.server.akka.scala.service

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.server.{Directives, Route}
import akka.stream.scaladsl.ImplicitFlowMaterializer
import akka.util.Timeout
import info.rori.lunchbox.server.akka.scala.service

import scala.concurrent.duration.DurationInt

object HttpService {
  val Name = "http-service"

  private case object Shutdown

  def props() = Props(new HttpService("localhost", 8080)(5.seconds))
}

class HttpService(host: String, port: Int)(implicit askTimeout: Timeout)
  extends Actor
  with ActorLogging
  with Directives
  with ImplicitFlowMaterializer {

  import context.dispatcher
  import info.rori.lunchbox.server.akka.scala.service.HttpService._

  log.info(s"Starting server at $host:$port")
  log.info(s"To shutdown, send http://$host:$port/shutdown")

  Http(context.system).bind(host, port).startHandlingWith(route)


  private def route: Route = service.httpRoute ~ shutdownRoute

  private def shutdownRoute: Route =
    path("shutdown") {
      get {
        complete {
          context.system.scheduler.scheduleOnce(500.millis, self, Shutdown)
          log.info("Shutting down now ...")
          HttpResponse(entity = "Shutting down now ...")
        }
      }
    }

  override def receive: Receive = {
    case Shutdown => context.system.shutdown()
  }

}
