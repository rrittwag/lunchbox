package lunchbox.api.v1.dto

import com.fasterxml.jackson.databind.ObjectMapper
import lunchbox.domain.models.GYROS
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@JsonTest
@ExtendWith(SpringExtension::class)
class LunchOfferDTOTest {

  @Autowired
  lateinit var mapper: ObjectMapper

  @Test
  fun serialize() {
    val dto = LunchOfferDTO.of(GYROS)

    val result = mapper.writeValueAsString(dto)

    assertEquals(GYROS_AS_JSON, result, true)
  }
}

const val GYROS_AS_JSON = """
    {
      id: 0,
      name: "Gyros",
      day: "2019-01-01",
      price: 580,
      provider: 1
    }
"""
