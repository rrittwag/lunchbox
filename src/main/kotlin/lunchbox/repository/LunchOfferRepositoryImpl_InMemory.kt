package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProviderId
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

  override fun deleteBefore(day: LocalDate) = synchronized(this) {
    offers = offers.filter { it.day >= day }
  }

  override fun deleteFrom(day: LocalDate, providerId: LunchProviderId) = synchronized(this) {
    offers = offers.filter { it.provider != providerId || it.day < day }
  }

  override fun saveAll(newOffers: Iterable<LunchOffer>): Iterable<LunchOffer> = synchronized(this) {
    val nextId = (offers.map { it.id }.max() ?: 0) + 1
    val newOffersWithId = newOffers.mapIndexed {
        index, lunchOffer -> lunchOffer.copy(id = nextId + index)
      }
    offers += newOffersWithId
    return newOffersWithId
  }
}
