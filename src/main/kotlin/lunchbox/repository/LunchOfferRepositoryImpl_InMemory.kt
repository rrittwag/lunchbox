package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class LunchOfferRepositoryImpl_InMemory(
  @Volatile var offers: List<LunchOffer> = emptyList()
) : LunchOfferRepository {

  override fun findAll(): List<LunchOffer> =
    offers

  override fun findByDay(day: LocalDate): List<LunchOffer> =
    offers.filter { it.day == day }

  override fun findByIdOrNull(id: Long): LunchOffer? =
    offers.find { it.id == id }

  override fun deleteAll() {
    offers = emptyList()
  }

  override fun saveAll(entities: Iterable<LunchOffer>): Iterable<LunchOffer> {
    offers += entities
    return entities
  }
}
