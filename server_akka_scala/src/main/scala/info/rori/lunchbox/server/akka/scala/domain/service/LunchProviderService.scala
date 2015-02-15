package info.rori.lunchbox.server.akka.scala.domain.service

import akka.actor.{Actor, Props}
import info.rori.lunchbox.server.akka.scala.domain.model._

// TODO: LunchProvider ist nun ein ValueObject. Ist der Service noch sinnvoll?
object LunchProviderService {
  val Name = "LunchProviderService"
  def props = Props(new LunchProviderService)

  case object GetAll
  case class GetById(id: LunchProviderId)
  case class GetByLocation(location: Location)
  case class MultiResult(providers: Set[LunchProvider])
  case class SingleResult(provider: Option[LunchProvider])
}

class LunchProviderService extends Actor {
  private val providers = LunchProvider.values

  import LunchProviderService._

  override def receive = {
    case GetAll => sender ! MultiResult(providers)
    case GetById(id) => sender ! SingleResult(providers.find(_.id == id))
    case GetByLocation(location) => sender ! MultiResult(providers.filter(_.location == location))
  }
}
