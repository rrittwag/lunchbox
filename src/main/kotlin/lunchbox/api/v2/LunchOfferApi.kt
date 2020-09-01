package lunchbox.api.v2

import java.time.LocalDate
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchOfferId
import lunchbox.domain.models.LunchProviderId
import lunchbox.repository.LunchOfferRepository
import lunchbox.util.api.HttpNotFoundException
import lunchbox.util.api.RestApi
import org.joda.money.Money
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

/**
 * REST API-Controller für Mittagsangebote.
 */
@RestApi
class LunchOfferApi(val repo: LunchOfferRepository) {

  @GetMapping(URL_LUNCHOFFER)
  fun getAll(
    @RequestParam @DateTimeFormat(iso = ISO.DATE) day: LocalDate?
  ): List<LunchOfferDTO> = when (day) {
    null -> repo.findAll()
    else -> repo.findByDay(day)
  }.map { it.toDTOv2() }

  @GetMapping("$URL_LUNCHOFFER/{id}")
  fun getById(@PathVariable id: Long): LunchOfferDTO =
    repo.findByIdOrNull(id)?.toDTOv2()
      ?: throw HttpNotFoundException("Mittagsangebot mit ID $id nicht gefunden!")
}

/**
 * DTO für Mittagsangebot.
 */
data class LunchOfferDTO(
  val id: LunchOfferId,
  val name: String,
  val description: String,
  val day: LocalDate,
  val price: Money?,
  val tags: Set<String>,
  val provider: LunchProviderId
)

fun LunchOffer.toDTOv2() = LunchOfferDTO(
  id,
  name,
  description,
  day,
  price,
  tags,
  provider
)
