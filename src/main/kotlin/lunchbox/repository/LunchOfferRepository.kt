package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProviderId
import java.time.LocalDate

interface LunchOfferRepository {
  fun findAll(): List<LunchOffer>

  fun findByDay(day: LocalDate): List<LunchOffer>

  fun findByIdOrNull(id: Long): LunchOffer?

  fun deleteBefore(day: LocalDate)

  fun deleteFrom(day: LocalDate, providerId: LunchProviderId)

  fun saveAll(newOffers: Iterable<LunchOffer>): Iterable<LunchOffer>
}
