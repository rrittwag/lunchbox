package lunchbox.api.v1 /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(LunchProviderController::class)
class LunchProviderControllerTest(
  @Autowired val mockMvc: MockMvc
) {

  @Nested
  inner class GetAll {
    @Test
    fun success() {
      mockMvc.get(URL_LUNCHPROVIDER)

      .andExpect {
        status { isOk }
        content { contentTypeCompatibleWith(APPLICATION_JSON) }
        jsonPath("$") { isArray }
        jsonPath("$.length()") { value("${LunchProvider.values().size}") }
        jsonPath("$[?(@.id == '${SCHWEINESTALL.id}')]") { exists() }
        jsonPath("$[?(@.id == '${SALT_N_PEPPER.id}')]") { exists() }
      }
    }
  }

  @Nested
  inner class GetOne {
    @Test
    fun `WHEN get schweinestall  THEN success`() {
      mockMvc.get("$URL_LUNCHPROVIDER/${SCHWEINESTALL.id}")

      .andExpect {
        status { isOk }
        content { contentTypeCompatibleWith(APPLICATION_JSON) }
        jsonPath("$.id") { value(SCHWEINESTALL.id) }
        jsonPath("$.name") { value(SCHWEINESTALL.label) }
      }
    }

    @Test
    fun `WHEN get unknown  THEN not found`() {
      mockMvc.get("$URL_LUNCHPROVIDER/404")

      .andExpect {
        status { isNotFound }
      }
    }
  }
}
