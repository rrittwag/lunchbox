package info.rori.lunchbox.server.akka.scala.domain.service

import akka.actor.{ActorLogging, Actor, Props}
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
  case class UpdateOffers(offers: Seq[LunchOffer])
}

class LunchOfferService extends Actor with ActorLogging {
  private var offers = Seq[LunchOffer]()

  context.actorOf(LunchOfferUpdater.props(self), LunchOfferUpdater.Name)

  import LunchOfferService._

  override def receive = {
    case GetAll => sender ! MultiResult(offers)
    case GetById(id) => sender ! SingleResult(offers.find(_.id == id))
    case GetByDay(day) => sender ! MultiResult(offers.filter(_.day == day))
    case UpdateOffers(offersForUpdate) => updateOffers(offersForUpdate)
  }

  def updateOffers(newOffers: Seq[LunchOffer]): Unit = {
    val updatedKeys = newOffers.map(o => (o.provider, o.day)).toSet
    val (offersToReplace, offersToKeep) = offers.partition(o => updatedKeys.contains((o.provider, o.day)))

    val reusableIDs = offersToReplace.map(_.id)
    val maxId = offers.foldLeft(0)(_ max _.id)
    val availableIDs = reusableIDs ++ Stream.from(maxId + 1).take(newOffers.size)

    val newOffersWithIds = newOffers.zip(availableIDs).map { case (offer, newId) => offer.copy(id = newId)}
    offers = (offersToKeep ++ newOffersWithIds).sorted

    log.info(s"updateOffers: added $newOffersWithIds, removed $offersToReplace")
  }
}
