package lunchbox.api.v2

import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProviderId
import lunchbox.util.api.HttpNotFoundException
import lunchbox.util.api.RestApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * REST API-Controller für Mittagsanbieter.
 */
@RestApi
class LunchProviderApi {
  @GetMapping(URL_LUNCHPROVIDER)
  fun getAll(): List<LunchProviderDTO> =
    LunchProvider.entries
      .map { it.toDTOv2() }

  @GetMapping("$URL_LUNCHPROVIDER/{id}")
  fun getById(
    @PathVariable id: LunchProviderId,
  ): LunchProviderDTO =
    LunchProvider.entries
      .find { id == it.id }
      ?.toDTOv2()
      ?: throw HttpNotFoundException("Mittagsanbieter mit ID $id nicht gefunden!")
}

/**
 * DTO für Mittagsanbieter.
 */
class LunchProviderDTO(
  val id: LunchProviderId,
  val name: String,
  val location: String,
  val url: String,
)

fun LunchProvider.toDTOv2() =
  LunchProviderDTO(
    id,
    label,
    location.label,
    menuUrl.toString(),
  )
