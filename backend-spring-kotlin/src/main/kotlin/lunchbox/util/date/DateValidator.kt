package lunchbox.util.date

import lunchbox.domain.models.LunchProvider
import java.time.DayOfWeek
import java.time.LocalDate

/**
 * Prüft im LunchResolver, ob ein Mittagsangebot aktuell oder bereits veraltet ist.
 */
interface DateValidator {
  fun isValid(
    day: LocalDate,
    provider: LunchProvider,
  ): Boolean

  fun and(that: DateValidator): DateValidator = LogicalAnd(this, that)

  companion object {
    /**
     * Mittagsangebote vor der vergangenen Woche sind irrelevant.
     */
    fun validFromMondayLastWeek() =
      object : DateValidator {
        override fun isValid(
          day: LocalDate,
          provider: LunchProvider,
        ) = day >= mondayLastWeek()
      }

    /**
     * Veraltete Mittagsangebote sind irrelevant.
     */
    fun validFrom(fromDate: LocalDate) =
      object : DateValidator {
        override fun isValid(
          day: LocalDate,
          provider: LunchProvider,
        ) = day >= fromDate
      }

    /**
     * Immer gültig.
     */
    fun alwaysValid() =
      object : DateValidator {
        override fun isValid(
          day: LocalDate,
          provider: LunchProvider,
        ) = true
      }

    fun mondayLastWeek(): LocalDate {
      val mondayThisWeek = LocalDate.now().with(DayOfWeek.MONDAY)
      return mondayThisWeek.minusWeeks(1)
    }
  }
}

private class LogicalAnd(
  val one: DateValidator,
  val two: DateValidator,
) : DateValidator {
  override fun isValid(
    day: LocalDate,
    provider: LunchProvider,
  ): Boolean = one.isValid(day, provider) && two.isValid(day, provider)
}
