package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProviderId
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class LunchOfferRepositoryImpl_InMemory(
  var offers: List<LunchOffer> = emptyList()
) : LunchOfferRepository {

  @Synchronized
  override fun findAll(): List<LunchOffer> =
    offers

  @Synchronized
  override fun findByDay(day: LocalDate): List<LunchOffer> =
    offers.filter { it.day == day }

  @Synchronized
  override fun findByIdOrNull(id: Long): LunchOffer? =
    offers.find { it.id == id }

  @Synchronized
  override fun deleteBefore(day: LocalDate) {
    offers = offers.filter { it.day >= day }
  }

  @Synchronized
  override fun deleteFrom(day: LocalDate, providerId: LunchProviderId) {
    offers = offers.filter { it.provider != providerId || it.day < day }
  }

  @Synchronized
  override fun saveAll(newOffers: Iterable<LunchOffer>): Iterable<LunchOffer> {
    val nextId = (offers.map { it.id }.max() ?: 0) + 1
    val newOffersWithId = newOffers.mapIndexed {
        index, lunchOffer -> lunchOffer.copy(id = nextId + index)
      }
    offers += newOffersWithId
    return newOffersWithId
  }
}
