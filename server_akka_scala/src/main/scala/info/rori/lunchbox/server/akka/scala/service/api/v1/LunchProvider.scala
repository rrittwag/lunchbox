package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.ToResponseMarshallable
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService._
import info.rori.lunchbox.server.akka.scala.service.HttpRoute
import spray.json._


/**
 * Model für LunchProvider in API v1.
 */
private[v1] case class LunchProvider_ApiV1(id: Int, name: String, location: String)

private[v1] case object LunchProvider_ApiV1 {
  def apply(p: LunchProvider): LunchProvider_ApiV1 = apply(id = p.id, name = p.name, location = p.location)
}

private[v1] object LunchProviderJsonSupport extends DefaultJsonProtocol {
  implicit val lunchProviderFormat = jsonFormat3(LunchProvider_ApiV1.apply)
  implicit val printer : spray.json.JsonPrinter = CompactPrinter // remove line, if you want to print pretty JSON
}


/**
 * HTTP-Route für LunchProvider in API v1.
 */
trait LunchProviderRoute_ApiV1
  extends HttpRoute {

  import info.rori.lunchbox.server.akka.scala.service.api.v1.LunchProviderJsonSupport._

  private def domainService = context.actorSelection("/user/app/domain/LunchProviderService")

  val lunchProviderRoute =
    path("lunchProvider") {
      (get & parameters('location.?)) { optLocation =>
        complete {
          val domainMsg = optLocation match {
            case Some(location) => GetByLocation(location)
            case None => GetAll
          }
          domainService.ask(domainMsg).mapTo[MultiResult]
            .map(msg => toResponse(msg.providers))
            .recoverOnError(s"api/v1/lunchProvider?$optLocation")
        }
      }
    } ~
    path("lunchProvider" / IntNumber) { id =>
      get {
        complete {
          domainService.ask(GetById(id)).mapTo[SingleResult]
            .map(msg => toResponse(msg.provider))
            .recoverOnError(s"api/v1/lunchProvider/$id")
        }
      } /*~
      post {
        entity(as[LunchProvider_ApiV1]) {
          obj => complete(obj.toString)
        }
      }*/

    }

  def toResponse(value: Option[LunchProvider]): ToResponseMarshallable = value match {
    case Some(provider) => LunchProvider_ApiV1(provider)
    case None => NotFound
  }

  def toResponse(providers: Seq[LunchProvider]): ToResponseMarshallable = providers.map(p => LunchProvider_ApiV1(p)).toJson

}
