package domain.logic

import java.time.{DayOfWeek, LocalDate}

class DateValidator {

  /**
   * Es sind nur die Mittagsangebote ab der vergangenen Woche interessant. Diese Methode prüft, ein Tag innerhalb dieses Zeitrahmens liegt.
   * <p>
   * @param day der Tag
   * @return
   */
  def isValid(day: LocalDate): Boolean = {
    val mondayThisWeek = LocalDate.now.`with`(DayOfWeek.MONDAY)
    val mondayLastWeek = mondayThisWeek.minusWeeks(1)
    day.compareTo(mondayLastWeek) >= 0
  }

}
