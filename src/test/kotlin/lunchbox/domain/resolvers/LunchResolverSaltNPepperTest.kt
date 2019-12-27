package lunchbox.domain.resolvers /* ktlint-disable max-line-length */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSaltNPepperTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverSaltNPepper(DateValidator.alwaysValid(), htmlParser)
  private val providerId = SALT_N_PEPPER.id

  @Test
  fun `resolve offers for week of 2015-06-19`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-06-19.html")
    val week = weekOf("2015-06-19")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
    offers shouldContain LunchOffer(0, "Grüne Bohneneintopf", "mit Kasslerfleisch", week.monday, euro("3.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenkeule", "in Curry-Mango-Rahm, frische Buttermöhren, Kartoffeln", week.monday, euro("5.20"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Putenstreifen", "mit Pilzen in Käsesahnesauce auf Spaghetti", week.monday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Sahnemilchreis", "mit Kirschen, 1 Tasse Kaffee", week.monday, euro("5.30"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Rumpsteak (180gr.)", "Grillbutter, Pommes frites, Salatbeilage", week.monday, euro("6.90"), listOf("Wochenangebot"), providerId)

    offers shouldContain LunchOffer(0, "Frische Paprikacremesuppe", "mit geröstetem Bacon", week.tuesday, euro("3.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Putenleber", "hausgemachter Apfelrotkohl, hausgemachtes Kartoffelpüree", week.tuesday, euro("5.20"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Gefüllte Schnitzel „Cordon bleu“", "mediterranem Gemüsegratin", week.tuesday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Blumenkohlgratin", "1 Dessert", week.tuesday, euro("4.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Rumpsteak (180gr.)", "Grillbutter, Pommes frites, Salatbeilage", week.tuesday, euro("6.90"), listOf("Wochenangebot"), providerId)

    offers shouldContain LunchOffer(0, "Erbseneintopf", "mit Wiener Würstchenscheiben", week.wednesday, euro("3.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Frische Lachsfiletwürfel", "auf Bandnudeln, Spinat-Käse-Sauce", week.wednesday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Hühnerfrikassee", "mit Butterreis", week.wednesday, euro("5.20"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "4 Kartoffelpuffer", "mit Apfelmus", week.wednesday, euro("5.20"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Rumpsteak (180gr.)", "Grillbutter, Pommes frites, Salatbeilage", week.wednesday, euro("6.90"), listOf("Wochenangebot"), providerId)

    offers shouldContain LunchOffer(0, "Thai Suppe", "mit Hühnerfleisch und Frischem Koriander", week.thursday, euro("3.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Frische Hähnchenbrust", "auf gebratenen Asianudeln, Kokos-Sauce", week.thursday, euro("5.20"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Paprika-Gulasch oder Champignon-Gulasch", "mit Nudeln Sauerrahm", week.thursday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "2 Eier", "Senfsauce, Buttergemüse, Kartoffeln, 1 Dessert", week.thursday, euro("4.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Rumpsteak (180gr.)", "Grillbutter, Pommes frites, Salatbeilage", week.thursday, euro("6.90"), listOf("Wochenangebot"), providerId)

    offers shouldContain LunchOffer(0, "Spaghetti-Bolognese", "", week.friday, euro("5.20"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Kasslersteak", "mit Pommes frites und Weißkrautsalat", week.friday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Sommerpommes", "1 Dessert", week.friday, euro("4.90"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Rumpsteak (180gr.)", "Grillbutter, Pommes frites, Salatbeilage", week.friday, euro("6.90"), listOf("Wochenangebot"), providerId)
  }

  @Test
  fun `resolve offers for Easter week of 2015-03-30`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-03-30.html")
    val week = weekOf("2015-03-30")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers.filter { it.day == week.monday } shouldHaveSize 5
    offers.filter { it.day == week.tuesday } shouldHaveSize 5
    offers.filter { it.day == week.wednesday } shouldHaveSize 5
    offers.filter { it.day == week.thursday } shouldHaveSize 4
    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for week of 2015-05-29`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-05-29.html")
    val week = weekOf("2015-05-29")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 5
    offers.filter { it.day == week.wednesday } shouldHaveSize 5
    offers.filter { it.day == week.thursday } shouldHaveSize 5
    offers.filter { it.day == week.friday } shouldHaveSize 4
  }

  @Test
  fun `resolve offers for week of 2015-07-11`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-07-11.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
  }

  @Test
  fun `resolve offers for week of 2015-08-10`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-08-10.html")
    val week = weekOf("2015-08-10")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
    offers shouldContain LunchOffer(0, "Gefülltes Schnitzel „Toskaner Art“", "mit Tomatensalat und Pommes frites", week.tuesday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Gefülltes Schnitzel „Cordon bleu“", "mit buntem Krautsalat und Pommes frites", week.wednesday, euro("5.70"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Hacksteak „Toskana“", "mit mediterraner Gemüsepfanne dazu Pommes frites", week.friday, euro("5.20"), emptyList(), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-02-16`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2016-02-16.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
  }

  @Test
  fun `resolve offers for week of 2016-04-12`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2016-04-12.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
    offers shouldContain LunchOffer(0, "Bunter Salat", "mit frischen Erdbeeren, gebratenem Hähnchenfleisch und hausgemachtem Erdbeer-Minze-Joghurt-Dressing", date("2016-04-11"), euro("5.90"), listOf("Wochenangebot"), providerId)
  }

  @Test
  fun `resolve offers for week of 2018-09-14`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2018-09-14.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers shouldContain LunchOffer(0, "Serbischer Bohneneintopf", "", date("2018-09-10"), euro("3.50"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "Spaghetti Bolognese", "mit Reibekäse", date("2018-09-10"), euro("5.90"), emptyList(), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-03-11`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2019-03-11.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers shouldContain LunchOffer(0, "Linseneintopf", "mit gebratenen Wiener Würstchenscheiben (für Vegetarier extra)", date("2019-03-11"), euro("3.50"), emptyList(), providerId)
    offers shouldContain LunchOffer(0, "frischer Blumenkohl", "mit sc. Hollandaise und Kartoffeln", date("2019-03-11"), euro("5.90"), listOf("vegetarisch"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-09-09`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2019-09-09.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers shouldContain LunchOffer(0, "Spinat-Lasagne", "", date("2019-09-09"), euro("5.90"), listOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Lammbraten", "mit Speckbohnen und Petersilienkartoffeln", date("2019-09-10"), euro("6.30"), emptyList(), providerId)
  }
}
