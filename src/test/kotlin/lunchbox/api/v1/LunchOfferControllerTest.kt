package lunchbox.api.v1 /* ktlint-disable max-line-length no-wildcard-imports */

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Called
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import lunchbox.repository.LunchOfferRepository
import lunchbox.domain.models.GYROS
import lunchbox.domain.models.SOLJANKA

@WebMvcTest(LunchOfferController::class)
class LunchOfferControllerTest(
  @Autowired val mockMvc: MockMvc
) {

  @MockkBean
  lateinit var repo: LunchOfferRepository

  @BeforeEach
  fun before() {
    clearAllMocks()
  }

  @Test
  fun `WHEN get all  THEN success`() {
    every { repo.findAll() } returns listOf(GYROS, SOLJANKA)

    val httpCall = mockMvc.perform(get(URL_LUNCHOFFER))

    httpCall.andExpect(status().isOk)
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray)
        .andExpect(jsonPath("$.length()").value("2"))
        .andExpect(jsonPath("$[?(@.day == '${GYROS.day}')]").exists())
        .andExpect(jsonPath("$[?(@.day == '${SOLJANKA.day}')]").exists())

    verify(exactly = 1) { repo.findAll() }
  }

  @Test
  fun `WHEN get all for day  THEN success`() {
    every { repo.findByDay(GYROS.day) } returns listOf(GYROS)

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER?day=${GYROS.day}"))

    httpCall.andExpect(status().isOk)
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$").isArray)
      .andExpect(jsonPath("$.length()").value("1"))
      .andExpect(jsonPath("$[?(@.day == '${GYROS.day}')]").exists())

    verify(exactly = 1) { repo.findByDay(GYROS.day) }
    verify(exactly = 0) { repo.findAll() }
  }

  @Test
  fun `WHEN get all with wrong day format  THEN 400`() {
    every { repo.findByDay(GYROS.day) } returns listOf(GYROS)

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER?day=xmas"))

    httpCall.andExpect(status().isBadRequest)
    verify { repo wasNot Called }
  }

  @Test
  fun `WHEN get gyros  THEN success`() {
    every { repo.findByIdOrNull(GYROS.id) } returns GYROS

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER/${GYROS.id}"))

    httpCall.andExpect(status().isOk)
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(GYROS.id))
      .andExpect(jsonPath("$.name").value(GYROS.name))

    verify(exactly = 1) { repo.findByIdOrNull(GYROS.id) }
  }

  @Test
  fun `WHEN get gyros  THEN not found`() {
    every { repo.findByIdOrNull(GYROS.id) } returns null

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER/${GYROS.id}"))

    httpCall.andExpect(status().isNotFound)
  }
}
