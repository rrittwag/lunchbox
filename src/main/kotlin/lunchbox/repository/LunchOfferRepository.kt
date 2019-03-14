package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import java.time.LocalDate

interface LunchOfferRepository {
  fun findAll(): List<LunchOffer>

  fun findByDay(day: LocalDate): List<LunchOffer>

  fun findByIdOrNull(id: Long): LunchOffer?

  fun deleteAll()

  fun saveAll(entities: Iterable<LunchOffer>): Iterable<LunchOffer>
}
