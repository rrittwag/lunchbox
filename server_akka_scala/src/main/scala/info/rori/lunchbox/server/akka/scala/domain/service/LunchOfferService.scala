package info.rori.lunchbox.server.akka.scala.domain.service

import akka.actor.{Actor, Props}
import info.rori.lunchbox.server.akka.scala.domain.model._
import org.joda.time.LocalDate

object LunchOfferService {
  val Name = "LunchOfferService"
  def props = Props(new LunchOfferService)

  case object GetAll
  case class GetById(id: Id)
  case class GetByDay(day: LocalDate)
  case class MultiResult(providers: Seq[LunchOffer])
  case class SingleResult(provider: Option[LunchOffer])
}

class LunchOfferService extends Actor {
  private val providers = Seq[LunchOffer]()

  import LunchOfferService._

  override def receive = {
    case GetAll => sender ! MultiResult(providers)
    case GetById(id) => sender ! SingleResult(providers.find(_.id == id))
    case GetByDay(day) => sender ! MultiResult(providers.filter(_.day == day))
  }
}
