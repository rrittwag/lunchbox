package info.rori.lunchbox.server.akka.scala.service.feed

import akka.http.model.HttpResponse
import info.rori.lunchbox.server.akka.scala.service.HttpRoute

trait FeedRoute
  extends HttpRoute {

  def feedRoute =
    logRequest(context.system.name) {
      path("feed") {
        get {
          complete(HttpResponse(entity = "feed"))
        }
      }
    }
}
