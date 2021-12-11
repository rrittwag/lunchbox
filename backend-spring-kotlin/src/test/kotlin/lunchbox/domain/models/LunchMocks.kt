package lunchbox.domain.models

import org.joda.money.Money
import java.time.LocalDate

val DATE_XMAS: LocalDate = LocalDate.of(2019, 12, 24)
val DATE_NEWYEAR: LocalDate = LocalDate.of(2019, 1, 1)

fun createOffer(
  id: LunchOfferId = 0,
  name: String = "Mitagsangebot",
  description: String = "Beschreibung",
  day: LocalDate = DATE_NEWYEAR,
  price: Money = Money.parse("EUR 5.80"),
  tags: Set<String> = setOf("Tagessuppe", "vegan"),
  provider: LunchProviderId = LunchProvider.SCHWEINESTALL.id
) = LunchOffer(id, name, description, day, price, tags, provider)

val GYROS = createOffer(
  name = "Gyros",
  description = "mit Pommes"
)

val SOLJANKA = createOffer(
  name = "Soljanka",
  day = DATE_XMAS,
  price = Money.parse("EUR 2.50"),
  provider = LunchProvider.AOK_CAFETERIA.id
)

val GYROS_NEXT_DAY = GYROS.copy(day = GYROS.day.plusDays(1))
