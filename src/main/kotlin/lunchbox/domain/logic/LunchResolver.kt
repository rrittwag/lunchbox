package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer
import java.util.concurrent.Future

interface LunchResolver {
  fun resolve(): Future<List<LunchOffer>>
}
