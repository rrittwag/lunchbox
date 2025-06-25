@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.domain.resolvers

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSuppenkulttourTest {
  private val htmlParser = HtmlParser(mockk())

  private fun resolver() = LunchResolverSuppenkulttour(DateValidator.alwaysValid(), htmlParser)

  private val providerId = SUPPENKULTTOUR.id

  @Test
  fun `resolve offers for week of 2019-01-14`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-01-14.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40

    var week = weekOf("2019-01-14")
    for (weekday in week.lunchDays) {
      offers shouldContain
        LunchOffer(
          0,
          "Berliner Kartoffelsuppe",
          "mit Kartoffeln, Wiener, Wurzelgemüse, Majoran",
          weekday,
          euro("4.70"),
          emptySet(),
          providerId,
        )
      offers shouldContain
        LunchOffer(
          0,
          "Hühnersuppe",
          "Huhn, Porree, Sellerie, Möhren, Pastinaken",
          weekday,
          euro("4.70"),
          emptySet(),
          providerId,
        )
      offers shouldContain
        LunchOffer(
          0,
          "afrikanischer Erdnusseintopf",
          "Erdnusspaste, Kichererbsen, Kidneybohnen, Lauch, rote Paprika, Weißkohl, Mangosaft",
          weekday,
          euro("4.70"),
          setOf("vegan"),
          providerId,
        )
    }
    offers shouldContain
      LunchOffer(
        0,
        "Schnüsch",
        "der bekannte Norddeutsche Gemüseeintopf, Kartoffeln, Bohnen, Kohlrabi, Möhren, Sellerie, Sahne, Kräuter",
        week.monday,
        euro("4.70"),
        setOf("vegetarisch", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta mit „Ossi-Würstchengulasch“",
        "Nudeln mit Tomatensoße, Wiener, sauren Gurken, Letschow",
        week.tuesday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Blumenkohl-Süßkartoffel-Curry",
        "mit Blumenkohl, Süßkartoffeln, Kichererbsen, roten Linsen, Madras Curry, Kurkuma, Ingwer",
        week.wednesday,
        euro("4.70"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Sweet-Chili-Lemon-Chicken",
        "knackiges Gemüse, Sprossen, mariniertes Huhn, Limette, Zitronengras, Reis",
        week.thursday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schmorgurken",
        "mit Rinderhackfleisch, Gurken, Tomatenmark, Sahne, Dill wahlweise + Röstzwiebeln",
        week.friday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )

    week = weekOf("2019-01-21")
    for (weekday in week.lunchDays) {
      offers shouldContain
        LunchOffer(
          0,
          "Grüne Bohnen Eintopf",
          "mit grünen Bohnen, Kartoffeln, Möhren, Kasslerschulter, Bohnenkraut",
          weekday,
          euro("4.70"),
          emptySet(),
          providerId,
        )
      offers shouldContain
        LunchOffer(
          0,
          "Hackfleischsuppe Mexico",
          "Rinderhackfleisch,Mais, weiße Bohnen, Tomaten",
          weekday,
          euro("4.70"),
          emptySet(),
          providerId,
        )
      offers shouldContain
        LunchOffer(
          0,
          "Magische Kohlsuppe",
          "Weißkohl, Möhren, Paprika, Sellerie,Tomaten, Petersilie",
          weekday,
          euro("4.70"),
          setOf("vegan"),
          providerId,
        )
    }
    offers shouldContain
      LunchOffer(
        0,
        "altdeutsche Kartoffelsuppe",
        "pürierte Variante, mit Schinken, Wiener, Möhren, Pastinaken, Lauch, Kartoffeln, Sahne, Muskat",
        week.monday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta Panna",
        "Nudeln mit Käse-Sahnesoße, Champignons, Kochschinken, Pfirsich",
        week.tuesday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Tomatensuppe",
        "Fond aus vielen, frischen Tomaten mit Feta, Wildreis & Kräutern",
        week.wednesday,
        euro("4.70"),
        setOf("vegetarisch", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Karotten-Linsensuppe",
        "mit Karotten, Süßkartoffeln, roten Linsen, Kurkuma, Ingwer, Kokos, Madras Curry",
        week.thursday,
        euro("4.70"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rote Beete Powertopf",
        "Rote Beete, mageres Rindfleisch, Weißkohl, wahlweise + Meerrettich",
        week.friday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-02-18`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-02-18.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40

    var week = weekOf("2019-02-18")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4
    offers shouldContain
      LunchOffer(
        0,
        "Süßkartoffel- Erdnusscremesuppe",
        "mit Süßkartoffeln, Erdnusspaste,Weißkohl, Curry, Ingwer, Kokos",
        week.monday,
        euro("4.70"),
        setOf("vegan"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Metaxasuppe",
        "marinierte Schweinefiletstreifen, Reis, Paprika, Tomaten in leckerer Metaxasoße - wie beim Lieblingsgriechen",
        week.thursday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )

    week = weekOf("2019-02-25")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4
    offers shouldContain
      LunchOffer(
        0,
        "Birnen-Linsen-DAL",
        "mit roten Linsen, Möhren, Kokosmilch, Birnen, frischem Ingwer, Kurkuma, Curry",
        week.monday,
        euro("4.70"),
        setOf("vegan"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kurkuma-Käsesuppe",
        "mit Möhren & Zucchini, Schmelzkäse, Kurkuma",
        week.monday,
        euro("4.70"),
        setOf("vegetarisch", "Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-06-10`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-06-10.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-06-10")
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain
      LunchOffer(
        0,
        "Pasta „Pollo“",
        "Huhn, Möhren, Zucchini, Cherrytomaten in einer cremigen Tomatensoße, verfeinert mit Olivenöl, Frischkäse, Mozzarella, Basilikum, Thymian",
        week.tuesday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "veganer Kartoffel-Kohlrabi- Eintopf",
        "mit Kartoffeln, Kohlrabi, Möhren, Petersilie, wahlweise + Schinkenwürfel",
        week.tuesday,
        euro("4.70"),
        setOf("vegan"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-08-26`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-08-26.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-08-26")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain
      LunchOffer(
        0,
        "Quark-Kartoffelsuppe",
        "(enthält i,g,1,2) cremige Suppe, Kartoffeln, Champignons, Wiener, Brokkoli, Kerbel, Schnittlauch, Quark",
        week.friday,
        null,
        setOf("Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-09-09`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-09-09.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-09-09")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain
      LunchOffer(
        0,
        "Möhren - Käse - Suppe",
        "Käsesuppe, Kurkuma, Juliennegemüsestreifen, wahlweise + mit Huhn",
        week.monday,
        euro("4.70"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Soupe au Pistou",
        "Gemüsesuppe aus der Provence - grüne Bohnen, weiße Bohnen, Zucchini, Kartoffel, Lauch, Tomaten, wahlweise + Ruccolapesto",
        week.monday,
        null,
        setOf("vegan"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-09-30`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-09-30.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-09-30")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 0
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain
      LunchOffer(
        0,
        "Hühnersuppe",
        "klare Brühe, Hühnchenbrust, Wurzelgemüse, wahlweise + Nudeln",
        week.friday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-10-07`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-10-07.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-10-07")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain
      LunchOffer(
        0,
        "Fenchel-Karottensuppe",
        "mit Fenchel, Möhrchen,Kokosmilch, Curry wahlweise + Zitronenhühnchen",
        week.monday,
        euro("4.50"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta Tomestra",
        "Tomaten-Estragonssoße mit Hühnchen, Strauchtomaten wahlweise + geriebenem Hartkäse",
        week.tuesday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Möhren-Kartoffeleintopf",
        "Karotten, Kartoffeln, Wirsing, Porree, Petersilie",
        week.wednesday,
        euro("4.70"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengulasch",
        "Low Carb - Hähnchen, Paprika, Tomaten, Lauch",
        week.thursday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Soljanka",
        "Wiener, Kassler, Bratwurst, Sauerkraut, Letscho, Zitrone, Schmand, Saure Gurken",
        week.friday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2019-10-28`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-10-28.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2019-10-28")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 0
    offers.filter { it.day == week.friday } shouldHaveSize 4

    offers shouldContain
      LunchOffer(
        0,
        "Soljanka",
        "Wiener, Kassler, Bratwurst, Sauerkraut, Letscho, Zitrone, Schmand, Saure Gurken",
        week.friday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kürbis-Kokos-Suppe (enthält i,e,f,j,k) vegan",
        "mit roten Linsen, Staudensellerie, Kürbis, roter Paprika, Kokosmilch, Kurkuma, Ingwer",
        week.friday,
        euro("4.70"),
        setOf("vegan"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2020-01-02`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2020-01-02.html")

    val offers = resolver().resolve(url)

    var week = weekOf("2019-12-23")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers shouldContain
      LunchOffer(
        0,
        "Oma´s Grüne Bohnen Eintopf",
        "Brechbohnen, Kartoffeln, Möhren, Kassler, Bohnenkraut",
        week.monday,
        euro("4.70"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gärtnergulasch",
        "mit Rinderhackfleisch, Zucchini, Tomaten, Kohlrabi, rote Paprika, Gurke",
        week.monday,
        euro("4.70"),
        emptySet(),
        providerId,
      )

    week = weekOf("2020-01-02")
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4
    offers shouldContain
      LunchOffer(
        0,
        "Süßkartoffel-Erbsencurry",
        "grüne & gelbe Erbsen, Karotten, Süßkartoffeln, Kokosmilch, Ingwer, Kurkuma",
        week.thursday,
        euro("4.70"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schnüsch",
        "der bekannte Norddeutsche Gemüseeintopf, Kartoffeln, Bohnen, Kohlrabi, Möhren, Sellerie, Sahne, Kräuter",
        week.friday,
        euro("4.70"),
        setOf("vegetarisch", "Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2020-03-23`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2020-03-23.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2020-03-23")
    offers shouldHaveSize 20
    offers shouldContain
      LunchOffer(
        0,
        "Gemüsegeschnetzeltes",
        "mit Reis, Karotten, Kohlrabi, Zucchini, Pastinaken, Tomaten, in einer fruchtigen Sahnesoße",
        week.thursday,
        euro("4.70"),
        setOf("Tagessuppe", "vegetarisch"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for Corona easter week of 2020-04-06`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2020-04-06.html")

    val offers = resolver().resolve(url)

    var week = weekOf("2020-04-06")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 0

    week = weekOf("2020-04-13")
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2020-05-18`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2020-05-18.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2020-05-18")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 0
    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for week of 2020-11-23`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2020-11-23.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2020-11-23")
    offers.filter { it.day == week.monday } shouldHaveSize 4
    offers.filter { it.day == week.tuesday } shouldHaveSize 4
    offers.filter { it.day == week.wednesday } shouldHaveSize 4
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 4
  }

  @Test
  fun `resolve offers for week of 2022-04-04`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2022-04-04.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2022-04-04")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2022-06-27`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2022-06-27.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2022-06-27")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-01-09`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2023-01-09.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2023-01-09")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-01-30`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2023-01-30.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2023-01-30")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-02-13`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2023-02-13.html")

    val offers = resolver().resolve(url)

    println(offers)

    val week = weekOf("2023-02-13")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-03-20`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2023-03-20.html")

    val offers = resolver().resolve(url)

    println(offers)

    val week = weekOf("2023-03-20")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-05-29 - Pfingsten`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2023-05-29.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 12
    val week = weekOf("2023-05-29")
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
    offers shouldContain
      LunchOffer(
        0,
        "pommersche Erbsensuppe",
        "Der Klassiker ! Erbsen mit Möhren, Kartoffeln, Porree, Pastinaken, Wiener, Thymian",
        week.tuesday,
        euro("5.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Spinat-Kartoffel-Topf",
        "mit Blattspinat, Kartoffel-und Süßkartoffelwürfel, Möhren, Cous Cous, Kokosmilch, rotem Curry",
        week.tuesday,
        euro("5.50"),
        setOf("vegan"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Pasta „Ossi-Würstchen-Gulasch“",
        "Nudeln in Tomatensoße mit Würstchen, Paprika, sauren Gurken",
        week.tuesday,
        euro("5.50"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Curry",
        "mit roten Linsen, Möhren, Markerbsen, Birne, Kokosmilch, Curry, Ingwer, Lemongras,",
        week.wednesday,
        euro("5.50"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Chicken Tikka Masala",
        "mit Hühnchen, Zucchini, Auberginen, Tomaten, Möhren Reis, Ingwer, Garam Masala, Koriander, wahlweise Joghurt",
        week.thursday,
        euro("5.50"),
        setOf("Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Soljanka",
        "Wiener, Kassler, Knacker, Paprika, Letschow, saure Gurken, Sauerkraut, Schmand, Zitrone",
        week.friday,
        euro("5.50"),
        setOf("Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2023-06-12`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2023-06-12.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 30
    val week = weekOf("2023-06-12")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
    offers shouldContain
      LunchOffer(
        0,
        "Cheesy Gärtnergulasch",
        "mit Rinderhackfleisch & Käsecreme, Zucchini, Kohlrabi, Paprika, Gurke, Tomaten, Kräuter, Tomatenmark",
        week.wednesday,
        euro("5.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Linsen DAL",
        "rote Linsen, Möhren, Zuccini, Ingwer, Kokosmilch, Koriander, Curry, schwarzer Sesam",
        week.wednesday,
        euro("5.50"),
        setOf("vegan"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "fruchtige Tomatencremesuppe",
        "mit Mozzarella, Gnocchi und Basilikum",
        week.wednesday,
        euro("5.50"),
        setOf("Tagessuppe"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2024-10-14`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2024-10-14.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 30
    val week = weekOf("2024-10-14")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
    offers shouldContain
      LunchOffer(
        0,
        "Omas Hühnersuppe",
        "mit Huhn, Wurzelgemüse, Petersilie, Nudeln",
        week.monday,
        euro("6.40"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-06-23`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2025-06-23.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 15
    val week = weekOf("2025-06-23")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
    offers shouldContain
      LunchOffer(
        0,
        "veganes Dinkel-Kokoscurry",
        "angeröstetes Dinkelvollkornschrot, Sesam, Karotten, Zucchini, Paprika, Brokkoli, Kichererbsen, Reisnudeln, Tomatenpüree, rotes Curry, Kafir-Limettenblätter, Sojasoße, Kokosmilch",
        week.wednesday,
        euro("6.50"),
        setOf("vegan", "Tagessuppe"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kasseler-Bohnen-Eintopf",
        "mit zartem Kasseler, grünen Bohnen, Kartoffeln, Möhren, Majoran und einem Schuss Apfelessig für die Frische",
        week.friday,
        euro("6.50"),
        setOf("Tagessuppe"),
        providerId,
      )
  }
}
