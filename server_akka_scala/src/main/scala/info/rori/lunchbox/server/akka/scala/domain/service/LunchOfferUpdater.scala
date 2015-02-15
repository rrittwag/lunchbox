package info.rori.lunchbox.server.akka.scala.domain.service

import akka.actor._
import info.rori.lunchbox.server.akka.scala.domain.logic.LunchResolverStrategySchweinestall
import info.rori.lunchbox.server.akka.scala.domain.model.{LunchOffer, LunchProvider}
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider.SCHWEINESTALL
import scala.concurrent.duration._

import scala.util.{Failure, Success}


object LunchOfferUpdater {
  val Name = "LunchOfferUpdater"

  def props(lunchOfferService: ActorRef) = Props(new LunchOfferUpdater(lunchOfferService))

  case object StartUpdate
  case class WorkerFinished(offers: Seq[LunchOffer])

}

class LunchOfferUpdater(lunchOfferService: ActorRef) extends Actor with ActorLogging {

  import LunchOfferUpdater._
  import LunchOfferService._

  // update LunchOffers on boot
  import context.dispatcher
  context.system.scheduler.scheduleOnce(50.milliseconds) {
    self ! StartUpdate
  }

  override def receive = {
    case StartUpdate => startUpdate()
    case WorkerFinished(resolvedOffers) =>
      if (resolvedOffers.nonEmpty)
        lunchOfferService ! UpdateOffers(resolvedOffers)
  }

  // TODO: kill Aktoren, die bereits 60s laufen
  def startUpdate(): Unit = {
    log.info("LunchOfferUpdater.startUpdate()")

    for (provider <- LunchProvider.values) {
      val nameForProviderWorker = LunchOfferUpdateWorker.NamePrefix + provider.getClass.getSimpleName
      if (!context.child(nameForProviderWorker).isDefined)
        context.actorOf(LunchOfferUpdateWorker.props(self, provider), nameForProviderWorker)
    }
  }

  override def preStart(): Unit = {
    log.info("Actor started")
  }

  override def postStop(): Unit = {
    log.info("Actor stopped")
  }
}

object LunchOfferUpdateWorker {
  val NamePrefix = "LunchOfferUpdateWorker"

  def props(lunchOfferUpdater: ActorRef, lunchProvider: LunchProvider) =
    Props(new LunchOfferUpdateWorker(lunchOfferUpdater, lunchProvider))

}

class LunchOfferUpdateWorker(lunchOfferUpdater: ActorRef, lunchProvider: LunchProvider) extends Actor with ActorLogging {
  import scala.concurrent._
  import ExecutionContext.Implicits.global
  import LunchOfferUpdater._

  Future {
    if (lunchProvider == SCHWEINESTALL)
      new LunchResolverStrategySchweinestall().resolve
    else
      Seq[LunchOffer]()
  } onComplete {
    case Success(offers) =>
      lunchOfferUpdater ! WorkerFinished(offers)
      self ! PoisonPill
    case Failure(t) =>
      log.error(t, s"LunchResolverStrategy with $lunchProvider terminated") // TODO: Fehler besser loggen
      lunchOfferUpdater ! WorkerFinished(Seq())
      self ! PoisonPill
  }

  override def receive = {
    case _ =>
  }

  override def preStart(): Unit = {
    log.info("Actor started")
  }

  override def postStop(): Unit = {
    log.info("Actor stopped")
  }
}