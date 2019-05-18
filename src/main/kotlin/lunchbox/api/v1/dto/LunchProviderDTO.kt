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
)

fun LunchProvider.toDTOv1() = LunchProviderDTO(
  id,
  label,
  location.label
)
