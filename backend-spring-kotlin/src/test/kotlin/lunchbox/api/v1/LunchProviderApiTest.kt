package lunchbox.api.v1

import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@WebMvcTest(LunchProviderApi::class)
class LunchProviderApiTest(
  @Autowired val mockMvc: MockMvc,
) {
  @Nested
  inner class GetAll {
    @Test
    fun success() {
      mockMvc.get(URL_LUNCHPROVIDER)
        .andExpect {
          status { isOk() }
          content { contentTypeCompatibleWith(APPLICATION_JSON) }
          jsonPath("$") { isArray() }
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
          status { isOk() }
          content { contentTypeCompatibleWith(APPLICATION_JSON) }
          jsonPath("$.id") { value(SCHWEINESTALL.id) }
          jsonPath("$.name") { value(SCHWEINESTALL.label) }
        }
    }

    @Test
    fun `WHEN get unknown  THEN not found`() {
      mockMvc.get("$URL_LUNCHPROVIDER/404")
        .andExpect {
          status { isNotFound() }
        }
    }
  }
}

// ------
//  DTOs
// ------

@JsonTest
class LunchProviderDTOTest(
  @Autowired val json: JacksonTester<LunchProviderDTO>,
) {
  @Test
  fun `convert DTO to JSON`() {
    assertThat(json.write(SCHWEINESTALL_AS_DTO)).isEqualTo(SCHWEINESTALL_AS_JSON)
  }
}

val SCHWEINESTALL_AS_DTO = SCHWEINESTALL.toDTOv1()

const val SCHWEINESTALL_AS_JSON = """
    {
      "id": 1,
      "name": "Schweinestall",
      "location": "Neubrandenburg"
    }
"""
