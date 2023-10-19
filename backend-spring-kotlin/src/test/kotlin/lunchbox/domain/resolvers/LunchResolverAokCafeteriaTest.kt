package lunchbox.domain.resolvers

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainNone
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverAokCafeteriaTest {
  private val htmlParser = HtmlParser(mockk())

  private fun resolver() = LunchResolverAokCafeteria(DateValidator.alwaysValid(), htmlParser)

  private val providerId = AOK_CAFETERIA.id

  @Test
  fun `resolve offers for week of 2020-08-31`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2020-08-31.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 23
    var week = weekOf("2020-08-31")
    offers shouldContain LunchOffer(0, "Gemüse-Spaghetti", "in Sahnesaue", week.monday, null, emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Fruchtige Asia-Gemüse Pfanne",
        "mit Hähnchen und Reis",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schnitzel",
        "mit Champignonrahm und Rosmarinkartoffeln",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "vegetarische Tortellini",
        "mit Pilzrahm",
        week.tuesday,
        null,
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengemüsecurry",
        "mit Reis, dazu Rote Beete",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Vanille Milchreis",
        "mit Apfelmus",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinebraten",
        "mit Sauce, Speckbohnen und Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "hausgem. Kotelett",
        "mit Spargelgemüse und Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hackbraten",
        "Buttermöhren und Kartoffeln",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschnitzel",
        "mit Blumenkohlsauce und Kartoffeln",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Mexikanische Bohnenpfanne",
        "mit Süßkartoffel-Schnitte und Joghurt",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratendes Seelachs",
        "mit Dillsauce und Kartoffeln",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebrat. Seelachs",
        "mit Dillsoße, Kartoffeln und Rohkost",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinefilet",
        "mit Letscho und Bratkartoffeln",
        week.friday,
        null,
        emptySet(),
        providerId,
      )

    week = weekOf("2020-09-07")
    offers shouldContain
      LunchOffer(
        0,
        "Boulette",
        "mit Blumenkohl- Erbsengemüse und Kartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Putenfrikasse",
        "mit Reis und Rohkost",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gabelspaghetti",
        "mit Bolognesesoße",
        week.tuesday,
        null,
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenbrustfilet",
        "mit Wachsbohnen und Kartoffeln",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Eierkuchen",
        "mit Erdbeer- Rhabarbarsoße",
        week.wednesday,
        null,
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kräuterbraten",
        "mit Soße, Rotkohl und Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Seelachs",
        "mit Meerettich- Petersiliensoße, Kartoffeln und Rohkost",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchencurry",
        "mit Wildreis und Dessert",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gemüseschnitzel",
        "mit Mischgemüse und Kartoffeln",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
  }

  // @Test
  fun `resolve offers for week of 2020-08-31_alt`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2020-08-31_alt.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 29
    var week = weekOf("2020-08-31")
    offers shouldContain LunchOffer(0, "Gemüse-Spaghetti", "in Sahnesaue", week.monday, null, emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Fruchtige Asia-Gemüse Pfanne",
        "mit Hähnchen und Reis",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schnitzel",
        "mit Champignonrahm und Rosmarinkartoffeln",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "vegetarische Tortellini",
        "mit Pilzrahm",
        week.tuesday,
        null,
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bunter Vitaminsalat",
        "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing",
        week.tuesday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Vanille Milchreis",
        "mit Apfelmus",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinebraten",
        "mit Sauce, Speckbohnen und Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bunter Vitaminsalat",
        "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing",
        week.wednesday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hackbraten",
        "Buttermöhren und Kartoffeln",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschnitzel",
        "mit Blumenkohlsauce und Kartoffeln",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bunter Vitaminsalat",
        "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing",
        week.thursday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratendes Seelachs",
        "mit Dillsauce und Kartoffeln",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebrat. Seelachs",
        "mit Dillsoße, Kartoffeln und Rohkost",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bunter Vitaminsalat",
        "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing",
        week.friday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )

    week = weekOf("2020-09-07")
    offers shouldContain
      LunchOffer(
        0,
        "Boulette",
        "mit Blumenkohl- Erbsengemüse und Kartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Putenfrikasse",
        "mit Reis und Rohkost",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Griechischer Bauernsalat",
        "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing",
        week.monday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gabelspaghetti",
        "mit Bolognesesoße",
        week.tuesday,
        null,
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenbrustfilet",
        "mit Wachsbohnen und Kartoffeln",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Griechischer Bauernsalat",
        "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing",
        week.tuesday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Eierkuchen",
        "mit Erdbeer- Rhabarbarsoße",
        week.wednesday,
        null,
        setOf("vegetarisch"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kräuterbraten",
        "mit Soße, Rotkohl und Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Griechischer Bauernsalat",
        "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing",
        week.wednesday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Seelachs",
        "mit Meerettich- Petersiliensoße, Kartoffeln und Rohkost",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchencurry",
        "mit Wildreis und Dessert",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Griechischer Bauernsalat",
        "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing",
        week.thursday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Gemüseschnitzel",
        "mit Mischgemüse und Kartoffeln",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Paprika Gulasch",
        "mit Kartoffeln, dazu einen Joghurt",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Griechischer Bauernsalat",
        "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing",
        week.friday,
        null,
        setOf("auf Vorbestellung"),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2020-09-14`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2020-09-14.html")

    val offers = resolver().resolve(url)

    val week = weekOf("2020-09-14")
    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Minikönigsberge",
        "Kartoffeln mit Sauce und Roter Beete",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Makkaroni Pilzpfanne",
        "Champignon, Zwiebel, und Ratatouligemüse, dazu Joghurt",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Sahnegeschnetzeltes", "mit Nudeln", week.tuesday, null, emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "geb. Fischfilet",
        "mit Rahmwirsing und Reis",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Steckrübenmöhreneintopf",
        "mit Brötchen",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kasslerkohlpfanne",
        "mit Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Eierragout", "und Kartoffeln", week.thursday, null, emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Schweineleber",
        "mit Kartoffelpüree und Apfel-Zwiebel-Sauce",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Vanillemilchsuppe",
        "mit Nudeln und Obst",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweinegulasch",
        "mit Kartoffeln und Senfgurke",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2021-11-15`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2021-11-15.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 30

    var week = weekOf("2021-11-15")
    offers shouldContain
      LunchOffer(
        0,
        "Gemüseschnitzel",
        "mit Kohlrabi- Möhrengemüse und Kartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Chili con Carne",
        "Rinderhack, mit Reis, Obst",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kotelett",
        "120g ohne Knochen, mit Bohnengemüse und Kartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Nudeln Bolognese", "Obst", week.tuesday, null, emptySet(), providerId)
    offers shouldContain
      LunchOffer(
        0,
        "Weißkohleintopf",
        "mit Schweinefleisch, Brötchen",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Rinderschmorbraten",
        "mit Rotkohl und Klöße",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Fischfilet",
        "mit Dillsoße und Kartoffeln, dazu Rohkost",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Szegediner Gulasch",
        "mit Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kaninchenkeule",
        "in Sahne-Zwiebel-Sauce mit Speckbohnen und Kartoffeln",
        week.wednesday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Mini Königsberger Klopse",
        "in Kapernsoße und Kartoffeln",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Putenbrust",
        "mit Spinat-Käse-Sauce und Nudeln",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "gebratene Forelle 160g",
        "mit Püree, Rohkost",
        week.thursday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Eier",
        "in Senfsoße und Kartoffeln, Rohkost",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bratwurst",
        "mit Bayrischkraut und Kartoffeln",
        week.friday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain LunchOffer(0, "Wildgulasch", "mit Spätzle", week.friday, null, emptySet(), providerId)

    week = weekOf("2021-11-22")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2021-12-20`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2021-12-20.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 33

    var week = weekOf("2021-12-13")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3

    week = weekOf("2021-12-20")
    offers.filter { it.day == week.monday } shouldHaveSize 2
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 2
    offers.filter { it.day == week.friday } shouldHaveSize 0

    week = weekOf("2021-12-27")
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 0
    offers.filter { it.day == week.wednesday } shouldHaveSize 0
    offers.filter { it.day == week.thursday } shouldHaveSize 0
    offers.filter { it.day == week.friday } shouldHaveSize 0

    week = weekOf("2022-01-03")
    offers.filter { it.day == week.monday } shouldHaveSize 2
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 2
    offers.filter { it.day == week.friday } shouldHaveSize 2
  }

  @Test
  fun `resolve offers for week of 2022-06-07`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2022-06-07.html")

    val offers = resolver().resolve(url)

    println(offers)
    offers shouldHaveSize 27

    var week = weekOf("2022-06-07")
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3

    week = weekOf("2022-06-13")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-08-28`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2023-08-28.html")

    val offers = resolver().resolve(url)

    println(offers)
    offers shouldHaveSize 30

    val week = weekOf("2023-08-28")
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2023-09-18`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2023-09-18.html")

    val offers = resolver().resolve(url)

    println(offers)
    offers shouldHaveSize 20

    val week = weekOf("2023-09-18")
    offers.filter { it.day == week.monday } shouldHaveSize 2
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 2
    offers.filter { it.day == week.friday } shouldHaveSize 2

    offers shouldContainNone { it.name.contains("Nicht bestellbar") }
  }
}
