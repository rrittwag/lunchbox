package lunchbox.api.v1 /* ktlint-disable max-line-length no-wildcard-imports */

import com.ninjasquad.springmockk.MockkBean
import io.mockk.Called
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.verify
import lunchbox.domain.models.GYROS
import lunchbox.domain.models.SOLJANKA
import lunchbox.repository.LunchOfferRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

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

  @Nested
  inner class GetAll {
    @Test
    fun success() {
      every { repo.findAll() } returns listOf(GYROS, SOLJANKA)

      mockMvc.get(URL_LUNCHOFFER)

      .andExpect {
        status { isOk }
        content { contentTypeCompatibleWith(APPLICATION_JSON) }
        jsonPath("$") { isArray }
        jsonPath("$.length()") { value("2") }
        jsonPath("$[?(@.day == '${GYROS.day}')]") { exists() }
        jsonPath("$[?(@.day == '${SOLJANKA.day}')]") { exists() }
      }

      verify(exactly = 1) { repo.findAll() }
    }
  }

  @Nested
  inner class GetAllByDay {
    @Test
    fun success() {
      every { repo.findByDay(GYROS.day) } returns listOf(GYROS)

      mockMvc.get("$URL_LUNCHOFFER?day=${GYROS.day}")

      .andExpect {
        status { isOk }
        content { contentTypeCompatibleWith(APPLICATION_JSON) }
        jsonPath("$") { isArray }
        jsonPath("$.length()") { value("1") }
        jsonPath("$[?(@.day == '${GYROS.day}')]") { exists() }
      }

      verify(exactly = 1) { repo.findByDay(GYROS.day) }
      verify(exactly = 0) { repo.findAll() }
    }

    @Test
    fun `WHEN wrong day format  THEN 400`() {
      every { repo.findByDay(GYROS.day) } returns listOf(GYROS)

      mockMvc.get("$URL_LUNCHOFFER?day=xmas")

      .andExpect {
        status { isBadRequest }
      }

      verify { repo wasNot Called }
    }
  }

  @Nested
  inner class GetOne {
    @Test
    fun success() {
      every { repo.findByIdOrNull(GYROS.id) } returns GYROS

      mockMvc.get("$URL_LUNCHOFFER/${GYROS.id}")

      .andExpect {
        status { isOk }
        content { contentTypeCompatibleWith(APPLICATION_JSON) }
        jsonPath("$.id") { value(GYROS.id) }
        jsonPath("$.name") { value(GYROS.name) }
      }

      verify(exactly = 1) { repo.findByIdOrNull(GYROS.id) }
    }

    @Test
    fun `not found`() {
      every { repo.findByIdOrNull(GYROS.id) } returns null

      mockMvc.get("$URL_LUNCHOFFER/${GYROS.id}")

      .andExpect {
        status { isNotFound }
      }
    }
  }
}
