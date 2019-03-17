package lunchbox.domain.models

import org.joda.money.Money
import java.time.LocalDate

val DATE_XMAS: LocalDate = LocalDate.of(2019, 12, 24)
val DATE_NEWYEAR: LocalDate = LocalDate.of(2019, 1, 1)

val GYROS = LunchOffer(
  0,
  "Gyros",
  DATE_NEWYEAR,
  Money.parse("EUR 5.80"),
  LunchProvider.SCHWEINESTALL.id
)

val SOLJANKA = LunchOffer(
  0,
  "Soljanka",
  DATE_XMAS,
  Money.parse("EUR 2.50"),
  LunchProvider.AOK_CAFETERIA.id
)

val GYROS_NEXT_DAY = GYROS.copy(day = GYROS.day.plusDays(1))
