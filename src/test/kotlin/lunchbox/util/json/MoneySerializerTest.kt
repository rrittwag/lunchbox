package lunchbox.util.json

import com.fasterxml.jackson.databind.ObjectMapper
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.skyscreamer.jsonassert.JSONAssert.assertEquals
import org.skyscreamer.jsonassert.JSONCompareMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.test.context.junit.jupiter.SpringExtension

@JsonTest
@ExtendWith(SpringExtension::class)
class MoneySerializerTest(
  @Autowired val mapper: ObjectMapper
) {

  @Test
  fun serialize() {
    val entity = TestEntityWithMoney(Money.parse("EUR 15.20"))

    val json = mapper.writeValueAsString(entity)

    assertEquals(json, "{ price: 1520 }", JSONCompareMode.STRICT)
  }
}

class TestEntityWithMoney(
  val price: Money
)
