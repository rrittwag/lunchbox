package lunchbox.repository

import lunchbox.domain.models.LunchOffer
import org.springframework.stereotype.Repository

@Repository
class LunchOfferRepositoryImplInMemory : LunchOfferRepository {

  override fun findAll(): List<LunchOffer> {
    return listOf()
  }
}
