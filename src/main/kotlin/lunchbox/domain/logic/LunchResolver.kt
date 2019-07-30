package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider

/**
 * Schnittstelle für das Ermitteln von Mittagsangeboten.
 */
interface LunchResolver {
  val provider: LunchProvider

  fun resolve(): List<LunchOffer>
}
