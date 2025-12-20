package lunchbox.api.v1

import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.test.web.servlet.client.assertj.RestTestClientResponse

@WebMvcTest(LunchProviderApi::class)
@AutoConfigureRestTestClient
class LunchProviderApiTest(
  @Autowired val restClient: RestTestClient,
) {
  @Nested
  inner class GetAll {
    @Test
    fun success() {
      val spec = restClient.get().uri(URL_LUNCHPROVIDER).exchange()

      val response = RestTestClientResponse.from(spec)
      assertThat(response).hasStatusOk()
      assertThat(response).hasContentTypeCompatibleWith(APPLICATION_JSON)
      spec.expectBody().apply {
        jsonPath("$").isArray()
        jsonPath("$.length()").isEqualTo("${LunchProvider.entries.size}")
        jsonPath("$[?(@.id == '${SCHWEINESTALL.id}')]").exists()
        jsonPath("$[?(@.id == '${SALT_N_PEPPER.id}')]").exists()
      }
    }
  }

  @Nested
  inner class GetOne {
    @Test
    fun `WHEN get schweinestall  THEN success`() {
      val spec = restClient.get().uri("$URL_LUNCHPROVIDER/${SCHWEINESTALL.id}").exchange()

      val response = RestTestClientResponse.from(spec)
      assertThat(response).hasStatusOk()
      assertThat(response).hasContentTypeCompatibleWith(APPLICATION_JSON)
      spec.expectBody().apply {
        jsonPath("$.id").isEqualTo(SCHWEINESTALL.id)
        jsonPath("$.name").isEqualTo(SCHWEINESTALL.label)
      }
    }

    @Test
    fun `WHEN get unknown  THEN not found`() {
      val spec = restClient.get().uri("$URL_LUNCHPROVIDER/404").exchange()

      val response = RestTestClientResponse.from(spec)
      assertThat(response).hasStatus(NOT_FOUND)
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
