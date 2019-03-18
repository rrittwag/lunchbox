package lunchbox.api.v1.dto

import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProviderId

/**
 * API-DTO für Mittagsanbieter.
 */
class LunchProviderDTO(
  val id: LunchProviderId,
  val name: String,
  val location: String
) {

  companion object {
    fun of(provider: LunchProvider) =
      LunchProviderDTO(
        provider.id,
        provider.label,
        provider.location.label
      )
  }
}
