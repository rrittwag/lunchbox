package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.pattern.ask
import info.rori.lunchbox.server.akka.scala.domain.model.LunchOffer
import info.rori.lunchbox.server.akka.scala.domain.service.LunchOfferService._
import info.rori.lunchbox.server.akka.scala.service.{HttpConversions, HttpRoute}
import org.joda.time.LocalDate


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

private[v1] object LunchOfferConversions extends HttpConversions {
  implicit val json2apiModel = jsonFormat5(LunchOffer_ApiV1.apply)
  implicit val domainModelConverter = new DomainModelConverter(LunchOffer_ApiV1.apply)
}


/**
 * HTTP-Route für LunchOffer in API v1.
 */
trait LunchOfferRoute_ApiV1
  extends HttpRoute {

  import LunchOfferConversions._

  private def domainService = context.actorSelection("/user/app/domain/LunchOfferService")

  val lunchOfferRoute =
    path("lunchOffer") {
      (get & parameters('day.?)) { optDayString =>
        validate(optDayString.isValidLocalDate, s"${optDayString.getOrElse("")} is no valid date") {
          complete {
            val domainReqMsg = optDayString match {
              case Some(dayString) => GetByDay(LocalDate.parse(dayString))
              case None => GetAll
            }
            domainService.ask(domainReqMsg).mapTo[MultiResult]
              .mapSeqToJsonResponse(domainResMsg => domainResMsg.offers)
              .recoverOnError(s"api/v1/lunchOffer?day=$optDayString")
          }
        }
      }
    } ~
    path("lunchOffer" / IntNumber) { id =>
      get {
        complete {
          domainService.ask(GetById(id)).mapTo[SingleResult]
            .mapOptionToJsonResponse(domainResMsg => domainResMsg.offer)
            .recoverOnError(s"api/v1/lunchOffer/$id")
        }
      } /*~
      post {
        entity(as[LunchOffer_ApiV1]) {
          obj => complete(obj.toString)
        }
      }*/
    }
}
