package lunchbox.api.v1
/*
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status */
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
// import org.mockito.Mockito.times
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import lunchbox.repository.LunchOfferRepository
import com.fasterxml.jackson.databind.ObjectMapper
// import org.mockito.ArgumentMatchers.any
// import org.mockito.ArgumentMatchers.notNull
// import org.mockito.Mockito.never
import lunchbox.domain.models.LunchOffer
import org.assertj.core.api.Assertions.assertThat

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
  fun `WHEN http GET lunchOffer  THEN return all offers`() {
/*    Mockito.`when`(repo.findAll())
        .thenReturn(listOf(PLAN_XMAS, PLAN_NEWYEAR))

    val httpCall = mockMvc.perform(get(URL_DAILYPLAN))

    httpCall.andExpect(status().isOk)
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray)
        .andExpect(jsonPath("$.length()").value("2"))
        .andExpect(jsonPath("$[?(@.day == '$DATE_XMAS')]").exists())
        .andExpect(jsonPath("$[?(@.day == '$DATE_NEWYEAR')]").exists()) */

    assertThat(true).isEqualTo(false)
  }

  private fun toJson(offer: LunchOffer): String {
    try {
      return objectMapper.writeValueAsString(offer)
    } catch (e: Exception) {
      throw RuntimeException(e)
    }
  }
}
