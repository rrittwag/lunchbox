package info.rori.lunchbox.server.akka.scala.service.api.v1

trait ApiRouteV1
  extends LunchProviderRoute_ApiV1 {

  def apiRouteV1 =
    pathPrefix("api" / "v1") {
      lunchProviderRoute
    }

}
