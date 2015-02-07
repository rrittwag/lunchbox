package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.http.model.{StatusCodes, HttpResponse}
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService.{SingleResult, MultiResult}
import info.rori.lunchbox.server.akka.scala.service.HttpRoute

import scala.util.{Success, Failure}


case object LunchProvider_ApiV1 {
  def apply(prov: LunchProvider): LunchProvider_ApiV1 = apply(id = prov.id, name = prov.name, location = prov.location)
}

case class LunchProvider_ApiV1(id: Int, name: String, location: String)


trait LunchProviderRoute_ApiV1
  extends HttpRoute {

  private def lunchboxProviderService = context.actorSelection("/user/app/domain/LunchProviderService")

  val lunchProviderRoute =
    path("lunchProvider") {
      get {
        val response = lunchboxProviderService
          .ask(LunchProviderService.GetAll)
          .mapTo[MultiResult]
        onComplete(response) {
          case Success(msg) => complete(toHttpResponse(msg.providers))
          case Failure(exc) => complete(toHttpResponse(exc))
        }
      }
    } ~
    path("lunchProvider" / IntNumber) { id =>
      get {
        val response = lunchboxProviderService
          .ask(LunchProviderService.GetById(id))
          .mapTo[SingleResult]
        onComplete(response) {
          case Success(msg) => complete(toHttpResponse(msg.provider))
          case Failure(exc) => complete(toHttpResponse(exc))
        }
      }
    }

  def toHttpResponse(value: Option[LunchProvider]): HttpResponse = value match {
    case Some(provider) => HttpResponse(entity = LunchProvider_ApiV1(provider).toString)
    case None => HttpResponse(StatusCodes.NotFound)
  }

  def toHttpResponse(providers: Seq[LunchProvider]): HttpResponse = HttpResponse(entity = providers.map(p => LunchProvider_ApiV1(p)).toString())

  def toHttpResponse(t: Throwable): HttpResponse = HttpResponse(status = StatusCodes.InternalServerError, entity = t.toString)

}