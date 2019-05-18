package lunchbox.api.v1

import lunchbox.api.v1.dto.LunchProviderDTO
import lunchbox.api.v1.dto.toDTOv1
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
      .map { it.toDTOv1() }

  @GetMapping("$URL_LUNCHPROVIDER/{id}")
  fun getById(@PathVariable id: Long): LunchProviderDTO =
    LunchProvider.values()
      .find { id == it.id }?.toDTOv1()
        ?: throw HttpNotFoundException("Mittagsanbieter mit ID $id nicht gefunden!")
}
