package lunchbox.domain.logic

import java.time.LocalDate

/**
 * Prüft im LunchResolver, ob ein Mittagsangebot aktuell oder bereits veraltet ist.
 */
interface DateValidator {

  fun isValid(day: LocalDate): Boolean

  companion object {
    /**
     * Veraltete Mittagsangebote sind irrelevant.
     */
    fun validFrom(fromDate: LocalDate) = object : DateValidator {
      override fun isValid(day: LocalDate) = day >= fromDate
    }
  }
}
