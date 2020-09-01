package lunchbox.domain.models

import java.time.LocalDate
import org.joda.money.Money

typealias LunchOfferId = Long

/**
 * Mittagsangebot
 */
data class LunchOffer(
  val id: LunchOfferId,
  val name: String,
  val description: String,
  val day: LocalDate,
  val price: Money?,
  val tags: Set<String>,
  val provider: LunchProviderId
)
