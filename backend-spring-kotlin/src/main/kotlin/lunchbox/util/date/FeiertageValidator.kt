package lunchbox.util.date

import lunchbox.domain.models.LunchLocation
import lunchbox.domain.models.LunchProvider
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Year

@Component
class FeiertageValidator(val api: FeiertageApi) : DateValidator {
  val feiertage = loadFeiertage()

  private final fun loadFeiertage(): Set<Feiertag> {
    val bundeslaender = LunchLocation.entries.map { it.bundesland }.toSet()
    val thisYear = Year.now()
    val nextYear = thisYear.plusYears(1)
    return api.queryFeiertage(setOf(thisYear, nextYear), bundeslaender)
  }

  override fun isValid(
    day: LocalDate,
    provider: LunchProvider,
  ): Boolean = feiertage.none { it.land == provider.location.bundesland && it.datum == day }
}
