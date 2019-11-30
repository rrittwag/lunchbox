package lunchbox.domain.resolvers /* ktlint-disable max-line-length no-wildcard-imports */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSchweinestallTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverSchweinestall(htmlParser)
  private val providerId = SCHWEINESTALL.id

  @Test
  fun `resolve offers for week of 2019-12-02`() {
    val url = javaClass.getResource("/menus/schweinestall/2019-12-02.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 10

    var week = weekOf("2019-12-02")
    offers shouldContain LunchOffer(0, "Backfischfilet mit Remoulade und Gemüsereis", week.monday, euro("6.10"), providerId)
    offers shouldContain LunchOffer(0, "Gyros-Geschnetzeltes mit Tsatsiki und Pommes frites", week.tuesday, euro("6.10"), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenkeule mit Apfelrotkohl und Salzkartoffeln", week.wednesday, euro("6.10"), providerId)
    offers shouldContain LunchOffer(0, "Paprikagulasch mit Böhmischen Knödeln", week.thursday, euro("5.60"), providerId)
    offers shouldContain LunchOffer(0, "Chicken Nuggets mit Pommes frites und Sweet Chili Sauce", week.friday, euro("5.60"), providerId)

    week = weekOf("2019-12-09")
    offers shouldContain LunchOffer(0, "Currywurst mit Pommes frites", week.monday, euro("5.60"), providerId)
    offers shouldContain LunchOffer(0, "Kohlroulade mit Salzkartoffeln", week.tuesday, euro("5.60"), providerId)
    offers shouldContain LunchOffer(0, "Paniertes Hähnchenschnitzel mit Erbsen und Kartoffelpüree", week.wednesday, euro("5.60"), providerId)
    offers shouldContain LunchOffer(0, "Bifteki auf Paprikarahmsauce mit Pommes frites", week.thursday, euro("6.10"), providerId)
    offers shouldContain LunchOffer(0, "3 Spiegeleier auf Spinat mit Salzkartoffeln", week.friday, euro("5.60"), providerId)
  }
}
