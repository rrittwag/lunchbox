/* ktlint-disable max-line-length */
@file:Suppress("MaxLineLength")

package lunchbox.domain.models

import lunchbox.domain.models.LunchLocation.BERLIN_SPRINGPFUHL
import lunchbox.domain.models.LunchLocation.NEUBRANDENBURG
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
  SCHWEINESTALL(1, "Schweinestall", NEUBRANDENBURG, URL("https://www.schweinestall-nb.de/mittagstisch-2/")),
  HOTEL_AM_RING(2, "Hotel am Ring", NEUBRANDENBURG, URL("http://www.hotel-am-ring.de/restaurant-rethra.html"), false),
  AOK_CAFETERIA(3, "AOK Cafeteria", NEUBRANDENBURG, URL("https://www.tfa-catering.de/")),
  SUPPENKULTTOUR(4, "Suppenkulttour", NEUBRANDENBURG, URL("https://www.suppenkult.com/wochenplan.html")),
  SALT_N_PEPPER(
    5,
    "Salt 'n' Pepper",
    BERLIN_SPRINGPFUHL,
    URL("https://www.partyservice-rohde.de/bistro-angebot-der-woche")
  ),
  GESUNDHEITSZENTRUM(
    6,
    "Gesundheitszentrum",
    BERLIN_SPRINGPFUHL,
    URL("https://de-de.facebook.com/pages/Kantine-BQuakatz-Allee-der-Kosmonauten/181190361991823"),
    false
  ),
  FELDKUECHE(7, "Feldküche Karow", BERLIN_SPRINGPFUHL, URL("https://www.feldkuechebkarow.de/speiseplan")),
  DAS_KRAUTHOF(8, "Das Krauthof", NEUBRANDENBURG, URL("https://www.daskrauthof.de/karte"), false),
  TABBOULEH(9, "Tabbouleh", BERLIN_SPRINGPFUHL, URL("https://www.restaurant-tabbouleh.de/menu"), false),
  PHOENIXEUM(10, "Phoenixeum", NEUBRANDENBURG, URL("https://www.suppenkult.com/wochenplan.html"))
}
