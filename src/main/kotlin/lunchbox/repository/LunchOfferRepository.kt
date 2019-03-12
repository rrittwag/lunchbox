package lunchbox.repository

import lunchbox.domain.models.LunchOffer

interface LunchOfferRepository {
  fun findAll(): List<LunchOffer>
}
