package lunchbox.domain.service

import jakarta.annotation.PostConstruct
import lunchbox.domain.models.LunchProvider
import lunchbox.repository.LunchOfferRepository
import lunchbox.util.date.DateValidator
import mu.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.scheduling.annotation.Schedules
import org.springframework.stereotype.Service

/**
 * Aktualisiert die Mittagsangebote aller Anbieter.
 */
@Service
class LunchOfferUpdate(
  val repo: LunchOfferRepository,
  val worker: LunchOfferUpdateWorker
) {

  private val logger = KotlinLogging.logger {}

  @PostConstruct // Aktualisiere beim Start und ...
  @Schedules(
    Scheduled(cron = "0 0  7 * *   *", zone = "Europe/Berlin"), // jeden Tag um 7 Uhr und ...
    Scheduled(cron = "0 0 10 * * MON", zone = "Europe/Berlin") // jeden Montag um 10 Uhr
  )
  fun updateOffers() {
    logger.info { "starting offer update" }

    removeOutdatedOffers()

    for (provider in LunchProvider.values().filter { it.active })
      worker.refreshOffersOf(provider)
  }

  private fun removeOutdatedOffers() {
    repo.deleteBefore(DateValidator.mondayLastWeek())
  }
}
