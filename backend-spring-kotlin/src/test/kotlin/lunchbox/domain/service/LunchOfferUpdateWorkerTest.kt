@file:Suppress("SameParameterValue")

package lunchbox.domain.service /* ktlint-disable max-line-length */

import io.mockk.Called
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifySequence
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.domain.models.createOffer
import lunchbox.domain.resolvers.LunchResolver
import lunchbox.repository.LunchOfferRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.RuntimeException
import java.time.LocalDate

class LunchOfferUpdateWorkerTest {

  private val repo = mockk<LunchOfferRepository>()
  private val resolvers = mutableListOf<LunchResolver>()
  private val testUnit = LunchOfferUpdateWorker(repo, resolvers)

  @BeforeEach
  fun beforeEach() {
    clearAllMocks()
    resolvers.clear()
  }

  @Test
  fun success() {
    val offers = listOf(offerYesterday, offerToday)
    mockResolver(SCHWEINESTALL, offers)
    every { repo.deleteFrom(yesterday, SCHWEINESTALL.id) } just Runs
    every { repo.saveAll(offers) } returns offers

    testUnit.refreshOffersOf(SCHWEINESTALL)

    verifySequence {
      repo.deleteFrom(yesterday, SCHWEINESTALL.id)
      repo.saveAll(offers)
    }
  }

  @Test
  fun `only call resolver of provider`() {
    val resolverAok = mockResolver(AOK_CAFETERIA)
    val resolverSuppe = mockResolver(SUPPENKULTTOUR)
    val resolverSchweinestall = mockResolver(SCHWEINESTALL)

    testUnit.refreshOffersOf(SCHWEINESTALL)

    verify(exactly = 0) { resolverAok.resolve() }
    verify(exactly = 0) { resolverSuppe.resolve() }
    verify(exactly = 1) { resolverSchweinestall.resolve() }
  }

  @Test
  fun `WHEN no resolver  THEN no repo call`() {
    testUnit.refreshOffersOf(SCHWEINESTALL)

    verify { repo wasNot Called }
  }

  @Test
  fun `WHEN resolver throws exception  THEN no repo call`() {
    mockResolverThrowingException(SCHWEINESTALL)

    testUnit.refreshOffersOf(SCHWEINESTALL)

    verify { repo wasNot Called }
  }

  // --- mocks 'n' stuff ---

  private fun mockResolver(
    provider: LunchProvider,
    offers: List<LunchOffer> = emptyList()
  ): LunchResolver {
    val resolver = mockk<LunchResolver>()
    every { resolver.provider } returns provider
    every { resolver.resolve() } returns offers
    resolvers += resolver
    return resolver
  }

  private fun mockResolverThrowingException(provider: LunchProvider): LunchResolver {
    val resolver = mockk<LunchResolver>()
    every { resolver.provider } returns provider
    every { resolver.resolve() } throws RuntimeException("")
    resolvers += resolver
    return resolver
  }

  private val today = LocalDate.now()

  private val yesterday = today.minusDays(1)

  private val offerYesterday = createOffer(
    provider = SCHWEINESTALL.id,
    day = yesterday
  )

  private val offerToday = createOffer(
    provider = SCHWEINESTALL.id,
    day = today
  )
}
