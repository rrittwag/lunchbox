package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer

/**
 * Schnittstelle für das Ermitteln von Mittagsangeboten.
 */
interface LunchResolver {
  fun resolve(): List<LunchOffer>
}
