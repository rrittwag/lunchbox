package lunchbox.domain.logic

import java.time.LocalDate

/**
 * Prüft im LunchResolver, ob ein Mittagsangebot aktuell oder bereits veraltet ist.
 */
interface DateValidator {

  fun isValid(day: LocalDate): Boolean

  companion object {
    fun validFrom(fromDate: LocalDate): DateValidator =
      DateValidator_From(fromDate)
  }
}

class DateValidator_From(
  private val fromDate: LocalDate
) : DateValidator {

  /**
   * Es sind nur die Mittagsangebote ab der vergangenen Woche interessant. Diese Methode prüft, ein Tag innerhalb dieses Zeitrahmens liegt.
   * <p>
   * @param day der Tag
   * @return
   */
  override fun isValid(day: LocalDate): Boolean {
    return day >= fromDate
  }
}
