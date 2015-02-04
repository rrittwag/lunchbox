package info.rori.lunchbox.server.akka.scala.service.api.v1

import akka.http.model.HttpResponse
import info.rori.lunchbox.server.akka.scala.service.HttpRoute


case class LunchProvider(id: Int, name: String)


trait LunchProviderRoute
  extends HttpRoute {

  val lunchProviderRoute =
    path("lunchProvider") {
      get {
        complete(HttpResponse(entity = s"api/v1/lunchProvider"))
//        handleWith { ru: Register => (registration ? ru).mapTo[Either[NotRegistered.type, Registered.type]] }
      }
    } ~
    path("lunchProvider" /  IntNumber) { id =>
      get {
        complete(HttpResponse(entity = s"api/v1/lunchProvider/$id"))
        }
      }
}
