package services.api.v1

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import domain.models.LunchOffer
import domain.services.LunchOfferService
import domain.services.LunchOfferService._
import org.joda.time.LocalDate
import play.api.http.MimeTypes
import play.api.libs.json.{Json, Writes}
import play.api.mvc.{Action, AnyContent, Controller}
import util.PlayDateTimeHelper._
import util.PlayMoneyHelper._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait LunchOfferJsonFormats {
  implicit val writesJson = new Writes[LunchOffer] {
    def writes(o: LunchOffer) = Json.obj(
      "id" -> o.id,
      "name" -> o.name,
      "day" -> o.day,
      "price" -> o.price,
      "provider" -> o.provider
    )
  }
}

@Singleton
class LunchOfferController @Inject()(system: ActorSystem)(implicit exec: ExecutionContext) extends Controller
  with LunchOfferJsonFormats {

  val TypeJson = s"${MimeTypes.JSON};charset=utf-8"

  val domainService = system.actorOf(LunchOfferService.props, "lunchOffer-actor")
  implicit val timeout = Timeout(5.seconds)

  def list(optDayString: Option[String]): Action[AnyContent] = Action.async {
    parseLocalDate(optDayString) match {
      case Success(optDay) => list(optDay)
      case Failure(_) => Future.successful(BadRequest(Json.obj("status" -> "400", "message" -> s"${optDayString.getOrElse("")} is no valid date")).as(TypeJson))
    }
  }

  private def list(optDay: Option[LocalDate]) = {
    val domainReqMsg = optDay.map(GetByDay).getOrElse(GetAll)

    domainService.ask(domainReqMsg).mapTo[MultiResult]
      .map(resultMsg => Ok(Json.toJson(resultMsg.offers)).as(TypeJson))
  }

  def find(id: Int) = Action.async {
    domainService.ask(GetById(id)).mapTo[SingleResult]
      .map {
        _.offer match {
          case Some(provider) => Ok(Json.toJson(provider)).as(TypeJson)
          case None => NotFound(Json.obj("status" -> "404", "message" -> s"resource with id $id not found")).as(TypeJson)
        }
      }
  }
}
