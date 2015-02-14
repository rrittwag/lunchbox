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

  /**
   * Konvertiert zwischen Domain Model & API Model
   * @param model2apiModel Methode zum Konvertieren von Domain Model zu API Model
   * @tparam DM Typ des Domain Models
   * @tparam AM Typ des API Models
   */
  class DomainModelConverter[DM, AM](model2apiModel: Function[DM, AM]) {
    def toApiModel(modelEntity: DM) = model2apiModel(modelEntity)
  }

  implicit val domainModelConverter = new DomainModelConverter(LunchProvider_ApiV1.apply)
  implicit val json2apiModel = jsonFormat3(LunchProvider_ApiV1.apply)
}


/**
 * HTTP-Route für LunchProvider in API v1.
 */
trait LunchProviderRoute_ApiV1
  extends HttpRoute {

  import LunchProviderConverter._

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
  implicit class DomainResult2HttpJsonResponse[R <: Any, DM <: Any, AM <: ApiModel]
  (resultFuture: Future[R])
  (implicit val model2apiModel: DomainModelConverter[DM, AM], implicit val jsonFormatter: spray.json.RootJsonFormat[AM], implicit val executor: ExecutionContextExecutor) {

    implicit val printer: spray.json.JsonPrinter = CompactPrinter // remove line, if you want to print pretty JSON
    implicit val marshaller = SprayJsonSupport.sprayJsValueMarshaller[AM]

    def mapSeqToJsonResponse(f: R => Seq[DM]) = resultFuture.map(msg => toResponse(f(msg)))

    def mapOptionToJsonResponse(f: R => Option[DM]) = resultFuture.map(msg => toResponse(f(msg)))

    private def toResponse(providers: Seq[DM]): ToResponseMarshallable = providers.map(p => model2apiModel.toApiModel(p)).toJson

    private def toResponse(optProvider: Option[DM]): ToResponseMarshallable = optProvider match {
      case Some(provider) => model2apiModel.toApiModel(provider).toJson
      case None => HttpRoute.NotFound
    }
  }

}
