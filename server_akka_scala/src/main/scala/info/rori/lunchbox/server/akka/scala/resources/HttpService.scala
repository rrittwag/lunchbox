package info.rori.lunchbox.server.akka.scala.resources

import akka.actor.{ActorLogging, Props, Actor}
import akka.http.Http
import akka.http.model.HttpResponse
import akka.http.server.{Directives, Route}
import akka.stream.scaladsl.ImplicitFlowMaterializer
import akka.util.Timeout
import info.rori.lunchbox.server.akka.scala.resources
import scala.concurrent.duration._
import akka.http.server.Directives._
import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import akka.http.Http
import akka.http.model.StatusCodes
import akka.http.server.{ Directives, Route }
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{ ImplicitFlowMaterializer, Source }
import akka.util.Timeout
import scala.concurrent.Future
import scala.concurrent.duration.{ DurationInt, FiniteDuration }

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

  import HttpService._
  import context.dispatcher

  log.info(s"Starting server at $host:$port")
  log.info(s"To shutdown, send GET request to http://$host:$port/shutdown")

  Http(context.system).bind(host, port).startHandlingWith(route)


  private def route: Route = resources.route ~ routeShutdown

  private def routeShutdown: Route =
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
