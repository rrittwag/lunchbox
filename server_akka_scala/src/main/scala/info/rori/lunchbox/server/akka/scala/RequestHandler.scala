package info.rori.lunchbox.server.akka.scala

import akka.http.model._
import akka.http.model.HttpMethods._

object RequestHandler {
  def apply() = new RequestHandler
}

class RequestHandler {

  val handler: HttpRequest => HttpResponse = {
    case HttpRequest(GET, Uri.Path("/ping"), _, _, _) => HttpResponse(entity = "PONG!")
    case _: HttpRequest => HttpResponse(404, entity = "Unknown resource!")
  }
}
