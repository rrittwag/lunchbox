package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchOfferId
import lunchbox.domain.models.LunchProviderId
import java.time.LocalDate

/**
 * Repository für Mittagsangebote.
 */
interface LunchOfferRepository {
  fun findAll(): List<LunchOffer>

  fun findByDay(day: LocalDate): List<LunchOffer>

  fun findByIdOrNull(id: LunchOfferId): LunchOffer?

  fun deleteBefore(day: LocalDate)

  fun deleteFrom(
    day: LocalDate,
    providerId: LunchProviderId,
  )

  fun saveAll(newOffers: Iterable<LunchOffer>): Iterable<LunchOffer>
}
