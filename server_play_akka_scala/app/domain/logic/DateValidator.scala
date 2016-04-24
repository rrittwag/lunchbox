package domain.logic

import org.joda.time.LocalDate

class DateValidator {

  /**
   * Es sind nur die Mittagsangebote ab der vergangenen Woche interessant. Diese Methode prüft, ein Tag innerhalb dieses Zeitrahmens liegt.
   * <p>
   * @param day der Tag
   * @return
   */
  def isValid(day: LocalDate): Boolean = {
    val mondayThisWeek = LocalDate.now().withDayOfWeek(1)
    val mondayLastWeek = mondayThisWeek.minusWeeks(1)
    day.compareTo(mondayLastWeek) >= 0
  }

}
