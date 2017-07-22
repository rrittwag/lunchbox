package controllers.api.v1

import javax.inject.{Inject, Singleton}

import akka.pattern.ask
import akka.util.Timeout
import domain.DomainApi
import domain.models.LunchProvider
import domain.services.LunchProviderService._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait LunchProviderJsonFormats {
  implicit val writesJson = new Writes[LunchProvider] {
    def writes(p: LunchProvider) = Json.obj(
      "id" -> p.id,
      "name" -> p.name,
      "location" -> p.location)
  }
}

@Singleton
class LunchProviderController @Inject() (domain: DomainApi)(implicit exec: ExecutionContext) extends InjectedController with LunchProviderJsonFormats {

  implicit val timeout = Timeout(5.seconds)

  def list(optLocation: Option[String]) = Action.async {
    val domainReqMsg = optLocation.map(GetByLocation).getOrElse(GetAll)

    domain.lunchProviderService.ask(domainReqMsg).mapTo[MultiResult]
      .map(resultMsg => Ok(Json.toJson(resultMsg.providers)))
  }

  def find(id: Int) = Action.async {
    domain.lunchProviderService.ask(GetById(id)).mapTo[SingleResult]
      .map { resultMsg =>
        resultMsg.provider match {
          case Some(provider) => Ok(Json.toJson(provider))
          case None => NotFound(Json.obj("status" -> "404", "message" -> s"resource with id $id not found"))
        }
      }
  }
}
