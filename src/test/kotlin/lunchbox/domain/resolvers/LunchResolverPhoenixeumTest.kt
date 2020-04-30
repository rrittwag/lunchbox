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
  fun `resolve offers for week of 2019-04-06`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-04-06.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-04-06")
    offers shouldContain LunchOffer(0, "Hackbraten (gemischt)", "mit Bohnengemüse und Salzkartoffeln", week.monday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hackbraten (gemischt)", "mit Bohnengemüse und Salzkartoffeln", week.tuesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinefilet", "mit frischen Champignons in Rahmsoße und Reis", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinefilet", "mit frischen Champignons in Rahmsoße und Reis", week.thursday, euro("5.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-01-14`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-04-27.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 3

    val week = weekOf("2020-04-27")
    offers shouldContain LunchOffer(0, "lauwarmer Gemüse-Kartoffelsalat", "mit Rucola, dazu zwei angebratene Maultaschen (veget. Füllung)", week.tuesday, euro("5.00"), setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Gyros vom Schwein", "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)", week.wednesday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gyros vom Schwein", "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)", week.thursday, euro("5.00"), emptySet(), providerId)
  }
}
