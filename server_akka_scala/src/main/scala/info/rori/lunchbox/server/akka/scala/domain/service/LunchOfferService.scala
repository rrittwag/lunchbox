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
  case class MultiResult(offers: Seq[LunchOffer])
  case class SingleResult(offer: Option[LunchOffer])
}

class LunchOfferService extends Actor {
  private val offers = Seq[LunchOffer]()

  import LunchOfferService._

  override def receive = {
    case GetAll => sender ! MultiResult(offers)
    case GetById(id) => sender ! SingleResult(offers.find(_.id == id))
    case GetByDay(day) => sender ! MultiResult(offers.filter(_.day == day))
  }
}
