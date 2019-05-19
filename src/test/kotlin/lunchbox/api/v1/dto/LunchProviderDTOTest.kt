package lunchbox.api.v1.dto

import com.fasterxml.jackson.databind.ObjectMapper
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest

@JsonTest
class LunchProviderDTOTest(
  @Autowired val mapper: ObjectMapper
) {

  @Test
  fun serialize() {
    val dto = SCHWEINESTALL.toDTOv1()

    val result = mapper.writeValueAsString(dto)

    assertThatJson(result).isEqualTo(SCHWEINESTALL_AS_JSON)
  }
}

const val SCHWEINESTALL_AS_JSON = """
    {
      "id": 1,
      "name": "Schweinestall",
      "location": "Neubrandenburg"
    }
"""
