package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.actor.ActorSelection
import akka.http.model.{StatusCodes, HttpResponse}
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService
import info.rori.lunchbox.server.akka.scala.service.HttpRoute

import scala.util.{Success, Failure}


case class ApiV1LunchProvider(id: Int, name: String, location: String)


trait LunchProviderRoute
  extends HttpRoute {

  private def lunchboxProviderService = context.actorSelection("/user/app/domain/LunchProviderService")

  val lunchProviderRoute =
    path("lunchProvider") {
      get {
        val response = lunchboxProviderService
          .ask(LunchProviderService.GetAll)
          .mapTo[Seq[LunchProvider]]
        onComplete(response) {
          case Success(value) => complete(HttpResponse(entity = value.toString))
          case Failure(exc) => complete(toHttpResponse(exc))
        }
      }
    } ~
    path("lunchProvider" / IntNumber) { id =>
      get {
        val response = lunchboxProviderService
          .ask(LunchProviderService.GetById(id))
          .mapTo[Option[LunchProvider]]
        onComplete(response) {
          case Success(value) => complete(toHttpResponse(value))
          case Failure(exc) => complete(toHttpResponse(exc))
        }
      }
    }

  def toHttpResponse(value: Option[LunchProvider]): HttpResponse = value match {
    case Some(provider) => HttpResponse(entity = provider.toString)
    case None => HttpResponse(StatusCodes.NotFound)
  }

  def toHttpResponse(t: Throwable): HttpResponse = HttpResponse(status = StatusCodes.InternalServerError, entity = "error: " + t.toString)

}