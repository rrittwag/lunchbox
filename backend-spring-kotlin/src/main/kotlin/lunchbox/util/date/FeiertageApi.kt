package lunchbox.util.date

import lunchbox.domain.models.Bundesland
import java.time.LocalDate
import java.time.Year

interface FeiertageApi {
  fun queryFeiertage(
    jahre: Set<Year>,
    laender: Set<Bundesland>,
  ): Set<Feiertag>
}

data class Feiertag(
  val land: Bundesland,
  val datum: LocalDate,
  val name: String,
)
