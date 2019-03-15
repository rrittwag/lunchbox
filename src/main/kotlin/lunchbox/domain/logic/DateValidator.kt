package lunchbox.domain.logic

import java.time.DayOfWeek
import java.time.LocalDate

class DateValidator {

  /**
   * Es sind nur die Mittagsangebote ab der vergangenen Woche interessant. Diese Methode prüft, ein Tag innerhalb dieses Zeitrahmens liegt.
   * <p>
   * @param day der Tag
   * @return
   */
  fun isValid(day: LocalDate): Boolean {
    val mondayThisWeek = LocalDate.now().`with`(DayOfWeek.MONDAY)
    val mondayLastWeek = mondayThisWeek.minusWeeks(1)
    return day >= mondayLastWeek
  }
}
