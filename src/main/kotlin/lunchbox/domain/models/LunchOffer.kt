package lunchbox.domain.models

import org.joda.money.Money
import java.time.LocalDate

typealias Id = Long

data class LunchOffer(
  val id: Id,
  val name: String,
  val day: LocalDate,
  val price: Money,
  val provider: LunchProviderId
)
