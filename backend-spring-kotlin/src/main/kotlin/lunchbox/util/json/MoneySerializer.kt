package lunchbox.util.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.joda.money.Money
import org.springframework.boot.jackson.JsonComponent

/**
 * Money-Objekte zu JSON serialisieren
 */
@JsonComponent
class MoneySerializer : JsonSerializer<Money>() {
  override fun serialize(
    value: Money?,
    gen: JsonGenerator?,
    serializers: SerializerProvider?,
  ) {
    val moneyAsNumber = value?.amountMinorInt ?: 0
    gen?.writeNumber(moneyAsNumber)
  }
}
