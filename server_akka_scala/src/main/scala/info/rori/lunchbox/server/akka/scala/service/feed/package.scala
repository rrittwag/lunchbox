package info.rori.lunchbox.server.akka.scala.service

import akka.http.model.HttpResponse
import akka.http.server.Directives._
import akka.http.server._

package object feed {
  val httpRoute: Route =
    path("feed") {
      get {
        complete(HttpResponse(entity = "feed"))
      }
    }
}
