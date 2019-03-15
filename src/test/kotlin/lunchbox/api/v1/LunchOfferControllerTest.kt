package lunchbox.api.v1 /* ktlint-disable max-line-length no-wildcard-imports */

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import lunchbox.repository.LunchOfferRepository
import lunchbox.domain.models.GYROS
import lunchbox.domain.models.SOLJANKA

@WebMvcTest(LunchOfferController::class)
class LunchOfferControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var repo: LunchOfferRepository

  @BeforeEach
  fun before() {
    Mockito.reset(repo)
  }

  @Test
  fun `WHEN get all  THEN success`() {
    Mockito.`when`(repo.findAll())
        .thenReturn(listOf(GYROS, SOLJANKA))

    val httpCall = mockMvc.perform(get(URL_LUNCHOFFER))

    httpCall.andExpect(status().isOk)
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray)
        .andExpect(jsonPath("$.length()").value("2"))
        .andExpect(jsonPath("$[?(@.day == '${GYROS.day}')]").exists())
        .andExpect(jsonPath("$[?(@.day == '${SOLJANKA.day}')]").exists())

    Mockito.verify(repo, times(1)).findAll()
  }

  @Test
  fun `WHEN get all for day  THEN success`() {
    Mockito.`when`(repo.findByDay(GYROS.day))
      .thenReturn(listOf(GYROS))

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER?day=${GYROS.day}"))

    httpCall.andExpect(status().isOk)
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$").isArray)
      .andExpect(jsonPath("$.length()").value("1"))
      .andExpect(jsonPath("$[?(@.day == '${GYROS.day}')]").exists())

    Mockito.verify(repo, times(1)).findByDay(GYROS.day)
    Mockito.verify(repo, never()).findAll()
  }

  @Test
  fun `WHEN get all with wrong day format  THEN 400`() {
    Mockito.`when`(repo.findByDay(GYROS.day))
      .thenReturn(listOf(GYROS))

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER?day=xmas"))

    httpCall.andExpect(status().isBadRequest)

    Mockito.verify(repo, never()).findByDay(GYROS.day)
    Mockito.verify(repo, never()).findAll()
  }

  @Test
  fun `WHEN get gyros  THEN success`() {
    Mockito.`when`(repo.findByIdOrNull(GYROS.id))
      .thenReturn(GYROS)

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER/${GYROS.id}"))

    httpCall.andExpect(status().isOk)
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(GYROS.id))
      .andExpect(jsonPath("$.name").value(GYROS.name))

    Mockito.verify(repo, times(1)).findByIdOrNull(GYROS.id)
  }

  @Test
  fun `WHEN get gyros  THEN not found`() {
    Mockito.`when`(repo.findByIdOrNull(GYROS.id))
      .thenReturn(null)

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER/${GYROS.id}"))

    httpCall.andExpect(status().isNotFound)
  }
}
