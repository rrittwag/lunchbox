package info.rori.lunchbox.server.akka.scala.service.api

package object v1 {

  trait ApiRouteV1
    extends LunchProviderRoute {

    def apiRouteV1 =
      pathPrefix("api" / "v1") {
        lunchProviderRoute
      }

  }

}