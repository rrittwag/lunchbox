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
)

fun LunchOffer.toDTOv1() = LunchOfferDTO(
  id,
  name,
  day,
  price,
  provider
)
