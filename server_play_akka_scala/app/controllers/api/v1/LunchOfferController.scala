package controllers.api.v1

import javax.inject.{Inject, Singleton}

import akka.pattern.ask
import akka.util.Timeout
import domain.DomainApi
import domain.models.LunchOffer
import domain.services.LunchOfferService._
import java.time.LocalDate
import play.api.libs.json.{Json, Writes}
import play.api.mvc._
import util.PlayDateTimeHelper._
import util.PlayMoneyHelper._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait LunchOfferJsonFormats {
  implicit val writesJson = new Writes[LunchOffer] {
    def writes(o: LunchOffer) = Json.obj(
      "id" -> o.id,
      "name" -> o.name,
      "day" -> o.day,
      "price" -> o.price,
      "provider" -> o.provider)
  }
}

@Singleton
class LunchOfferController @Inject() (domain: DomainApi)(implicit exec: ExecutionContext) extends InjectedController with LunchOfferJsonFormats {

  implicit val timeout = Timeout(5.seconds)

  def list(optDay: Option[LocalDate]) = Action.async {
    val domainReqMsg = optDay.map(GetByDay).getOrElse(GetAll)

    domain.lunchOfferService.ask(domainReqMsg).mapTo[MultiResult]
      .map(resultMsg => Ok(Json.toJson(resultMsg.offers)))
  }

  def find(id: Int) = Action.async {
    domain.lunchOfferService.ask(GetById(id)).mapTo[SingleResult]
      .map { resultMsg =>
        resultMsg.offer match {
          case Some(provider) => Ok(Json.toJson(provider))
          case None => NotFound(Json.obj("status" -> "404", "message" -> s"resource with id $id not found"))
        }
      }
  }
}
