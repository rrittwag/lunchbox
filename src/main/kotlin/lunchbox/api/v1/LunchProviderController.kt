package lunchbox.api.v1

import lunchbox.api.v1.dto.LunchProviderDTO
import lunchbox.domain.models.LunchProvider
import lunchbox.util.exceptions.HttpNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PathVariable

/**
 * REST API-Controller für Mittagsanbieter.
 */
@RestController
class LunchProviderController {

  @GetMapping(URL_LUNCHPROVIDER)
  fun getAll(): List<LunchProviderDTO> =
    LunchProvider.values()
      .map { LunchProviderDTO.of(it) }

  @GetMapping("$URL_LUNCHPROVIDER/{id}")
  fun get(@PathVariable("id") id: Long): LunchProviderDTO {
    val provider =
      LunchProvider.values()
        .find { id == it.id }
          ?: throw HttpNotFoundException("Mittagsanbieter mit ID $id nicht gefunden!")
    return LunchProviderDTO.of(provider)
  }
}
