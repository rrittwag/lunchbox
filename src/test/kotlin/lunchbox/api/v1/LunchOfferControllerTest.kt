package lunchbox.api.v1

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.Mockito.anyLong
import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import lunchbox.repository.LunchOfferRepository
import com.fasterxml.jackson.databind.ObjectMapper
import lunchbox.domain.models.DATE_NEWYEAR
import lunchbox.domain.models.DATE_XMAS
import lunchbox.domain.models.GYROS
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.SOLJANKA

@WebMvcTest(LunchOfferController::class)
class LunchOfferControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @MockBean
  lateinit var repo: LunchOfferRepository

  @Autowired
  lateinit var objectMapper: ObjectMapper

  @BeforeEach
  fun before() {
    Mockito.reset(repo)
  }

  @Test
  fun `WHEN http GET lunchOffer  THEN success`() {
    Mockito.`when`(repo.findAll())
        .thenReturn(listOf(GYROS, SOLJANKA))

    val httpCall = mockMvc.perform(get(URL_LUNCHOFFER))

    httpCall.andExpect(status().isOk)
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray)
        .andExpect(jsonPath("$.length()").value("2"))
        .andExpect(jsonPath("$[?(@.day == '$DATE_XMAS')]").exists())
        .andExpect(jsonPath("$[?(@.day == '$DATE_NEWYEAR')]").exists())

    Mockito.verify(repo, times(1)).findAll()
  }

  @Test
  fun `WHEN http GET gyros  THEN success`() {
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
  fun `WHEN http GET gyros  THEN not found`() {
    Mockito.`when`(repo.findByIdOrNull(anyLong()))
      .thenReturn(null)

    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER/${GYROS.id}"))

    httpCall.andExpect(status().isNotFound)
  }

  private fun toJson(offer: LunchOffer): String {
    try {
      return objectMapper.writeValueAsString(offer)
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}
