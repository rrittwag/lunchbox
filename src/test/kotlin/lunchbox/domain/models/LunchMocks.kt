package lunchbox.domain.models

import java.time.LocalDate
import org.joda.money.Money

val DATE_XMAS: LocalDate = LocalDate.of(2019, 12, 24)
val DATE_NEWYEAR: LocalDate = LocalDate.of(2019, 1, 1)

fun createOffer(
  id: LunchOfferId = 0,
  name: String = "Mitagsangebot",
  details: String = "Details",
  day: LocalDate = DATE_NEWYEAR,
  price: Money = Money.parse("EUR 5.80"),
  tags: List<String> = listOf("Tagessuppe", "vegan"),
  provider: LunchProviderId = LunchProvider.SCHWEINESTALL.id
) = LunchOffer(id, name, details, day, price, tags, provider)

val GYROS = createOffer(
  name = "Gyros",
  details = "mit Pommes"
)

val SOLJANKA = createOffer(
  name = "Soljanka",
  day = DATE_XMAS,
  price = Money.parse("EUR 2.50"),
  provider = LunchProvider.AOK_CAFETERIA.id
)

val GYROS_NEXT_DAY = GYROS.copy(day = GYROS.day.plusDays(1))
