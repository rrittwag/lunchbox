package lunchbox.util.date

import lunchbox.domain.models.Bundesland
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.LocalDate
import java.time.Year
import java.util.concurrent.CompletableFuture

/**
 * Ruft Feiertage aus der Feiertage API ab.
 */
@Component
class FeiertageApiImpl(
  val baseUrl: String = "https://feiertage-api.de",
) : FeiertageApi {
  override fun queryFeiertage(
    jahre: Set<Year>,
    laender: Set<Bundesland>,
  ): Set<Feiertag> {
    val httpCalls =
      jahre.flatMap { jahr ->
        laender.map { land ->
          CompletableFuture.supplyAsync { fetchFeiertage(jahr, land) }
        }
      }
    httpCalls.forEach { it.join() }
    return httpCalls.flatMap { it.get() }.toSet()
  }

  fun fetchFeiertage(
    jahr: Year,
    land: Bundesland,
  ): List<Feiertag> =
    RestClient
      .create("$baseUrl/api/?jahr=${jahr.value}&nur_land=${land.kuerzel}")
      .get()
      .retrieve()
      .body<Map<String, FeiertagDTO>>()
      .orEmpty()
      .map { entry ->
        Feiertag(
          land = land,
          datum = entry.value.datum,
          name = entry.key,
        )
      }
}

private data class FeiertagDTO(
  val datum: LocalDate,
  val hinweis: String?,
)
