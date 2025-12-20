package lunchbox.util.json

import org.joda.money.Money
import org.springframework.boot.jackson.JacksonComponent
import tools.jackson.core.JsonGenerator
import tools.jackson.databind.SerializationContext
import tools.jackson.databind.ValueSerializer

/**
 * Money-Objekte zu JSON serialisieren
 */
@JacksonComponent
class MoneySerializer : ValueSerializer<Money>() {
  override fun serialize(
    value: Money?,
    gen: JsonGenerator?,
    ctxt: SerializationContext?,
  ) {
    val moneyAsNumber = value?.amountMinorInt ?: 0
    gen?.writeNumber(moneyAsNumber)
  }
}
