package services

import play.api.http.{MimeTypes, HttpErrorHandler}
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent._

class ErrorHandler extends HttpErrorHandler {

  def onClientError(request: RequestHeader, statusCode: Int, message: String) = {
    Future.successful {
      val content = Json.obj("status" -> statusCode, "message" -> message)
      Status(statusCode)(content).as(s"${MimeTypes.JSON};charset=utf-8")
    }
  }

  def onServerError(request: RequestHeader, e: Throwable) = {
    Future.successful {
      val content = Json.obj("status" -> "500", "message" -> e.getMessage)
      e.printStackTrace()
      InternalServerError(content).as(s"${MimeTypes.JSON};charset=utf-8")
    }
  }
}