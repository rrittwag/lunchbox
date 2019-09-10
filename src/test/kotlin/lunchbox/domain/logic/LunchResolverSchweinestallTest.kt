package lunchbox.domain.logic /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSchweinestallTest {

  private fun resolver() = LunchResolverSchweinestall()
  private val providerId = SCHWEINESTALL.id

  @Test
  fun `resolve offers for week of 2015-02-09`() {
    val url = javaClass.getResource("/menus/schweinestall/2015-02-09.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2015-02-09")
    offers shouldHaveSize 5
    offers shouldContain LunchOffer(0, "Goldmakrelenfilet auf Gurken-Dillsauce mit Salzkartoffeln", week.monday, euro("5.80"), providerId)
    offers shouldContain LunchOffer(0, "Pilzgulasch mit Salzkartoffeln", week.tuesday, euro("4.80"), providerId)
    offers shouldContain LunchOffer(0, "Paniertes Hähnchenbrustfilet gefüllt mit Kräuterbutter, dazu Erbsen und Kartoffelpüree", week.wednesday, euro("5.80"), providerId)
    offers shouldContain LunchOffer(0, "Kasselersteak mit Ananas und Käse überbacken, dazu Buttermöhren und Pommes frites", week.thursday, euro("5.80"), providerId)
    offers shouldContain LunchOffer(0, "Spaghetti Bolognese mit Parmesan", week.friday, euro("4.80"), providerId)
  }
}
