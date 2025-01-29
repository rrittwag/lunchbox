package lunchbox.util.date

import io.github.oshai.kotlinlogging.KotlinLogging
import lunchbox.domain.models.LunchLocation
import lunchbox.domain.models.LunchProvider
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Year

@Component
class FeiertageValidator(
  val api: FeiertageApi,
) : DateValidator {
  private val logger = KotlinLogging.logger {}
  val feiertage = loadFeiertage()

  private final fun loadFeiertage(): Set<Feiertag> {
    val bundeslaender = LunchLocation.entries.map { it.bundesland }.toSet()
    val thisYear = Year.now()
    val nextYear = thisYear.plusYears(1)
    val result = api.queryFeiertage(setOf(thisYear, nextYear), bundeslaender)
    logger.info { "${result.size} Feiertage geladen" }
    logger.debug { "Geladene Feiertage: ${result.joinToString { ", " }}" }
    return result
  }

  override fun isValid(
    day: LocalDate,
    provider: LunchProvider,
  ): Boolean = feiertage.none { it.land == provider.location.bundesland && it.datum == day }
}
