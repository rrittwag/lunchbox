package domain.services

import java.time.{DayOfWeek, Instant, ZoneId, ZonedDateTime}

import akka.actor._
import domain.logic._
import domain.models.LunchProvider._
import domain.models.{LunchOffer, LunchProvider}
import infrastructure._

import play.api.libs.ws._

import scala.concurrent.duration._
import scala.util.{Failure, Success}

object LunchOfferUpdater {
  val Name = "LunchOfferUpdater"

  def props(lunchOfferService: ActorRef) = Props(new LunchOfferUpdater(lunchOfferService))

  case object StartUpdate

  case class WorkerFinished(offers: Seq[LunchOffer])

}

class LunchOfferUpdater(lunchOfferService: ActorRef) extends Actor with ActorLogging {

  import LunchOfferService._
  import LunchOfferUpdater._
  import context.dispatcher

  // update LunchOffers on boot
  context.system.scheduler.scheduleOnce(50.milliseconds) {
    self ! StartUpdate
  }
  // update LunchOffers every day at 7:00 h
  val today7h = ZonedDateTime.now(ZoneId.of("Europe/Berlin"))
    .withHour(7)
    .withMinute(0)
    .withSecond(0)
    .withNano(0)
  val future7h = if (today7h.isBefore(ZonedDateTime.now)) today7h.plusHours(24) else today7h
  val durationTo7h = future7h.toInstant.toEpochMilli - Instant.now.toEpochMilli

  val dailyUpdate = context.system.scheduler.schedule(durationTo7h.millis, 24.hours) {
    self ! StartUpdate
  }

  // additionally update LunchOffers every monday at 10:00 h
  val monday10h = ZonedDateTime.now(ZoneId.of("Europe/Berlin"))
    .`with`(DayOfWeek.MONDAY)
    .withHour(10)
    .withMinute(0)
    .withSecond(0)
    .withNano(0)
  val futureMonday10h = if (monday10h.isBefore(ZonedDateTime.now)) monday10h.plusWeeks(1) else monday10h
  val durationToMonday10h = futureMonday10h.toInstant.toEpochMilli - Instant.now.toEpochMilli

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

  import LunchOfferUpdater._

  import scala.concurrent._
  import ExecutionContext.Implicits.global

  // TODO: DI für Util & LunchResolver verwenden
  val dateValidator = new DateValidator()

  val offersFuture: Future[Seq[LunchOffer]] = lunchProvider match {
    case SCHWEINESTALL => new LunchResolverSchweinestall(dateValidator).resolve
    case HOTEL_AM_RING => new LunchResolverHotelAmRing(dateValidator).resolve
    case SUPPENKULTTOUR => new LunchResolverSuppenkulttour(dateValidator).resolve
    case AOK_CAFETERIA => new LunchResolverAokCafeteria(dateValidator).resolve
    case SALT_N_PEPPER => new LunchResolverSaltNPepper(dateValidator).resolve
    case GESUNDHEITSZENTRUM =>
      new LunchResolverGesundheitszentrum(
        dateValidator,
        new DefaultFacebookClient,
        new DefaultOcrClient
      ).resolve
    case FELDKUECHE =>
      new LunchResolverFeldkueche(dateValidator, new DefaultOcrClient).resolve
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