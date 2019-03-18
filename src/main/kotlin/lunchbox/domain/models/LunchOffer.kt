package lunchbox.domain.models

import org.joda.money.Money
import java.time.LocalDate

typealias LunchOfferId = Long

/**
 * Mittagsangebot
 */
data class LunchOffer(
  val id: LunchOfferId,
  val name: String,
  val day: LocalDate,
  val price: Money,
  val provider: LunchProviderId
)
