package info.rori.lunchbox.server.akka.scala.domain.model

case class LunchProvider(
                          id: LunchProviderId,
                          name: String,
                          location: Location
                          )
