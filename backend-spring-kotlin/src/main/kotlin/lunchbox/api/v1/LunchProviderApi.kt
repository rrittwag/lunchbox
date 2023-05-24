package lunchbox.api.v1

import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProviderId
import lunchbox.util.api.HttpNotFoundException
import lunchbox.util.api.RestApi
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

/**
 * REST API-Controller für Mittagsanbieter.
 */
@RestApi("lunchProviderApi_v1")
class LunchProviderApi {

  @GetMapping(URL_LUNCHPROVIDER)
  fun getAll(): List<LunchProviderDTO> =
    LunchProvider.values()
      .map { it.toDTOv1() }

  @GetMapping("$URL_LUNCHPROVIDER/{id}")
  fun getById(@PathVariable id: LunchProviderId): LunchProviderDTO =
    LunchProvider.values()
      .find { id == it.id }?.toDTOv1()
      ?: throw HttpNotFoundException("Mittagsanbieter mit ID $id nicht gefunden!")
}

/**
 * DTO für Mittagsanbieter.
 */
class LunchProviderDTO(
  val id: LunchProviderId,
  val name: String,
  val location: String,
)

fun LunchProvider.toDTOv1() = LunchProviderDTO(
  id,
  label,
  location.label,
)
