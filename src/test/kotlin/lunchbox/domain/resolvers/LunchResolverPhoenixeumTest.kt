package lunchbox.domain.resolvers /* ktlint-disable max-line-length */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverPhoenixeumTest {
  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverPhoenixeum(DateValidator.alwaysValid(), htmlParser)
  private val providerId = LunchProvider.PHOENIXEUM.id

  @Test
  fun `resolve offers for week of 2020-04-06`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-04-06.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 7

    var week = weekOf("2020-04-06")
    offers shouldContain LunchOffer(0, "Hackbraten (gemischt)", "mit Bohnengemüse und Salzkartoffeln", week.monday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hackbraten (gemischt)", "mit Bohnengemüse und Salzkartoffeln", week.tuesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinefilet", "mit frischen Champignons in Rahmsoße und Reis", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinefilet", "mit frischen Champignons in Rahmsoße und Reis", week.thursday, euro("5.00"), emptySet(), providerId)

    week = weekOf("2020-04-13")
    offers shouldContain LunchOffer(0, "paniertes Schweineschnitzel", "mit Bohnen-Möhrengemüse & Kartoffeln", week.tuesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "kleine Fleischbällchen", "in Tomaten-Letscho-Soße, dazu Reis oder Kartoffeln", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "kleine Fleischbällchen", "in Tomaten-Letscho-Soße, dazu Reis oder Kartoffeln", week.thursday, euro("5.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-04-27`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-04-27.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 3

    val week = weekOf("2020-04-27")
    offers shouldContain LunchOffer(0, "lauwarmer Gemüse-Kartoffelsalat", "mit Rucola, dazu zwei angebratene Maultaschen (veget. Füllung)", week.tuesday, euro("5.00"), setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Gyros vom Schwein", "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gyros vom Schwein", "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)", week.thursday, euro("5.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-05-11`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-05-11.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 8

    var week = weekOf("2020-05-04")
    offers shouldContain LunchOffer(0, "Nudeln", "mit Tomatensoße und Jägerschnitzel", week.monday, euro("5.00"), setOf(), providerId)
    offers shouldContain LunchOffer(0, "Nudeln", "mit Tomatensoße und Jägerschnitzel", week.tuesday, euro("5.00"), setOf(), providerId)
    offers shouldContain LunchOffer(0, "Gemüsepfanne", "mit Hühnchen, Spargel, Möhren, Erbsen, Pilzen, Sahne und Reis", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gemüsepfanne", "mit Hühnchen, Spargel, Möhren, Erbsen, Pilzen, Sahne und Reis", week.thursday, euro("5.00"), emptySet(), providerId)

    week = weekOf("2020-05-11")
    offers shouldContain LunchOffer(0, "Currywurst", "mit Hausgemachter Soße, Paprika-Tomatengemüse und Schwenkartoffeln", week.monday, euro("5.00"), setOf(), providerId)
    offers shouldContain LunchOffer(0, "Currywurst", "mit Hausgemachter Soße, Paprika-Tomatengemüse und Schwenkartoffeln", week.tuesday, euro("5.00"), setOf(), providerId)
    offers shouldContain LunchOffer(0, "Schweineroulade", "mit Rotkohl und Klößen", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweineroulade", "mit Rotkohl und Klößen", week.thursday, euro("5.00"), emptySet(), providerId)
  }
}
