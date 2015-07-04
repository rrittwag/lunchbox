package info.rori.lunchbox.server.akka.scala.api.v1

trait ApiRouteV1
  extends LunchProviderRoute_ApiV1
  with LunchOfferRoute_ApiV1 {

  def apiRouteV1 =
    logRequest(context.system.name) {
      pathPrefix("api" / "v1") {
        lunchProviderRoute ~ lunchOfferRoute
      }
    }
}
