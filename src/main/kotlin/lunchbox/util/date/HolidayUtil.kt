package lunchbox.util.date

import java.time.LocalDate
import java.time.Month
import lunchbox.domain.models.LunchLocation
import lunchbox.domain.models.LunchLocation.BERLIN_SPRINGPFUHL
import lunchbox.domain.models.LunchLocation.NEUBRANDENBURG

/**
 * Hilfsmethoden für den Umgang mit deutschen Feiertagen.
 */
object HolidayUtil {

  fun isHoliday(day: LocalDate, location: LunchLocation): Boolean {
    if (Holiday.values().any { it.isHoliday(day, location) })
      return true

    // TODO Ostertage, Christi Himmelfahrt & Pfingsten berechnen

    return false
  }
}

enum class Holiday(
  private val month: Month,
  private val dayOfMonth: Int,
  private vararg val locations: LunchLocation = LunchLocation.values()
) {
  // bundesdeutsche Feiertage
  NEUJAHR(Month.JANUARY, 1),
  TAG_DER_ARBEIT(Month.MAY, 1),
  TAG_DER_DEUTSCHEN_EINHEIT(Month.OCTOBER, 3),
  WEIHNACHTSFEIERTAG_1(Month.DECEMBER, 25),
  WEIHNACHTSFEIERTAG_2(Month.DECEMBER, 26),

  // Feiertage Berlin
  FRAUENTAG(Month.MARCH, 8, BERLIN_SPRINGPFUHL),

  // Feiertage MV
  REFORMATIONSTAG(Month.OCTOBER, 31, NEUBRANDENBURG);

  fun isHoliday(day: LocalDate, location: LunchLocation): Boolean =
    day.month == month && day.dayOfMonth == dayOfMonth && locations.contains(location)
}
