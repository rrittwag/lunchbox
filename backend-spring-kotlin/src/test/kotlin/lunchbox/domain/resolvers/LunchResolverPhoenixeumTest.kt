@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.domain.resolvers

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
    offers shouldContain LunchOffer(
      0,
      "Hackbraten (gemischt)",
      "mit Bohnengemüse und Salzkartoffeln",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Hackbraten (gemischt)",
      "mit Bohnengemüse und Salzkartoffeln",
      week.tuesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Schweinefilet",
      "mit frischen Champignons in Rahmsoße und Reis",
      week.wednesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Schweinefilet",
      "mit frischen Champignons in Rahmsoße und Reis",
      week.thursday,
      euro("5.00"),
      emptySet(),
      providerId,
    )

    week = weekOf("2020-04-13")
    offers shouldContain LunchOffer(
      0,
      "paniertes Schweineschnitzel",
      "mit Bohnen-Möhrengemüse & Kartoffeln",
      week.tuesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "kleine Fleischbällchen",
      "in Tomaten-Letscho-Soße, dazu Reis oder Kartoffeln",
      week.wednesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "kleine Fleischbällchen",
      "in Tomaten-Letscho-Soße, dazu Reis oder Kartoffeln",
      week.thursday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2020-04-27`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-04-27.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 3

    val week = weekOf("2020-04-27")
    offers shouldContain LunchOffer(
      0,
      "lauwarmer Gemüse-Kartoffelsalat",
      "mit Rucola, dazu zwei angebratene Maultaschen (veget. Füllung)",
      week.tuesday,
      euro("5.00"),
      setOf("vegetarisch"),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gyros vom Schwein",
      "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)",
      week.wednesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gyros vom Schwein",
      "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)",
      week.thursday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2020-05-11`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-05-11.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 8

    var week = weekOf("2020-05-04")
    offers shouldContain LunchOffer(
      0,
      "Nudeln",
      "mit Tomatensoße und Jägerschnitzel",
      week.monday,
      euro("5.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Nudeln",
      "mit Tomatensoße und Jägerschnitzel",
      week.tuesday,
      euro("5.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gemüsepfanne",
      "mit Hühnchen, Spargel, Möhren, Erbsen, Pilzen, Sahne und Reis",
      week.wednesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gemüsepfanne",
      "mit Hühnchen, Spargel, Möhren, Erbsen, Pilzen, Sahne und Reis",
      week.thursday,
      euro("5.00"),
      emptySet(),
      providerId,
    )

    week = weekOf("2020-05-11")
    offers shouldContain LunchOffer(
      0,
      "Currywurst",
      "mit Hausgemachter Soße, Paprika-Tomatengemüse und Schwenkartoffeln",
      week.monday,
      euro("5.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Currywurst",
      "mit Hausgemachter Soße, Paprika-Tomatengemüse und Schwenkartoffeln",
      week.tuesday,
      euro("5.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Schweineroulade",
      "mit Rotkohl und Klößen",
      week.wednesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Schweineroulade",
      "mit Rotkohl und Klößen",
      week.thursday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2020-05-18`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-05-18.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 3

    val week = weekOf("2020-05-18")
    offers shouldContain LunchOffer(
      0,
      "Boullette",
      "mit Mischgemüse und Salzkartoffeln",
      week.monday,
      euro("5.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Boullette",
      "mit Mischgemüse und Salzkartoffeln",
      week.tuesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Boullette",
      "mit Mischgemüse und Salzkartoffeln",
      week.wednesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2020-06-08`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-06-08.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-06-08")
    offers shouldContain LunchOffer(
      0,
      "Sahne Hering",
      "mit Kartoffeln und Gurkensalat",
      week.monday,
      euro("5.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Sahne Hering",
      "mit Kartoffeln und Gurkensalat",
      week.tuesday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "gebratene Schweinefilets",
      "mit Champignons in Sahnesoße, dazu Kartoffeln",
      week.wednesday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "gebratene Schweinefilets",
      "mit Champignons in Sahnesoße, dazu Kartoffeln",
      week.thursday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2020-09-07`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-09-07.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-09-07")
    offers shouldContain LunchOffer(
      0,
      "\"SchniBoKa\"",
      "paniertes Schnitzel dazu grüne Bohnen und Kartoffeln",
      week.monday,
      euro("6.50"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "\"SchniBoKa\"",
      "paniertes Schnitzel dazu grüne Bohnen und Kartoffeln",
      week.tuesday,
      euro("6.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gulasch",
      "mit Schweinefleisch, Paprika, Möhren, Zwiebeln dazu Nudeln",
      week.wednesday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gulasch",
      "mit Schweinefleisch, Paprika, Möhren, Zwiebeln dazu Nudeln",
      week.thursday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2020-11-23`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-11-23.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-11-23")
    offers shouldContain LunchOffer(
      0,
      "Backfisch mit Holsteiner Kartoffelsalat",
      "aus Kartoffeln, Äpfeln, Zwiebeln, Rucola, Mandelsplitter, Mayo, Joghurt, Olivenöl",
      week.monday,
      euro("6.00"),
      setOf(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Backfisch mit Holsteiner Kartoffelsalat",
      "aus Kartoffeln, Äpfeln, Zwiebeln, Rucola, Mandelsplitter, Mayo, Joghurt, Olivenöl",
      week.tuesday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Paprikaschoten",
      "rote Paprika gefüllt mit Rinderhack & Reis, dazu deftige,dunkle Soße und Salzkartoffeln",
      week.wednesday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Paprikaschoten",
      "rote Paprika gefüllt mit Rinderhack & Reis, dazu deftige,dunkle Soße und Salzkartoffeln",
      week.thursday,
      euro("6.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2023-03-20`() {
    val url = javaClass.getResource("/menus/phoenixeum/2023-03-20.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 4
    val week = weekOf("2023-03-20")
    offers shouldContain LunchOffer(
      0,
      "buntes Ofengemüse",
      "mit Olivenöl, Paprika, Zucchini, Champignons, Kartoffeln, Möhren, Sellerie, Süßkartoffeln mit Feta und Sonnenblumenkernen, Zitronenjoghurt als Dip",
      week.monday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "buntes Ofengemüse",
      "mit Olivenöl, Paprika, Zucchini, Champignons, Kartoffeln, Möhren, Sellerie, Süßkartoffeln mit Feta und Sonnenblumenkernen, Zitronenjoghurt als Dip",
      week.tuesday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Currywurst",
      "mit hausgemachter Currysoße & Wedges",
      week.wednesday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Currywurst",
      "mit hausgemachter Currysoße & Wedges",
      week.thursday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2023-05-29`() {
    val url = javaClass.getResource("/menus/phoenixeum/2023-05-29.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 3
    val week = weekOf("2023-05-29")
    offers shouldContain LunchOffer(
      0,
      "Senfei",
      "2 Bioeier mit Kartoffeln, Soße, roter Beete",
      week.tuesday,
      euro("6.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Hühnchenkeule",
      "mit Kartoffeln und Ofengemüse",
      week.wednesday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Hühnchenkeule",
      "mit Kartoffeln und Ofengemüse",
      week.thursday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2023-07-17`() {
    val url = javaClass.getResource("/menus/phoenixeum/2023-07-17.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 4
    val week = weekOf("2023-07-17")
    offers shouldContain LunchOffer(
      0,
      "Senfei",
      "2 Bio Eier, Kartoffeln, Rohkost und Soße",
      week.monday,
      euro("6.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Senfei",
      "2 Bio Eier, Kartoffeln, Rohkost und Soße",
      week.tuesday,
      euro("6.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "gebratene Hühnchenkeule",
      "mit Buttererbsen, Salzkartoffeln Sauce Hollandaise",
      week.wednesday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "gebratene Hühnchenkeule",
      "mit Buttererbsen, Salzkartoffeln Sauce Hollandaise",
      week.thursday,
      euro("7.50"),
      emptySet(),
      providerId,
    )
  }
}
