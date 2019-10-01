package lunchbox.util.date

import lunchbox.domain.models.LunchLocation
import java.time.LocalDate
import java.time.Month

object HolidayUtil {

  fun isHoliday(day: LocalDate, location: LunchLocation): Boolean {
    // Neujahr
    if (day.month == Month.JANUARY && day.dayOfMonth == 1)
      return true

    // Tag der Arbeit
    if (day.month == Month.MAY && day.dayOfMonth == 1)
      return true

    // Tag der deutschen Einheit
    if (day.month == Month.OCTOBER && day.dayOfMonth == 3)
      return true

    // Weihnachten & Silvester
    if (day.month == Month.DECEMBER && day.dayOfMonth in listOf(25, 26, 31))
      return true

    // Frauentag
    if (location == LunchLocation.BERLIN_SPRINGPFUHL) {
      if (day.month == Month.MARCH && day.dayOfMonth == 8)
        return true
    }

    // TODO Ostertage berechnen
    // TODO weitere Feiertage ergänzen

    return false
  }
}
