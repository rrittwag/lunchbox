package lunchbox.domain.service

import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import lunchbox.util.date.DateValidator
import lunchbox.domain.models.LunchProvider
import lunchbox.repository.LunchOfferRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LunchOfferUpdateTest {

  private val repo = mockk<LunchOfferRepository>()
  private val worker = mockk<LunchOfferUpdateWorker>()
  private val testUnit = LunchOfferUpdate(repo, worker)

  @BeforeEach
  fun beforeEach() {
    clearAllMocks()
  }

  @Test
  fun success() {
    every { repo.deleteBefore(DateValidator.mondayLastWeek()) } just Runs
    every { worker.refreshOffersOf(any()) } just Runs

    testUnit.updateOffers()

    verify(exactly = 1) { repo.deleteBefore(DateValidator.mondayLastWeek()) }
    for (provider in activeProviders)
      verify(exactly = 1) { worker.refreshOffersOf(provider) }
  }

  // --- mocks 'n' stuff

  private val activeProviders = LunchProvider.values().filter { it.active }
}
