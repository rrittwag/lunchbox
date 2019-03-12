package lunchbox.api.v1

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import lunchbox.domain.models.LunchOffer
import lunchbox.repository.LunchOfferRepository

const val URL_API_PREFIX = "/api/v1"
const val URL_LUNCHOFFER = "$URL_API_PREFIX/lunchoffer"

@RestController
class LunchOfferController(val repo: LunchOfferRepository) {

  @GetMapping(URL_LUNCHOFFER)
  fun getLunchOffers(): List<LunchOffer> = repo.findAll()
}
