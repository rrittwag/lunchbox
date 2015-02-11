package info.rori.lunchbox.server.akka.scala.domain.service

import akka.actor.{Actor, Props}
import info.rori.lunchbox.server.akka.scala.domain.model._

object LunchProviderService {
  val Name = "LunchProviderService"
  def props = Props(new LunchProviderService)

  case object GetAll
  case class GetById(id: LunchProviderId)
  case class GetByLocation(location: Location)
  case class MultiResult(providers: Seq[LunchProvider])
  case class SingleResult(provider: Option[LunchProvider])
}

class LunchProviderService extends Actor {
  private val providers = Seq(
    LunchProvider(1, "Schweinestall", "Neubrandenburg"),
    LunchProvider(2, "Hotel am Ring", "Neubrandenburg"),
    LunchProvider(3, "AOK Cafeteria", "Neubrandenburg"),
    LunchProvider(4, "Suppenkulttour", "Neubrandenburg"),
    LunchProvider(5, "Salt 'n' Pepper", "Berlin")
  )

  import LunchProviderService._

  override def receive = {
    case GetAll => sender ! MultiResult(providers)
    case GetById(id) => sender ! SingleResult(providers.find(_.id == id))
    case GetByLocation(location) => sender ! MultiResult(providers.filter(_.location == location))
  }
}
