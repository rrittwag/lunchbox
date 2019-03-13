package lunchbox.domain.models

import org.joda.money.Money
import java.time.LocalDate

val DATE_XMAS: LocalDate = LocalDate.of(2019, 12, 24)
val DATE_NEWYEAR: LocalDate = LocalDate.of(2019, 1, 1)

val GYROS = LunchOffer(
  1,
  "Gyros",
  DATE_NEWYEAR,
  Money.parse("EUR 3.50"),
  LunchProvider.TABBOULEH.id
)

val SOLJANKA = LunchOffer(
  2,
  "Soljanka",
  DATE_XMAS,
  Money.parse("EUR 2.50"),
  LunchProvider.SCHWEINESTALL.id
)
