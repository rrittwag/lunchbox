package lunchbox.api.v1

import lunchbox.api.v1.dto.LunchOfferDTO
import lunchbox.util.exceptions.HttpNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import lunchbox.repository.LunchOfferRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

/**
 * REST API-Controller für Mittagsangebote.
 */
@RestController
class LunchOfferController(val repo: LunchOfferRepository) {

  @GetMapping(URL_LUNCHOFFER)
  fun getAll(
    @RequestParam @DateTimeFormat(iso = ISO.DATE) day: LocalDate?
  ): List<LunchOfferDTO> = when (day) {
    null -> repo.findAll()
    else -> repo.findByDay(day)
  }.map { LunchOfferDTO.of(it) }

  @GetMapping("$URL_LUNCHOFFER/{id}")
  fun getById(@PathVariable id: Long): LunchOfferDTO =
    repo.findByIdOrNull(id)?.let { LunchOfferDTO.of(it) }
      ?: throw HttpNotFoundException("Mittagsangebot mit ID $id nicht gefunden!")
}
