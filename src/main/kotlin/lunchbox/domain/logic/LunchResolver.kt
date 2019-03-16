package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer

interface LunchResolver {
  fun resolve(): List<LunchOffer>
}
