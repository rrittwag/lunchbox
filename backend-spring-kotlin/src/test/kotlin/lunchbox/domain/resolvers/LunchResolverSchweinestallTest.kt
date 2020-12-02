package lunchbox.domain.resolvers /* ktlint-disable max-line-length */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSchweinestallTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverSchweinestall(DateValidator.alwaysValid(), htmlParser)
  private val providerId = SCHWEINESTALL.id

  @Test
  fun `resolve offers for week of 2020-01-13`() {
    val url = javaClass.getResource("/menus/schweinestall/2020-01-13.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 10

    var week = weekOf("2020-01-06")
    offers shouldContain LunchOffer(0, "Paniertes Alaska-Seelachsfilet", "auf Petersiliensauce mit Salzkartoffeln", week.monday, euro("6.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gyros-Geschnetzeltes", "mit Tsatsiki und Pommes frites", week.tuesday, euro("6.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hühnerfrikassee", "mit Reis", week.wednesday, euro("5.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Königsberger Klopse", "mit Salzkartoffeln und Rote Bete", week.thursday, euro("5.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "3 Eier in süßsaurer Sauce", "mit Kartoffelpüree", week.friday, euro("5.60"), emptySet(), providerId)

    week = weekOf("2020-01-13")
    offers shouldContain LunchOffer(0, "Gebratene Forelle", "mit Mandelbutter und Salzkartoffeln", week.monday, euro("6.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hackbraten „Griechische Art“", "mit Blattspinat, Tomate und Hirtenkäse überbacken, dazu Reis", week.tuesday, euro("5.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenbrustfilet", "auf mexikanischer Gemüsesauce mit Kartoffelspalten", week.wednesday, euro("6.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Jägerschnitzel", "mit Tomatensauce und Spirelli", week.thursday, euro("5.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Cheeseburger", "mit Pommes frites", week.friday, euro("6.10"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-02-10`() {
    val url = javaClass.getResource("/menus/schweinestall/2020-02-10.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 10

    val week = weekOf("2020-02-10")
    offers shouldContain LunchOffer(0, "Pangasiusfilet", "mit Champignons und Paprika überbacken, dazu Pommes frites", week.monday, euro("6.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Geschnetzeltes in Sahnesauce", "mit Spätzle", week.tuesday, euro("5.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenbrustfilet", "auf Ratatouille mit Reis", week.wednesday, euro("6.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kohlroulade", "mit Salzkartoffeln", week.thursday, euro("5.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Spaghetti Carbonara", "", week.friday, euro("5.60"), emptySet(), providerId)
  }
}
