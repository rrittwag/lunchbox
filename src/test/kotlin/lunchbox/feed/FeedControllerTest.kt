package lunchbox.feed /* ktlint-disable max-line-length no-wildcard-imports */

import com.ninjasquad.springmockk.MockkBean
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import lunchbox.domain.models.LunchLocation.NEUBRANDENBURG
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.domain.models.createOffer
import lunchbox.repository.LunchOfferRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.LocalDate

@WebMvcTest(FeedController::class)
@Suppress("UsePropertyAccessSyntax")
class FeedControllerTest(
  @Autowired val mockMvc: MockMvc,
  @Autowired val testUnit: FeedController
) {

  @MockkBean
  lateinit var repo: LunchOfferRepository

  @BeforeEach
  fun before() {
    clearAllMocks()
  }

  @Nested
  inner class HttpCall {
    @Test
    fun success() {
      every { repo.findAll() } returns listOf(offerYesterday, offerToday)

      val httpCall = mockMvc.perform(get("$URL_FEED?location=${NEUBRANDENBURG.label}"))

      httpCall.andExpect(status().isOk)
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_ATOM_XML))
        .andExpect(xpath("/feed").exists())
        .andExpect(xpath("/feed/entry").nodeCount(2))

      verify(exactly = 1) { repo.findAll() }
    }

    @Test
    fun `WHEN param location is missing  THEN return 400`() {
      every { repo.findAll() } returns listOf(offerYesterday, offerToday)

      val httpCall = mockMvc.perform(get(URL_FEED))

      httpCall.andExpect(status().isBadRequest)
    }
  }

  @Nested
  inner class CreateFeed {
    @Test
    fun success() {
      every { repo.findAll() } returns listOf(offerToday)

      val result = testUnit.createFeed(NEUBRANDENBURG, "http://example.com/feed")

      assertThat(result.id).isNotEmpty()
      assertThat(result.title).isEqualTo("Mittagstisch Neubrandenburg")
      assertThat(result.alternateLinks).hasSize(1)
      assertThat(result.alternateLinks[0].rel).isEqualTo("self")
      assertThat(result.alternateLinks[0].href).isEqualTo("http://example.com/feed")
      assertThat(result.authors).hasSize(1)
      assertThat(result.authors[0].name).isEqualTo("Lunchbox")
      assertThat(result.updated).isNotNull()

      assertThat(result.entries).hasSize(1)
      assertThat(result.entries[0].id).isEqualTo("urn:date:${offerToday.day}")
      assertThat(result.entries[0].title).isNotEmpty()
      assertThat(result.entries[0].authors).hasSize(1)
      assertThat(result.entries[0].updated).isNotNull()
      assertThat(result.entries[0].published).isNotNull()
      assertThat(result.entries[0].contents).hasSize(1)

      verify(exactly = 1) { repo.findAll() }
    }

    @Test
    fun `create feed entry per day sorted descending`() {
      every { repo.findAll() } returns listOf(offerYesterday, offerToday)

      val result = testUnit.createFeed(NEUBRANDENBURG, "http://link")

      assertThat(result.entries).hasSize(2)
      assertThat(result.entries[0].id).isEqualTo("urn:date:${offerToday.day}")
      assertThat(result.entries[1].id).isEqualTo("urn:date:${offerYesterday.day}")
      verify(exactly = 1) { repo.findAll() }
    }

    @Test
    fun `WHEN no offers  THEN feed is empty`() {
      every { repo.findAll() } returns emptyList()

      val result = testUnit.createFeed(NEUBRANDENBURG, "http://link")

      assertThat(result.entries).isEmpty()
      verify(exactly = 1) { repo.findAll() }
    }

    @Test
    fun `WHEN all offers are after today  THEN feed is empty`() {
      every { repo.findAll() } returns listOf(offerTomorrow)

      val result = testUnit.createFeed(NEUBRANDENBURG, "http://link")

      assertThat(result.entries).isEmpty()
      verify(exactly = 1) { repo.findAll() }
    }

    @Test
    fun `WHEN all offers in other location  THEN feed is empty`() {
      every { repo.findAll() } returns listOf(offerBerlin)

      val result = testUnit.createFeed(NEUBRANDENBURG, "http://link")

      assertThat(result.entries).isEmpty()
      verify(exactly = 1) { repo.findAll() }
    }
  }

  // --- mocks 'n' stuff

  private val today = LocalDate.now()

  private val tomorrow = today.plusDays(1)

  private val yesterday = today.minusDays(1)

  private val offerYesterday = createOffer(
    provider = SUPPENKULTTOUR.id,
    day = yesterday
  )

  private val offerToday = createOffer(
    provider = AOK_CAFETERIA.id,
    day = today
  )

  private val offerTomorrow = createOffer(
    provider = AOK_CAFETERIA.id,
    day = tomorrow
  )

  private val offerBerlin = createOffer(
    provider = SALT_N_PEPPER.id,
    day = today
  )
}
