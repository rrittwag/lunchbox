package lunchbox.api.v1.dto

import lunchbox.domain.models.GYROS
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class LunchOfferDTOTest(
  @Autowired val json: JacksonTester<LunchOfferDTO>
) {

  @Test
  fun serialize() {
    val dto = GYROS.toDTOv1()

    assertThat(json.write(dto)).isEqualTo(GYROS_AS_JSON)
  }
}

const val GYROS_AS_JSON = """
    {
      "id": 0,
      "name": "Gyros",
      "day": "2019-01-01",
      "price": 580,
      "provider": 1
    }
"""
