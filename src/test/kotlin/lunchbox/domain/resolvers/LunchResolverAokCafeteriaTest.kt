package lunchbox.domain.resolvers /* ktlint-disable max-line-length */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
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
    offers shouldContain LunchOffer(0, "Fruchtige Asia-Gemüse Pfanne", "mit Hähnchen und Reis", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schnitzel", "mit Champignonrahm und Rosmarinkartoffeln", week.tuesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "vegetarische Tortellini", "mit Pilzrahm", week.tuesday, null, setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Hähnchengemüsecurry", "mit Reis, dazu Rote Beete", week.tuesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Vanille Milchreis", "mit Apfelmus", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinebraten", "mit Sauce, Speckbohnen und Kartoffeln", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "hausgem. Kotelett", "mit Spargelgemüse und Kartoffeln", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hackbraten", "Buttermöhren und Kartoffeln", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenschnitzel", "mit Blumenkohlsauce und Kartoffeln", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Mexikanische Bohnenpfanne", "mit Süßkartoffel-Schnitte und Joghurt", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "gebratendes Seelachs", "mit Dillsauce und Kartoffeln", week.friday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "gebrat. Seelachs", "mit Dillsoße, Kartoffeln und Rohkost", week.friday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinefilet", "mit Letscho und Bratkartoffeln", week.friday, null, emptySet(), providerId)

    week = weekOf("2020-09-07")
    offers shouldContain LunchOffer(0, "Boulette", "mit Blumenkohl- Erbsengemüse und Kartoffeln", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Putenfrikasse", "mit Reis und Rohkost", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gabelspaghetti", "mit Bolognesesoße", week.tuesday, null, setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenbrustfilet", "mit Wachsbohnen und Kartoffeln", week.tuesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Eierkuchen", "mit Erdbeer- Rhabarbarsoße", week.wednesday, null, setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Kräuterbraten", "mit Soße, Rotkohl und Kartoffeln", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Seelachs", "mit Meerettich- Petersiliensoße, Kartoffeln und Rohkost", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchencurry", "mit Wildreis und Dessert", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gemüseschnitzel", "mit Mischgemüse und Kartoffeln", week.friday, null, emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-08-31_alt`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2020-08-31_alt.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 29
    var week = weekOf("2020-08-31")
    offers shouldContain LunchOffer(0, "Gemüse-Spaghetti", "in Sahnesaue", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Fruchtige Asia-Gemüse Pfanne", "mit Hähnchen und Reis", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schnitzel", "mit Champignonrahm und Rosmarinkartoffeln", week.tuesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "vegetarische Tortellini", "mit Pilzrahm", week.tuesday, null, setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Bunter Vitaminsalat", "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing", week.tuesday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "Vanille Milchreis", "mit Apfelmus", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinebraten", "mit Sauce, Speckbohnen und Kartoffeln", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Bunter Vitaminsalat", "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing", week.wednesday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "Hackbraten", "Buttermöhren und Kartoffeln", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenschnitzel", "mit Blumenkohlsauce und Kartoffeln", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Bunter Vitaminsalat", "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing", week.thursday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "gebratendes Seelachs", "mit Dillsauce und Kartoffeln", week.friday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "gebrat. Seelachs", "mit Dillsoße, Kartoffeln und Rohkost", week.friday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Bunter Vitaminsalat", "Ananas, Gemüsemais, Feta, Eisbergsalat, Grüne Gurke, Ei, Porree und geriebene süße Mandel, Joghurtdressing", week.friday, null, setOf("auf Vorbestellung"), providerId)

    week = weekOf("2020-09-07")
    offers shouldContain LunchOffer(0, "Boulette", "mit Blumenkohl- Erbsengemüse und Kartoffeln", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Putenfrikasse", "mit Reis und Rohkost", week.monday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Griechischer Bauernsalat", "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing", week.monday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "Gabelspaghetti", "mit Bolognesesoße", week.tuesday, null, setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenbrustfilet", "mit Wachsbohnen und Kartoffeln", week.tuesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Griechischer Bauernsalat", "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing", week.tuesday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "Eierkuchen", "mit Erdbeer- Rhabarbarsoße", week.wednesday, null, setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Kräuterbraten", "mit Soße, Rotkohl und Kartoffeln", week.wednesday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Griechischer Bauernsalat", "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing", week.wednesday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "Seelachs", "mit Meerettich- Petersiliensoße, Kartoffeln und Rohkost", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchencurry", "mit Wildreis und Dessert", week.thursday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Griechischer Bauernsalat", "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing", week.thursday, null, setOf("auf Vorbestellung"), providerId)
    offers shouldContain LunchOffer(0, "Gemüseschnitzel", "mit Mischgemüse und Kartoffeln", week.friday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Paprika Gulasch", "mit Kartoffeln, dazu einen Joghurt", week.friday, null, emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Griechischer Bauernsalat", "Tomate, Gurke, Rote Zwiebeln, Feta, Oliven, Joghurt Dressing", week.friday, null, setOf("auf Vorbestellung"), providerId)
  }
}
