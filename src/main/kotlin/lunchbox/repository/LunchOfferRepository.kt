package lunchbox.repository

import lunchbox.domain.models.LunchOffer

interface LunchOfferRepository {
  fun findAll(): List<LunchOffer>

  fun findByIdOrNull(id: Long): LunchOffer?

  fun deleteAll()

  fun saveAll(entities: Iterable<LunchOffer>): Iterable<LunchOffer>
}
