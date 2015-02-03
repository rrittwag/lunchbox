package info.rori.lunchbox.server.akka.scala.resources.api

import akka.http.model.HttpResponse
import akka.http.server.Route
import akka.http.server.Directives._

package object v1 {
  val route: Route =
    path("api" / "v1") {
      get {
        complete(HttpResponse(entity = "api/v1"))
      }
    }

}