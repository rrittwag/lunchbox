package lunchbox.util.json

import org.springframework.boot.jackson.JsonComponent
import com.fasterxml.jackson.databind.JsonSerializer
import org.joda.money.Money
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider

@JsonComponent
class MoneySerializer : JsonSerializer<Money>() {
  override fun serialize(value: Money?, gen: JsonGenerator?, serializers: SerializerProvider?) {
    val moneyAsNumber = value?.amountMinorInt ?: 0
    gen?.writeNumber(moneyAsNumber)
  }
}
