package lunchbox.domain.models

import lunchbox.domain.models.LunchLocation.NEUBRANDENBURG
import lunchbox.domain.models.LunchLocation.BERLIN_SPRINGPFUHL

typealias LunchProviderId = Long

enum class LunchProvider(
  val id: LunchProviderId,
  val label: String,
  val location: LunchLocation
) {
  SCHWEINESTALL(1, "Schweinestall", NEUBRANDENBURG),
  HOTEL_AM_RING(2, "Hotel am Ring", NEUBRANDENBURG),
  AOK_CAFETERIA(3, "AOK Cafeteria", NEUBRANDENBURG),
  SUPPENKULTTOUR(4, "Suppenkulttour", NEUBRANDENBURG),
  SALT_N_PEPPER(5, "Salt 'n' Pepper", BERLIN_SPRINGPFUHL),
  GESUNDHEITSZENTRUM(6, "Gesundheitszentrum", BERLIN_SPRINGPFUHL),
  FELDKUECHE(7, "Feldküche Karow", BERLIN_SPRINGPFUHL),
  DAS_KRAUTHOF(8, "Das Krauthof", NEUBRANDENBURG),
  TABBOULEH(9, "Tabbouleh", BERLIN_SPRINGPFUHL)
}
