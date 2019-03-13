package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import org.springframework.stereotype.Repository

@Repository
class LunchOfferRepositoryImpl_InMemory(
  @Volatile var offers: List<LunchOffer> = emptyList()
) : LunchOfferRepository {

  override fun findAll(): List<LunchOffer> {
    return offers
  }

  override fun deleteAll() {
    offers = emptyList()
  }

  override fun saveAll(entities: Iterable<LunchOffer>): Iterable<LunchOffer> {
    offers = offers.plus(entities)
    return entities
  }
}
