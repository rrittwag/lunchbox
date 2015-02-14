package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.http.marshalling.ToResponseMarshallable
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService._
import info.rori.lunchbox.server.akka.scala.service.HttpRoute
import spray.json._
import akka.http.marshallers.sprayjson.SprayJsonSupport

import scala.concurrent.{ExecutionContextExecutor, Future}

trait ApiModel

/**
 * Model für LunchProvider in API v1.
 */
private[v1] case class LunchProvider_ApiV1(id: Int,
                                           name: String,
                                           location: String) extends ApiModel

private[v1] case object LunchProvider_ApiV1 {
  def apply(p: LunchProvider): LunchProvider_ApiV1 = apply(id = p.id, name = p.name, location = p.location)
}

private[v1] object LunchProviderConverter extends DefaultJsonProtocol {
  implicit val model2apiModel: Function[LunchProvider, LunchProvider_ApiV1] = LunchProvider_ApiV1.apply
  implicit val json2apiModel = jsonFormat3(LunchProvider_ApiV1.apply)
}


/**
 * HTTP-Route für LunchProvider in API v1.
 */
trait LunchProviderRoute_ApiV1
  extends HttpRoute {

  import info.rori.lunchbox.server.akka.scala.service.api.v1.LunchProviderConverter._

  private def domainService = context.actorSelection("/user/app/domain/LunchProviderService")

  val lunchProviderRoute =
    path("lunchProvider") {
      (get & parameters('location.?)) { optLocation =>
        complete {
          val domainReqMsg = optLocation match {
            case Some(location) => GetByLocation(location)
            case None => GetAll
          }
          domainService.ask(domainReqMsg).mapTo[MultiResult]
            .mapSeqToJsonResponse(domainResMsg => domainResMsg.providers)
            .recoverOnError(s"api/v1/lunchProvider?location=$optLocation")
        }
      }
    } ~
    path("lunchProvider" / IntNumber) { id =>
      get {
        complete {
          domainService.ask(GetById(id)).mapTo[SingleResult]
            .mapOptionToJsonResponse(domainResMsg => domainResMsg.provider)
            .recoverOnError(s"api/v1/lunchProvider/$id")
        }
      } /*~
      post {
        entity(as[LunchProvider_ApiV1]) {
          obj => complete(obj.toString)
        }
      }*/
      }

  /**
   *
   * @param resultFuture domain result as future
   * @tparam R type of resulting domain message
   */
  implicit class DomainResult2HttpJsonResponse[R <: Any, M <: LunchProvider, MA <: LunchProvider_ApiV1]
  (resultFuture: Future[R])
  (implicit val model2apiModel: M => MA, implicit val jsonFormatter: spray.json.RootJsonFormat[MA], implicit val executor: ExecutionContextExecutor) {

    implicit val printer: spray.json.JsonPrinter = CompactPrinter // remove line, if you want to print pretty JSON
    implicit val marshaller = SprayJsonSupport.sprayJsValueMarshaller[MA]

    def mapSeqToJsonResponse(f: R => Seq[M]) = resultFuture.map(msg => toResponse(f(msg)))

    def mapOptionToJsonResponse(f: R => Option[M]) = resultFuture.map(msg => toResponse(f(msg)))

    private def toResponse(providers: Seq[M]): ToResponseMarshallable = providers.map(p => model2apiModel(p)).toJson

    private def toResponse(optProvider: Option[M]): ToResponseMarshallable = optProvider match {
      case Some(provider) => model2apiModel(provider).toJson
      case None => HttpRoute.NotFound
    }
  }

}
