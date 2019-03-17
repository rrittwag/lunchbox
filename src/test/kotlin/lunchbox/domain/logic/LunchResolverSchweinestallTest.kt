package lunchbox.domain.logic /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldContain
import org.junit.jupiter.api.Test

class LunchResolverSchweinestallTest {

  private fun resolver() = LunchResolverSchweinestall()

  @Test
  fun `resolve offers for week of 2015-02-09`() {
    val url = javaClass.getResource("/menus/schweinestall/schweinestall_2015-02-09.html")

    val offers = resolver().resolve(url)

    offers.size shouldBe 5
    offers shouldContain LunchOffer(0, "Goldmakrelenfilet auf Gurken-Dillsauce mit Salzkartoffeln", date("2015-02-09"), euro("5.80"), ID)
    offers shouldContain LunchOffer(0, "Pilzgulasch mit Salzkartoffeln", date("2015-02-10"), euro("4.80"), ID)
    offers shouldContain LunchOffer(0, "Paniertes Hähnchenbrustfilet gefüllt mit Kräuterbutter, dazu Erbsen und Kartoffelpüree", date("2015-02-11"), euro("5.80"), ID)
    offers shouldContain LunchOffer(0, "Kasselersteak mit Ananas und Käse überbacken, dazu Buttermöhren und Pommes frites", date("2015-02-12"), euro("5.80"), ID)
    offers shouldContain LunchOffer(0, "Spaghetti Bolognese mit Parmesan", date("2015-02-13"), euro("4.80"), ID)
  }
}

private val ID = LunchProvider.SCHWEINESTALL.id
