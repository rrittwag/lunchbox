package util

import org.joda.money.{CurrencyUnit, Money}
import play.api.libs.json._

/**
  * Helper functions for converting Joda's Money.
  * User: robbel
  * Date: 24.04.16
  */
object PlayMoneyHelper {

  /**
    * Reads Joda's Money (as minor EUR) from Play JSON.
    */
  implicit val jodaMoneyReads = Reads[Money](js =>
    js.validate[String].map[Money](moneyString =>
      Money.ofMinor(CurrencyUnit.EUR, moneyString.toInt)
    )
  )

  /**
    * Writes Joda's Money to Play JSON (as minor EUR of type number).
    */
  implicit val jodaMoneyWrites: Writes[Money] = new Writes[Money] {
    def writes(m: Money): JsValue = JsNumber(m.getAmountMinorInt)
  }

}
