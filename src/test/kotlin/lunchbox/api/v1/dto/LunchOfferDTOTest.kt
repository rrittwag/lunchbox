package lunchbox.api.v1.dto

import com.fasterxml.jackson.databind.ObjectMapper
import lunchbox.domain.models.GYROS
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest

@JsonTest
class LunchOfferDTOTest(
  @Autowired val mapper: ObjectMapper
) {

  @Test
  fun serialize() {
    val dto = GYROS.toDTOv1()

    val result = mapper.writeValueAsString(dto)

    assertEquals(GYROS_AS_JSON, result, JSONCompareMode.STRICT)
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
