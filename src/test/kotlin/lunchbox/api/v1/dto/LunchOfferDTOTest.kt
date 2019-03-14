package lunchbox.api.v1.dto

import com.fasterxml.jackson.databind.ObjectMapper
import lunchbox.domain.models.GYROS
import lunchbox.domain.models.GYROS_AS_JSON
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
    val json = mapper.writeValueAsString(GYROS)
    assertEquals(json, GYROS_AS_JSON, true)
  }
}
