package lunchbox.util.json

import org.assertj.core.api.Assertions.assertThat
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
class MoneySerializerTest(
  @Autowired val json: JacksonTester<TestEntityWithMoney>
) {

  @Test
  fun serialize() {
    val entity = TestEntityWithMoney(Money.parse("EUR 15.20"))

    assertThat(json.write(entity)).isEqualTo("{ price: 1520 }")
  }
}

class TestEntityWithMoney(
  val price: Money
)
