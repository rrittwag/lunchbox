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

@RestController
class LunchOfferController(val repo: LunchOfferRepository) {

  @GetMapping(URL_LUNCHOFFER)
  fun getAll(
    @DateTimeFormat(iso = ISO.DATE) @RequestParam("day") day: LocalDate?
  ): List<LunchOfferDTO> {
    val offers =
      if (day != null) repo.findByDay(day)
      else repo.findAll()
    return offers.map { LunchOfferDTO.of(it) }
  }

  @GetMapping("$URL_LUNCHOFFER/{id}")
  fun get(@PathVariable("id") id: Long): LunchOfferDTO {
    val offer = repo.findByIdOrNull(id)
      ?: throw HttpNotFoundException("Mittagsangebot mit ID $id nicht gefunden!")
    return LunchOfferDTO.of(offer)
  }
}
