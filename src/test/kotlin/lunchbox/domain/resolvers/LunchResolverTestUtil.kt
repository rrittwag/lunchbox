package lunchbox.domain.resolvers

import java.time.DayOfWeek
import java.time.LocalDate
import org.joda.money.Money

/**
 * Hilfsfunktionen für LunchResolver-Tests.
 */

fun date(dateStr: String): LocalDate = LocalDate.parse(dateStr)

fun euro(moneyStr: String): Money = Money.parse("EUR $moneyStr")

fun weekOf(dateString: String): Week = Week(date(dateString))

data class Week(val dateInWeek: LocalDate) {
  val monday: LocalDate = dateInWeek.with(DayOfWeek.MONDAY)
  val tuesday: LocalDate = dateInWeek.with(DayOfWeek.TUESDAY)
  val wednesday: LocalDate = dateInWeek.with(DayOfWeek.WEDNESDAY)
  val thursday: LocalDate = dateInWeek.with(DayOfWeek.THURSDAY)
  val friday: LocalDate = dateInWeek.with(DayOfWeek.FRIDAY)

  val lunchDays = listOf(monday, tuesday, wednesday, thursday, friday)
}
