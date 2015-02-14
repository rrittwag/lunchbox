package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.http.marshallers.sprayjson.SprayJsonSupport._
import akka.http.marshalling.ToResponseMarshallable
import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchOffer
import info.rori.lunchbox.server.akka.scala.domain.service.LunchOfferService._
import info.rori.lunchbox.server.akka.scala.service.HttpRoute
import org.joda.time.LocalDate
import spray.json._

import scala.concurrent.Future


/**
 * Model für LunchOffer in API v1.
 */
private[v1] case class LunchOffer_ApiV1(id: Int,
                                        name: String,
                                        day: String, // ISO 8601 format 'YYYY-MM-DD'
                                        price: Int, // in EURO cent
                                        provider: Int)

private[v1] case object LunchOffer_ApiV1 {
  def apply(o: LunchOffer): LunchOffer_ApiV1 =
    apply(id = o.id, name = o.name, day = o.day.toString, price = o.price.getAmountMinorInt, provider = o.provider)
}

private[v1] object LunchOfferJsonSupport extends DefaultJsonProtocol {
  implicit val LunchOfferFormat = jsonFormat5(LunchOffer_ApiV1.apply)
  implicit val printer: spray.json.JsonPrinter = CompactPrinter // remove line, if you want to print pretty JSON
}


/**
 * HTTP-Route für LunchOffer in API v1.
 */
trait LunchOfferRoute_ApiV1
  extends HttpRoute {

  import LunchOfferJsonSupport._

  private def domainService = context.actorSelection("/user/app/domain/LunchOfferService")

  val lunchOfferRoute =
    path("lunchOffer") {
      (get & parameters('day.?)) { optDayString =>
        validate(optDayString.isValidLocalDate, s"${optDayString.get} is no valid date") {
          complete {
            val domainReqMsg = optDayString match {
              case Some(dayString) => GetByDay(LocalDate.parse(dayString))
              case None => GetAll
            }
            domainService.ask(domainReqMsg).mapTo[MultiResult]
              .mapSeqToResponse(domainResMsg => domainResMsg.offers)
              .recoverOnError(s"api/v1/lunchOffer?day=$optDayString")
          }
        }
      }
    } ~
    path("lunchOffer" / IntNumber) { id =>
      get {
        complete {
          domainService.ask(GetById(id)).mapTo[SingleResult]
            .mapOptionToResponse(domainResMsg => domainResMsg.offer)
            .recoverOnError(s"api/v1/lunchOffer/$id")
        }
      } /*~
      post {
        entity(as[LunchOffer_ApiV1]) {
          obj => complete(obj.toString)
        }
      }*/
    }

  /**
   *
   * @param resultFuture domain result as future
   * @tparam M type of domain message
   */
  implicit class DomainResult2HttpResponseBla[M <: Object](resultFuture: Future[M]) {
    def mapSeqToResponse(f: M => Seq[LunchOffer]) = resultFuture.map(msg => toResponse(f(msg)))

    def mapOptionToResponse(f: M => Option[LunchOffer]) = resultFuture.map(msg => toResponse(f(msg)))

    private def toResponse(offers: Seq[LunchOffer]): ToResponseMarshallable = offers.map(o => LunchOffer_ApiV1(o)).toJson

    private def toResponse(optOffer: Option[LunchOffer]): ToResponseMarshallable = optOffer match {
      case Some(offer) => LunchOffer_ApiV1(offer).toJson
      case None => HttpRoute.NotFound
    }
  }

}
