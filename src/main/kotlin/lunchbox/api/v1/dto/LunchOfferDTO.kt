package lunchbox.api.v1.dto

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchOfferId
import lunchbox.domain.models.LunchProviderId
import org.joda.money.Money
import java.time.LocalDate

/**
 * API-DTO für Mittagsangebot.
 */
data class LunchOfferDTO(
  val id: LunchOfferId,
  val name: String,
  val day: LocalDate,
  val price: Money,
  val provider: LunchProviderId
) {

  companion object {
    fun of(offer: LunchOffer) =
      LunchOfferDTO(
        offer.id,
        offer.name,
        offer.day,
        offer.price,
        offer.provider
      )
  }
}
