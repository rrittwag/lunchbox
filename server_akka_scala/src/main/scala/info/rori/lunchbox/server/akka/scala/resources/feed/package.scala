package info.rori.lunchbox.server.akka.scala.resources

import akka.http.model.HttpResponse
import akka.http.server.Directives._
import akka.http.server._

package object feed {
  val route: Route =
    path("feed") {
      get {
        complete(HttpResponse(entity = "feed"))
      }
    }
}
