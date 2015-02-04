package info.rori.lunchbox.server.akka.scala.service

import akka.http.model.HttpResponse

package object feed {

  trait FeedRoute
    extends HttpRoute {

    def feedRoute =
      path("feed") {
        get {
          complete(HttpResponse(entity = "feed"))
        }
      }
  }

}
