package lunchbox.util.json

import com.fasterxml.jackson.databind.ObjectMapper
import net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest

@JsonTest
class MoneySerializerTest(
  @Autowired val mapper: ObjectMapper
) {

  @Test
  fun serialize() {
    val entity = TestEntityWithMoney(Money.parse("EUR 15.20"))

    val result = mapper.writeValueAsString(entity)

    assertThatJson(result).isEqualTo("{ price: 1520 }")
  }
}

class TestEntityWithMoney(
  val price: Money
)
