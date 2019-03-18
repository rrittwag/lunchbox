package lunchbox.domain.models /* ktlint-disable max-line-length */

import lunchbox.domain.models.LunchLocation.NEUBRANDENBURG
import lunchbox.domain.models.LunchLocation.BERLIN_SPRINGPFUHL
import java.net.URL

typealias LunchProviderId = Long

/**
 * Auflistung der Mittagsanbieter.
 */
enum class LunchProvider(
  val id: LunchProviderId,
  val label: String,
  val location: LunchLocation,
  val menuUrl: URL,
  val active: Boolean = true
) {
  SCHWEINESTALL(1, "Schweinestall", NEUBRANDENBURG, URL("http://www.schweinestall-nb.de/index.php?id=159")),
  HOTEL_AM_RING(2, "Hotel am Ring", NEUBRANDENBURG, URL("http://example.com"), false),
  AOK_CAFETERIA(3, "AOK Cafeteria", NEUBRANDENBURG, URL("http://example.com")),
  SUPPENKULTTOUR(4, "Suppenkulttour", NEUBRANDENBURG, URL("http://example.com")),
  SALT_N_PEPPER(5, "Salt 'n' Pepper", BERLIN_SPRINGPFUHL, URL("http://example.com")),
  GESUNDHEITSZENTRUM(6, "Gesundheitszentrum", BERLIN_SPRINGPFUHL, URL("http://example.com")),
  FELDKUECHE(7, "Feldküche Karow", BERLIN_SPRINGPFUHL, URL("http://example.com")),
  DAS_KRAUTHOF(8, "Das Krauthof", NEUBRANDENBURG, URL("http://example.com")),
  TABBOULEH(9, "Tabbouleh", BERLIN_SPRINGPFUHL, URL("http://example.com"))
}
