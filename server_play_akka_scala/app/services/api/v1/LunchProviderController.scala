package services.api.v1

import javax.inject.{Inject, Singleton}

import akka.actor.ActorSystem
import akka.util.Timeout
import domain.models.LunchProvider
import domain.services.LunchProviderService
import domain.services.LunchProviderService._
import play.api.http.MimeTypes
import play.api.libs.json._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import akka.pattern.ask


trait LunchProviderJsonFormats {
  implicit val writesJson = new Writes[LunchProvider] {
    def writes(p: LunchProvider) = Json.obj(
      "id" -> p.id,
      "name" -> p.name,
      "location" -> p.location
    )
  }
}


@Singleton
class LunchProviderController @Inject()(system: ActorSystem)(implicit exec: ExecutionContext) extends Controller
  with LunchProviderJsonFormats {

  val TypeJson = s"${MimeTypes.JSON};charset=utf-8"

  val domainService = system.actorOf(LunchProviderService.props, "lunchProvider-actor")
  implicit val timeout = Timeout(5.seconds)


  def list(optLocation: Option[String]) = Action.async {
    val domainReqMsg = optLocation.map(GetByLocation).getOrElse(GetAll)

    domainService.ask(domainReqMsg).mapTo[MultiResult]
      .map(resultMsg => Ok(Json.toJson(resultMsg.providers)).as(TypeJson))
  }

  def find(id: Int) = Action.async {
    domainService.ask(GetById(id)).mapTo[SingleResult]
      .map{ _.provider match {
        case Some(provider) => Ok(Json.toJson(provider)).as(TypeJson)
        case None => NotFound(Json.obj("status" -> "404", "message" -> s"resource with id $id not found")).as(TypeJson)
      }}
  }
}
