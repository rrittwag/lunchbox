package lunchbox.api.v1

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchOfferId
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProviderId
import lunchbox.repository.LunchOfferRepository
import lunchbox.util.api.HttpNotFoundException
import lunchbox.util.api.RestApi
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.format.annotation.DateTimeFormat.ISO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import java.math.BigDecimal
import java.time.LocalDate

/**
 * REST API-Controller für Mittagsangebote.
 */
@RestApi("lunchOfferApi_v1")
class LunchOfferApi(
  val repo: LunchOfferRepository,
) {
  @GetMapping(URL_LUNCHOFFER)
  fun getAll(
    @RequestParam
    @DateTimeFormat(iso = ISO.DATE)
    day: LocalDate?,
  ): List<LunchOfferDTO> =
    when (day) {
      null -> repo.findAll()
      else -> repo.findByDay(day)
    }.map { it.toDTOv1() }

  @GetMapping("$URL_LUNCHOFFER/{id}")
  fun getById(
    @PathVariable id: LunchOfferId,
  ): LunchOfferDTO =
    repo.findByIdOrNull(id)?.toDTOv1()
      ?: throw HttpNotFoundException("Mittagsangebot mit ID $id nicht gefunden!")
}

/**
 * DTO für Mittagsangebot.
 */
data class LunchOfferDTO(
  val id: LunchOfferId,
  val name: String,
  val day: LocalDate,
  val price: Money,
  val provider: LunchProviderId,
)

fun LunchOffer.toDTOv1(): LunchOfferDTO {
  var name = this.name
  if (this.description.isNotEmpty()) {
    name +=
      when (this.provider) {
        LunchProvider.SUPPENKULTTOUR.id -> ": ${this.description}"
        else -> " ${this.description}"
      }
  }

  return LunchOfferDTO(
    id,
    name,
    day,
    price ?: Money.of(CurrencyUnit.EUR, BigDecimal.ZERO),
    provider,
  )
}
