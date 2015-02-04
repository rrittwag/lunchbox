package info.rori.lunchbox.server.akka.scala.service

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.server.{Directives, Route}
import akka.stream.scaladsl.ImplicitFlowMaterializer
import akka.util.Timeout
import info.rori.lunchbox.server.akka.scala.service.api.v1.ApiRouteV1
import info.rori.lunchbox.server.akka.scala.service.feed.FeedRoute
import info.rori.lunchbox.server.akka.scala.{ApplicationRoot, service}

import scala.concurrent.duration.DurationInt

object HttpService {
  val Name = "http-service"

  def props() = Props(new HttpService("localhost", 8080)(5.seconds))
}

class HttpService(host: String, port: Int)(implicit askTimeout: Timeout)
  extends Actor
  with ImplicitFlowMaterializer
  with MaintenanceRoute
  with ApiRouteV1
  with FeedRoute {

  import context.dispatcher

  log.info(s"Starting server at $host:$port")
  log.info(s"To shutdown, send http://$host:$port/shutdown")

  Http(context.system).bind(host, port).startHandlingWith(route)

  def route: Route = apiRouteV1 ~ feedRoute ~ maintenanceRoute

  override def receive = {
    case msg => log.warning("unhandled message in HttpService: " + msg)
  }
}


trait HttpRoute
  extends Actor
  with ActorLogging
  with Directives


trait MaintenanceRoute
  extends HttpRoute {

  import context.dispatcher

  def maintenanceRoute =
    path("shutdown") {
      get {
        complete {
          // TODO: Shutdown an ApplicationRoot schicken
          context.system.scheduler.scheduleOnce(500.millis, self, ApplicationRoot.Shutdown)
          log.info("Shutting down now ...")
          HttpResponse(entity = "Shutting down now ...")
        }
      }
    }
}
