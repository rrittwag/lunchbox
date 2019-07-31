package lunchbox.domain.logic

import org.joda.money.Money
import java.time.DayOfWeek
import java.time.LocalDate

fun date(dateStr: String): LocalDate = LocalDate.parse(dateStr)

fun euro(moneyStr: String): Money = Money.parse("EUR $moneyStr")

fun weekOf(dateString: String): Week = Week(date(dateString))

data class Week(val dateInWeek: LocalDate) {
  val monday: LocalDate by lazy { dateInWeek.with(DayOfWeek.MONDAY) }
  val tuesday: LocalDate by lazy { dateInWeek.with(DayOfWeek.TUESDAY) }
  val wednesday: LocalDate by lazy { dateInWeek.with(DayOfWeek.WEDNESDAY) }
  val thursday: LocalDate by lazy { dateInWeek.with(DayOfWeek.THURSDAY) }
  val friday: LocalDate by lazy { dateInWeek.with(DayOfWeek.FRIDAY) }
}
