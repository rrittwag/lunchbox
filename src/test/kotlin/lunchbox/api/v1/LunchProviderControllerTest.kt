package lunchbox.api.v1 /* ktlint-disable max-line-length no-wildcard-imports */

import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.domain.models.LunchProvider.TABBOULEH

@WebMvcTest(LunchProviderController::class)
class LunchProviderControllerTest {

  @Autowired
  lateinit var mockMvc: MockMvc

  @Test
  fun `WHEN get all  THEN success`() {
    val httpCall = mockMvc.perform(get(URL_LUNCHPROVIDER))

    httpCall.andExpect(status().isOk)
        .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
        .andExpect(jsonPath("$").isArray)
        .andExpect(jsonPath("$.length()").value("${LunchProvider.values().size}"))
        .andExpect(jsonPath("$[?(@.id == '${SCHWEINESTALL.id}')]").exists())
        .andExpect(jsonPath("$[?(@.id == '${TABBOULEH.id}')]").exists())
  }

  @Test
  fun `WHEN get schweinestall  THEN success`() {
    val httpCall = mockMvc.perform(get("$URL_LUNCHPROVIDER/${SCHWEINESTALL.id}"))

    httpCall.andExpect(status().isOk)
      .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
      .andExpect(jsonPath("$.id").value(SCHWEINESTALL.id))
      .andExpect(jsonPath("$.name").value(SCHWEINESTALL.label))
  }

  @Test
  fun `WHEN get unknown  THEN not found`() {
    val httpCall = mockMvc.perform(get("$URL_LUNCHOFFER/404"))

    httpCall.andExpect(status().isNotFound)
  }
}
