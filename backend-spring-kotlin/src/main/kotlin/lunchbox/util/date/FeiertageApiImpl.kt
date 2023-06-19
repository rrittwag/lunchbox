package lunchbox.util.date

import lunchbox.domain.models.Bundesland
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.Flux
import reactor.util.retry.Retry
import java.time.Duration
import java.time.LocalDate
import java.time.Year

/**
 * Ruft Feiertage aus der Feiertage API ab.
 */
@Component
class FeiertageApiImpl(
  val baseUrl: String = "https://feiertage-api.de",
) : FeiertageApi {
  override fun queryFeiertage(jahre: Set<Year>, laender: Set<Bundesland>): Set<Feiertag> {
    val httpCalls =
      Flux.fromIterable(jahre).flatMap { jahr ->
        Flux.fromIterable(laender).flatMap { land ->
          WebClient.create("$baseUrl/api/?jahr=${jahr.value}&nur_land=${land.kuerzel}")
            .get()
            .retrieve()
            .bodyToFlux<Map<String, FeiertagDTO>>()
            .retryWhen(Retry.backoff(5, Duration.ofSeconds(5)))
            .map {
              it.map { entry ->
                Feiertag(
                  land = land,
                  datum = entry.value.datum,
                  name = entry.key,
                )
              }
            }
        }
      }
    val httpCallsAsMono = httpCalls.collectList().map { it.flatten() }
    return httpCallsAsMono.block()?.toSet() ?: return emptySet()
  }
}

private data class FeiertagDTO(
  val datum: LocalDate,
  val hinweis: String?,
)
