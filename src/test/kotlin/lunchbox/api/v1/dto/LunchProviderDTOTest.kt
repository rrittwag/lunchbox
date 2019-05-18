package lunchbox.api.v1.dto

import com.fasterxml.jackson.databind.ObjectMapper
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@JsonTest
@ExtendWith(SpringExtension::class)
class LunchProviderDTOTest(
  @Autowired val mapper: ObjectMapper
) {

  @Test
  fun serialize() {
    val dto = SCHWEINESTALL.toDTOv1()

    val result = mapper.writeValueAsString(dto)

    assertEquals(SCHWEINESTALL_AS_JSON, result, true)
  }
}

const val SCHWEINESTALL_AS_JSON = """
    {
      id: 1,
      name: "Schweinestall",
      location: "Neubrandenburg"
    }
"""
