package info.rori.lunchbox.server.akka.scala.api.v1

import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider
import info.rori.lunchbox.server.akka.scala.domain.service.LunchProviderService._
import info.rori.lunchbox.server.akka.scala.api.{HttpJsonConversions, HttpRoute}

/**
 * Model für LunchProvider in API v1.
 */
private[v1] case class LunchProvider_ApiV1(id: Int,
                                           name: String,
                                           location: String)

private[v1] case object LunchProvider_ApiV1 {
  def apply(p: LunchProvider): LunchProvider_ApiV1 = apply(id = p.id, name = p.name, location = p.location)
}

private[v1] object LunchProviderConversions extends HttpJsonConversions {
  implicit val json2apiModel = jsonFormat3(LunchProvider_ApiV1.apply)
  implicit val domainModelConverter = new ModelConverter(LunchProvider_ApiV1.apply)
}


/**
 * HTTP-Route für LunchProvider in API v1.
 */
trait LunchProviderRoute_ApiV1
  extends HttpRoute {

  import LunchProviderConversions._

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
}
