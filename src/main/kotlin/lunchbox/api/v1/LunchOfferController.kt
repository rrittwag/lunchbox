package lunchbox.api.v1

import lunchbox.api.v1.dto.LunchOfferDTO
import lunchbox.util.exceptions.HttpNotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import lunchbox.repository.LunchOfferRepository
import org.springframework.web.bind.annotation.PathVariable

@RestController
class LunchOfferController(val repo: LunchOfferRepository) {

  @GetMapping(URL_LUNCHOFFER)
  fun getLunchOffers(): List<LunchOfferDTO> =
    repo
      .findAll()
      .map { LunchOfferDTO.of(it) }

  @GetMapping("$URL_LUNCHOFFER/{id}")
  fun getLunchOffer(@PathVariable("id") id: Long): LunchOfferDTO {
    val offer = repo.findByIdOrNull(id)
      ?: throw HttpNotFoundException("Mittagsangebot mit ID $id nicht gefunden!")
    return LunchOfferDTO.of(offer)
  }
}
