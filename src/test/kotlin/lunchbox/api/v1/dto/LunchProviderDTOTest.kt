package lunchbox.api.v1.dto

import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class LunchProviderDTOTest(
  @Autowired val json: JacksonTester<LunchProviderDTO>
) {

  @Test
  fun serialize() {
    val dto = SCHWEINESTALL.toDTOv1()

    assertThat(json.write(dto)).isEqualTo(SCHWEINESTALL_AS_JSON)
  }
}

const val SCHWEINESTALL_AS_JSON = """
    {
      "id": 1,
      "name": "Schweinestall",
      "location": "Neubrandenburg"
    }
"""
