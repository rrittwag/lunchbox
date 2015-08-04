package info.rori.lunchbox.server.akka.scala.domain.service


import akka.actor._
import info.rori.lunchbox.server.akka.scala.domain.logic._
import info.rori.lunchbox.server.akka.scala.domain.model.{LunchOffer, LunchProvider}
import info.rori.lunchbox.server.akka.scala.domain.model.LunchProvider._
import org.joda.time.{DateTimeZone, DateTime}
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

  import context.dispatcher

  // update LunchOffers on boot
  context.system.scheduler.scheduleOnce(50.milliseconds) {
    self ! StartUpdate
  }
  // update LunchOffers every day at 7:00 h
  val today7h = DateTime.now(DateTimeZone.forID("Europe/Berlin"))
    .withHourOfDay(7)
    .withMinuteOfHour(0)
    .withSecondOfMinute(0)
    .withMillisOfSecond(0)
  val future7h = if (today7h.isBeforeNow) today7h.plusHours(24) else today7h
  val durationTo7h = future7h.getMillis - DateTime.now.getMillis

  val dailyUpdate = context.system.scheduler.schedule(durationTo7h.millis, 24.hours) {
    self ! StartUpdate
  }

  // additionally update LunchOffers every monday at 10:00 h
  val monday10h = DateTime.now(DateTimeZone.forID("Europe/Berlin"))
    .withDayOfWeek(1)
    .withHourOfDay(10)
    .withMinuteOfHour(0)
    .withSecondOfMinute(0)
    .withMillisOfSecond(0)
  val futureMonday10h = if (monday10h.isBeforeNow) monday10h.plusWeeks(1) else monday10h
  val durationToMonday10h = futureMonday10h.getMillis - DateTime.now.getMillis

  val monday10hUpdate = context.system.scheduler.schedule(durationToMonday10h.millis, 7.days) {
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

    lunchOfferService ! RemoveOldOffers

    for (provider <- LunchProvider.values) {
      val nameForWorker = LunchOfferUpdateWorker.NamePrefix + provider.getClass.getSimpleName
      if (context.child(nameForWorker).isEmpty)
        context.actorOf(LunchOfferUpdateWorker.props(self, provider), nameForWorker)
    }
  }

  override def preStart(): Unit = {
    log.info("Actor started")
  }

  override def postStop(): Unit = {
    dailyUpdate.cancel()
    monday10hUpdate.cancel()
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

  // TODO: DI für Util & LunchResolver verwenden
  val dateValidator = new DateValidator()

  val offersFuture: Future[Seq[LunchOffer]] = lunchProvider match {
    case SCHWEINESTALL => new LunchResolverSchweinestall(dateValidator).resolve
    case HOTEL_AM_RING => new LunchResolverHotelAmRing(dateValidator).resolve
    case SUPPENKULTTOUR => new LunchResolverSuppenkulttour(dateValidator).resolve
    case AOK_CAFETERIA => new LunchResolverAokCafeteria(dateValidator).resolve
    case SALT_N_PEPPER => new LunchResolverSaltNPepper(dateValidator).resolve
    case GESUNDHEITSZENTRUM => new LunchResolverGesundheitszentrum(dateValidator).resolve
    case _ => Future(Nil)
  }
  offersFuture.onComplete {
    case Success(offers) =>
      lunchOfferUpdater ! WorkerFinished(offers)
      self ! PoisonPill
    case Failure(t) =>
      log.error(t, s"LunchResolver for $lunchProvider terminated") // TODO: Fehler besser loggen
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