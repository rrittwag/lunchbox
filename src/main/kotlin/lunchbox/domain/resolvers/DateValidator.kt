package lunchbox.domain.resolvers

import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Prüft im LunchResolver, ob ein Mittagsangebot aktuell oder bereits veraltet ist.
 */
interface DateValidator {

  fun isValid(day: LocalDate): Boolean

  companion object {
    /**
     * Mittagsangebote vor der vergangenen Woche sind irrelevant.
     */
    fun validFromMondayLastWeek() = object : DateValidator {
      override fun isValid(day: LocalDate) = day >= mondayLastWeek()
    }

    /**
     * Veraltete Mittagsangebote sind irrelevant.
     */
    fun validFrom(fromDate: LocalDate) = object : DateValidator {
      override fun isValid(day: LocalDate) = day >= fromDate
    }

    /**
     * Immer gültig.
     */
    fun alwaysValid() = object : DateValidator {
      override fun isValid(day: LocalDate) = true
    }

    fun mondayLastWeek(): LocalDate {
      val mondayThisWeek = LocalDate.now().with(DayOfWeek.MONDAY)
      return mondayThisWeek.minusWeeks(1)
    }
  }
}
