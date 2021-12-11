package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchOfferId
import lunchbox.domain.models.LunchProviderId
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Hält die Mittagsangebote im Arbeitsspeicher.
 * <p>
 * Der Zugriff auf die Mittagsangebote ist thread-safe.
 */
@Repository
class LunchOfferRepositoryImplInMemory : LunchOfferRepository {
  val offers = CopyOnWriteArrayList<LunchOffer>()

  override fun findAll(): List<LunchOffer> =
    offers

  override fun findByDay(day: LocalDate): List<LunchOffer> =
    offers.filter { it.day == day }

  override fun findByIdOrNull(id: LunchOfferId): LunchOffer? =
    offers.find { it.id == id }

  @Synchronized
  override fun deleteBefore(day: LocalDate) {
    offers.removeIf { it.day < day }
  }

  @Synchronized
  override fun deleteFrom(day: LocalDate, providerId: LunchProviderId) {
    offers.removeIf { it.provider == providerId && it.day >= day }
  }

  @Synchronized
  override fun saveAll(newOffers: Iterable<LunchOffer>): Iterable<LunchOffer> {
    val nextId = (offers.map { it.id }.maxOrNull() ?: 0) + 1
    val newOffersWithId = newOffers.mapIndexed { index, offer ->
      offer.copy(id = nextId + index)
    }
    offers += newOffersWithId
    return newOffersWithId
  }
}
