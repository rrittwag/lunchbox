@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.domain.resolvers

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverPhoenixeumTest {
  private val htmlParser = HtmlParser(mockk())
  private val ocrClient = mockk<OcrClient>()

  private fun resolver() = LunchResolverPhoenixeum(DateValidator.alwaysValid(), htmlParser, ocrClient)

  private val providerId = LunchProvider.PHOENIXEUM.id

  @Test
  fun `resolve offers for week of 2026-10-26`() {
    val offers =
      resolver().parseOffers(
        readFileContent("/menus/suppenkulttour/ocr/2026-10-26_Foto-22.10.26-07-52-24.png.html"),
      )

    offers shouldHaveSize 4

    val week = weekOf("2026-10-26")
    offers shouldContain
      LunchOffer(
        0,
        "Paniertes Hühnchenschnitzel",
        "mit Mischgemüse und Salzkartoffeln",
        week.monday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paniertes Hühnchenschnitzel",
        "mit Mischgemüse und Salzkartoffeln",
        week.tuesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Paprikaschote",
        "gefüllte Paprikaschote in kräftiger Soße, dazu Salzkartoffeln",
        week.wednesday,
        euro("9.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gefüllte Paprikaschote",
        "gefüllte Paprikaschote in kräftiger Soße, dazu Salzkartoffeln",
        week.thursday,
        euro("9.00"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2026-11-02`() {
    val offers = resolver().parseOffers(readFileContent("/menus/suppenkulttour/ocr/2026-11-02_WPNEU.png.html"))

    offers shouldHaveSize 4

    val week = weekOf("2026-11-02")
    offers shouldContain
      LunchOffer(
        0,
        "Backfisch",
        "Backfisch in Maispanade mit Kartoffelgratin und Gurkensalat",
        week.monday,
        euro("9.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Backfisch",
        "Backfisch in Maispanade mit Kartoffelgratin und Gurkensalat",
        week.tuesday,
        euro("9.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "geschmorter Weisskohl, Rinderhack, Salzkartoffeln, Klecks Schmand",
        week.wednesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "geschmorter Weisskohl, Rinderhack, Salzkartoffeln, Klecks Schmand",
        week.thursday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2026-08-10`() {
    assertOcrOffers(
      "2026-08-10",
      "2026-08-10_Bildschirmfoto-2026-08-10-um-14.15.33.jpg.html",
      ExpectedOcrOffer("Panierter Kasslerkotelett", "mit Butterbohnen und Kartoffelgratin", "8.50"),
      ExpectedOcrOffer("Gefüllte Paprikaschote", "mit Sauce und Salzkartoffeln", "8.50"),
    )
  }

  @Test
  fun `resolve offers for week of 2026-08-24`() {
    assertOcrOffers(
      "2026-08-24",
      "2026-08-24_VorlagePlan.png.html",
      ExpectedOcrOffer(
        "Schweinegeschnetzeltes",
        "Gyros-Art mit Rahmchampignons würzig gebratenes Schweinegeschnetzeltes nach Gyros-Art mit cremigen Rahmchampignons, dazu Reis & einen Klecks Gurken-Kräuter-Joghurt",
        "8.50",
      ),
      ExpectedOcrOffer(
        "Hähnchen-Cordon-Bleu",
        "mit Sommergemüse paniertes Hähnchen-Cordon-Bleu mit Schinken und Käse gefüllt, dazu buntes Sommergemüse, Kartoffelspalten und Kräuterrahm",
        "9.00",
      ),
    )
  }

  @Test
  fun `resolve offers for week of 2026-08-31`() {
    assertOcrOffers(
      "2026-08-31",
      "2026-08-31_Vorlageneu.png.html",
      ExpectedOcrOffer("große, \"Thüringer Bratwurst\"", "mit Sauerkraut & Salzkartoffeln", "8.00"),
      ExpectedOcrOffer(
        "Deftiges Gulasch mit Spätzle",
        "geschmortes Rinder & Schweinefleisch, bunte Paprika, Gemüsezwiebeln, dazu Spätzle",
        "8.50",
      ),
    )
  }

  @Test
  fun `resolve offers for week of 2026-09-07`() {
    assertOcrOffers(
      "2026-09-07",
      "2026-09-07_Wochenplan-07-11-09.png.html",
      ExpectedOcrOffer(
        "Zigeuner-Schnitzel",
        "paniertes Schweineschnitzel (180 g) mit Pommes & Zigeunersoße",
        "8.50",
      ),
      ExpectedOcrOffer(
        "Mecklenburger Rollbraten vom heimischen Wild",
        "klassisch gefüllt, mit Rotkohl & Klößen",
        "9.50",
      ),
    )
  }

  @Test
  fun `resolve offers for week of 2026-09-14`() {
    assertOcrOffers(
      "2026-09-14",
      "2026-09-14_Planaktuell.png.html",
      ExpectedOcrOffer("Königsberger Klopse", "Kartoffeln, rote Beete", "8.50"),
      ExpectedOcrOffer("Lasagne al forno", "herzhafte Lasagne mit würziger Rindfleischsoße und Käsekruste", "9.00"),
    )
  }

  @Test
  fun `resolve offers for week of 2026-09-28`() {
    assertOcrOffers(
      "2026-09-28",
      "2026-09-28_PekingSoup.png.html",
      ExpectedOcrOffer(
        "Paniertes Schweinekotelett",
        "mit cremigem Schwarzwurzel-Möhrengemüse & Salzkartoffeln",
        "8.50",
      ),
      ExpectedOcrOffer(
        "Ungarisches Paprikahuhn",
        "geschmortes Hühnchenbrustfilet in kräftiger Paprika-Rahmsoße mit Paprika & Gemüsezwiebeln, dazu Butterspätzle",
        "8.50",
      ),
    )
  }

  @Test
  fun `resolve offers for week of 2026-10-05`() {
    assertOcrOffers(
      "2026-10-05",
      "2026-10-05_OktoberersteWoche.png.html",
      ExpectedOcrOffer(
        "Sahniges Nudelgratin mit Rinderhack & Käse",
        "Cremiges Nudelgratin mit würzigem Rinderhackfleisch, Sahnesoße und Käse überbacken, dazu frischer Tomaten-Gurkensalat",
        "8.50",
      ),
      ExpectedOcrOffer(
        "Currywurst mit Pommes frites",
        "Currywurst mit hausgemachter, fruchtig-würziger Currysoße und goldbraunen, knusprigen Pommes",
        "8.50",
      ),
    )
  }

  @Test
  fun `resolve offers for week of 2026-10-12`() {
    assertOcrOffers(
      "2026-10-12",
      "2026-10-12_Typografischer-Wochenplan-mit-Gerichten.png.html",
      ExpectedOcrOffer(
        "Paniertes Seelachsfilet mit Kartoffelgratin",
        "Seelachsfilet in knuspriger Panade, Kartoffelgratin, Gurkensalat",
        "9.00",
      ),
      ExpectedOcrOffer("Schwalbennest", "cremiges Schwalbennest mit Rotkohl & Salzkartoffeln", "9.00"),
    )
  }

  @Test
  fun `resolve offers for week of 2026-10-19`() {
    assertOcrOffers(
      "2026-10-19",
      "2026-10-19_Woechentlicher-deutscher-Cafeteria-Speiseplan.png.html",
      ExpectedOcrOffer(
        "Saftiges Ofenhähnchen",
        "mit buntem Ofengemüse, lockerem Couscous und frischem Zitronen-Joghurt-Dip",
        "9.00",
      ),
      ExpectedOcrOffer(
        "Königsberger Klopse",
        "hausgemachte Hackfleischklopse in cremiger Kapernsoße, dazu Salzkartoffeln und Rote Bete",
        "9.00",
      ),
    )
  }

  private data class ExpectedOcrOffer(
    val name: String,
    val description: String,
    val price: String,
  )

  private fun assertOcrOffers(
    monday: String,
    resource: String,
    mondayAndTuesday: ExpectedOcrOffer,
    wednesdayAndThursday: ExpectedOcrOffer,
  ) {
    val offers = resolver().parseOffers(readFileContent("/menus/suppenkulttour/ocr/$resource"))
    val week = weekOf(monday)

    offers shouldHaveSize 4
    listOf(week.monday, week.tuesday).forEach { day ->
      offers shouldContain
        LunchOffer(
          0,
          mondayAndTuesday.name,
          mondayAndTuesday.description,
          day,
          euro(mondayAndTuesday.price),
          emptySet(),
          providerId,
        )
    }
    listOf(week.wednesday, week.thursday).forEach { day ->
      offers shouldContain
        LunchOffer(
          0,
          wednesdayAndThursday.name,
          wednesdayAndThursday.description,
          day,
          euro(wednesdayAndThursday.price),
          emptySet(),
          providerId,
        )
    }
  }

  private fun readFileContent(path: String): String {
    val url = javaClass.getResource(path)
    return url.readText(Charsets.UTF_8)
  }

  // --- old implementation ---

  @Test
  fun `resolve offers for week of 2020-04-06`() {
    val url = javaClass.getResource("/menus/phoenixeum/2020-04-06.html")

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 7

    var week = weekOf("2020-04-06")
    offers shouldContain
      LunchOffer(
        0,
        "Hackbraten (gemischt)",
        "mit Bohnengemüse und Salzkartoffeln",
        week.monday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hackbraten (gemischt)",
        "mit Bohnengemüse und Salzkartoffeln",
        week.tuesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinefilet",
        "mit frischen Champignons in Rahmsoße und Reis",
        week.wednesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinefilet",
        "mit frischen Champignons in Rahmsoße und Reis",
        week.thursday,
        euro("5.00"),
        emptySet(),
        providerId,
      )

    week = weekOf("2020-04-13")
    offers shouldContain
      LunchOffer(
        0,
        "paniertes Schweineschnitzel",
        "mit Bohnen-Möhrengemüse & Kartoffeln",
        week.tuesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "kleine Fleischbällchen",
        "in Tomaten-Letscho-Soße, dazu Reis oder Kartoffeln",
        week.wednesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 3

    val week = weekOf("2020-04-27")
    offers shouldContain
      LunchOffer(
        0,
        "lauwarmer Gemüse-Kartoffelsalat",
        "mit Rucola, dazu zwei angebratene Maultaschen (veget. Füllung)",
        week.tuesday,
        euro("5.00"),
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gyros vom Schwein",
        "mit Tzatziki, Reis, Salat (Tomaten, Gurken, Oliven, Petersilie)",
        week.wednesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 8

    var week = weekOf("2020-05-04")
    offers shouldContain
      LunchOffer(
        0,
        "Nudeln",
        "mit Tomatensoße und Jägerschnitzel",
        week.monday,
        euro("5.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Nudeln",
        "mit Tomatensoße und Jägerschnitzel",
        week.tuesday,
        euro("5.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gemüsepfanne",
        "mit Hühnchen, Spargel, Möhren, Erbsen, Pilzen, Sahne und Reis",
        week.wednesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gemüsepfanne",
        "mit Hühnchen, Spargel, Möhren, Erbsen, Pilzen, Sahne und Reis",
        week.thursday,
        euro("5.00"),
        emptySet(),
        providerId,
      )

    week = weekOf("2020-05-11")
    offers shouldContain
      LunchOffer(
        0,
        "Currywurst",
        "mit Hausgemachter Soße, Paprika-Tomatengemüse und Schwenkartoffeln",
        week.monday,
        euro("5.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Currywurst",
        "mit Hausgemachter Soße, Paprika-Tomatengemüse und Schwenkartoffeln",
        week.tuesday,
        euro("5.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineroulade",
        "mit Rotkohl und Klößen",
        week.wednesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 3

    val week = weekOf("2020-05-18")
    offers shouldContain
      LunchOffer(
        0,
        "Boullette",
        "mit Mischgemüse und Salzkartoffeln",
        week.monday,
        euro("5.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Boullette",
        "mit Mischgemüse und Salzkartoffeln",
        week.tuesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-06-08")
    offers shouldContain
      LunchOffer(
        0,
        "Sahne Hering",
        "mit Kartoffeln und Gurkensalat",
        week.monday,
        euro("5.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Sahne Hering",
        "mit Kartoffeln und Gurkensalat",
        week.tuesday,
        euro("5.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratene Schweinefilets",
        "mit Champignons in Sahnesoße, dazu Kartoffeln",
        week.wednesday,
        euro("6.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-09-07")
    offers shouldContain
      LunchOffer(
        0,
        "\"SchniBoKa\"",
        "paniertes Schnitzel dazu grüne Bohnen und Kartoffeln",
        week.monday,
        euro("6.50"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "\"SchniBoKa\"",
        "paniertes Schnitzel dazu grüne Bohnen und Kartoffeln",
        week.tuesday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gulasch",
        "mit Schweinefleisch, Paprika, Möhren, Zwiebeln dazu Nudeln",
        week.wednesday,
        euro("6.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4

    val week = weekOf("2020-11-23")
    offers shouldContain
      LunchOffer(
        0,
        "Backfisch mit Holsteiner Kartoffelsalat",
        "aus Kartoffeln, Äpfeln, Zwiebeln, Rucola, Mandelsplitter, Mayo, Joghurt, Olivenöl",
        week.monday,
        euro("6.00"),
        setOf(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Backfisch mit Holsteiner Kartoffelsalat",
        "aus Kartoffeln, Äpfeln, Zwiebeln, Rucola, Mandelsplitter, Mayo, Joghurt, Olivenöl",
        week.tuesday,
        euro("6.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paprikaschoten",
        "rote Paprika gefüllt mit Rinderhack & Reis, dazu deftige,dunkle Soße und Salzkartoffeln",
        week.wednesday,
        euro("6.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4
    val week = weekOf("2023-03-20")
    offers shouldContain
      LunchOffer(
        0,
        "buntes Ofengemüse",
        "mit Olivenöl, Paprika, Zucchini, Champignons, Kartoffeln, Möhren, Sellerie, Süßkartoffeln mit Feta und Sonnenblumenkernen, Zitronenjoghurt als Dip",
        week.monday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "buntes Ofengemüse",
        "mit Olivenöl, Paprika, Zucchini, Champignons, Kartoffeln, Möhren, Sellerie, Süßkartoffeln mit Feta und Sonnenblumenkernen, Zitronenjoghurt als Dip",
        week.tuesday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Currywurst",
        "mit hausgemachter Currysoße & Wedges",
        week.wednesday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 3
    val week = weekOf("2023-05-29")
    offers shouldContain
      LunchOffer(
        0,
        "Senfei",
        "2 Bioeier mit Kartoffeln, Soße, roter Beete",
        week.tuesday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hühnchenkeule",
        "mit Kartoffeln und Ofengemüse",
        week.wednesday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
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

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4
    val week = weekOf("2023-07-17")
    offers shouldContain
      LunchOffer(
        0,
        "Senfei",
        "2 Bio Eier, Kartoffeln, Rohkost und Soße",
        week.monday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Senfei",
        "2 Bio Eier, Kartoffeln, Rohkost und Soße",
        week.tuesday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratene Hühnchenkeule",
        "mit Buttererbsen, Salzkartoffeln Sauce Hollandaise",
        week.wednesday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratene Hühnchenkeule",
        "mit Buttererbsen, Salzkartoffeln Sauce Hollandaise",
        week.thursday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2023-07-31`() {
    val url = javaClass.getResource("/menus/phoenixeum/2023-07-31.html")

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4
    val week = weekOf("2023-07-31")
    offers shouldContain
      LunchOffer(
        0,
        "Rahmspinat",
        "mit Rührei (BIO) und Kartoffeln",
        week.monday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rahmspinat",
        "mit Rührei (BIO) und Kartoffeln",
        week.tuesday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Jägerschnitzel",
        "mit Tomatensoße und Nudeln",
        week.wednesday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Jägerschnitzel",
        "mit Tomatensoße und Nudeln",
        week.thursday,
        euro("6.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2024-09-09`() {
    val url = javaClass.getResource("/menus/phoenixeum/2024-09-09.html")

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4
    val week = weekOf("2024-09-09")
    offers shouldContain
      LunchOffer(
        0,
        "gebratene Hühnchenkeule",
        "mit Buttererbsen, Salzkartoffeln Sauce Hollandaise",
        week.monday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratene Hühnchenkeule",
        "mit Buttererbsen, Salzkartoffeln Sauce Hollandaise",
        week.tuesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "leckere Bratwurst 150 g",
        "Mischgemüse, Kartoffeln",
        week.wednesday,
        euro("8.00"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "leckere Bratwurst 150 g",
        "Mischgemüse, Kartoffeln",
        week.thursday,
        euro("8.00"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2024-10-14`() {
    val url = javaClass.getResource("/menus/phoenixeum/2024-10-14.html")

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4
    val week = weekOf("2024-10-14")
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenkeule",
        "mit Kohlrabi-Möhrengemüse und Salzkartoffeln",
        week.monday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenkeule",
        "mit Kohlrabi-Möhrengemüse und Salzkartoffeln",
        week.tuesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschnitzel (160 g)",
        "mit Pankopanade, Leipziger Allerlei Gemüse, Kartoffeln",
        week.wednesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschnitzel (160 g)",
        "mit Pankopanade, Leipziger Allerlei Gemüse, Kartoffeln",
        week.thursday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2026-01-26`() {
    val url = javaClass.getResource("/menus/phoenixeum/2026-01-26.html")

    val offers = resolver().resolveOld(url)

    offers shouldHaveSize 4
    val week = weekOf("2026-01-26")
    offers shouldContain
      LunchOffer(
        0,
        "Hack’n’Cheese Auflauf",
        "Rinderhack & Nudeln in Tomaten-Béchamelsauce, Ofengemüse aus Zucchini, Aubergine und Möhren - überbacken mit einer aromatischen Mischung aus Bergkäse, Mozzarella und Cheddar",
        week.monday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hack’n’Cheese Auflauf",
        "Rinderhack & Nudeln in Tomaten-Béchamelsauce, Ofengemüse aus Zucchini, Aubergine und Möhren - überbacken mit einer aromatischen Mischung aus Bergkäse, Mozzarella und Cheddar",
        week.tuesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schnitzel",
        "Knuspriges Schweineschnitzel, dazu cremiger Rahm-Blumenkohl, Kartoffeln",
        week.wednesday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schnitzel",
        "Knuspriges Schweineschnitzel, dazu cremiger Rahm-Blumenkohl, Kartoffeln",
        week.thursday,
        euro("8.50"),
        emptySet(),
        providerId,
      )
  }
}
