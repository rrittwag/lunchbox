package lunchbox.domain.logic /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.domain.models.LunchOffer
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSaltNPepperTest {

  private fun resolver() = LunchResolverSaltNPepper(DateValidator.alwaysValid())
  private val providerId = SALT_N_PEPPER.id

  @Test
  fun `resolve offers for week of 2015-06-19`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-06-19.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
    offers shouldContain LunchOffer(0, "Grüne Bohneneintopf mit Kasslerfleisch", date("2015-06-15"), euro("3.90"), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenkeule in Curry-Mango-Rahm, frische Buttermöhren, Kartoffeln", date("2015-06-15"), euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "Putenstreifen mit Pilzen in Käsesahnesauce auf Spaghetti", date("2015-06-15"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "Sahnemilchreis mit Kirschen, 1 Tasse Kaffee", date("2015-06-15"), euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-15"), euro("6.90"), providerId)

    offers shouldContain LunchOffer(0, "Frische Paprikacremesuppe mit geröstetem Bacon", date("2015-06-16"), euro("3.90"), providerId)
    offers shouldContain LunchOffer(0, "Putenleber, hausgemachter Apfelrotkohl, hausgemachtes Kartoffelpüree", date("2015-06-16"), euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "Gefüllte Schnitzel „Cordon bleu“, mediterranem Gemüsegratin", date("2015-06-16"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "Blumenkohlgratin, 1 Dessert", date("2015-06-16"), euro("4.90"), providerId)
    offers shouldContain LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-16"), euro("6.90"), providerId)

    offers shouldContain LunchOffer(0, "Erbseneintopf mit Wiener Würstchenscheiben", date("2015-06-17"), euro("3.90"), providerId)
    offers shouldContain LunchOffer(0, "Frische Lachsfiletwürfel auf Bandnudeln, Spinat-Käse-Sauce", date("2015-06-17"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "Hühnerfrikassee mit Butterreis", date("2015-06-17"), euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "4 Kartoffelpuffer mit Apfelmus", date("2015-06-17"), euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-17"), euro("6.90"), providerId)

    offers shouldContain LunchOffer(0, "Thai Suppe mit Hühnerfleisch und Frischem Koriander", date("2015-06-18"), euro("3.90"), providerId)
    offers shouldContain LunchOffer(0, "Frische Hähnchenbrust auf gebratenen Asianudeln, Kokos-Sauce", date("2015-06-18"), euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "Paprika-Gulasch oder Champignon-Gulasch mit Nudeln Sauerrahm", date("2015-06-18"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "2 Eier, Senfsauce, Buttergemüse, Kartoffeln, 1 Dessert", date("2015-06-18"), euro("4.90"), providerId)
    offers shouldContain LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-18"), euro("6.90"), providerId)

    offers shouldContain LunchOffer(0, "Spaghetti-Bolognese", date("2015-06-19"), euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "Kasslersteak mit Pommes frites und Weißkrautsalat", date("2015-06-19"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "Sommerpommes, 1 Dessert", date("2015-06-19"), euro("4.90"), providerId)
    offers shouldContain LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-19"), euro("6.90"), providerId)
  }

  @Test
  fun `resolve offers for Easter week of 2015-03-30`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-03-30.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers.filter { it.day == date("2015-03-30") } shouldHaveSize 5
    offers.filter { it.day == date("2015-03-31") } shouldHaveSize 5
    offers.filter { it.day == date("2015-04-01") } shouldHaveSize 5
    offers.filter { it.day == date("2015-04-02") } shouldHaveSize 4
    offers.filter { it.day == date("2015-04-03") } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for week of 2015-05-29`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2015-05-29.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers.filter { it.day == date("2015-05-25") } shouldHaveSize 0
    offers.filter { it.day == date("2015-05-26") } shouldHaveSize 5
    offers.filter { it.day == date("2015-05-27") } shouldHaveSize 5
    offers.filter { it.day == date("2015-05-28") } shouldHaveSize 5
    offers.filter { it.day == date("2015-05-29") } shouldHaveSize 4
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

    val offers = resolver().resolve(url)

    offers shouldHaveSize 24
    offers shouldContain LunchOffer(0, "Gefülltes Schnitzel „Toskaner Art“ mit Tomatensalat und Pommes frites", date("2015-08-11"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "Gefülltes Schnitzel „Cordon bleu“ mit buntem Krautsalat und Pommes frites", date("2015-08-12"), euro("5.70"), providerId)
    offers shouldContain LunchOffer(0, "Hacksteak „Toskana“ mit mediterraner Gemüsepfanne dazu Pommes frites", date("2015-08-14"), euro("5.20"), providerId)
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
    offers shouldContain LunchOffer(0, "Wochenangebot: Bunter Salat mit frischen Erdbeeren, gebratenem Hähnchenfleisch und hausgemachtem Erdbeer-Minze-Joghurt-Dressing", date("2016-04-11"), euro("5.90"), providerId)
  }

  @Test
  fun `resolve offers for week of 2018-09-14`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2018-09-14.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers shouldContain LunchOffer(0, "Serbischer Bohneneintopf", date("2018-09-10"), euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Spaghetti Bolognese mit Reibekäse", date("2018-09-10"), euro("5.90"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-03-11`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2019-03-11.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers shouldContain LunchOffer(0, "Linseneintopf mit gebratenen Wiener Würstchenscheiben (für Vegetarier extra)", date("2019-03-11"), euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Vegetarisch: frischer Blumenkohl mit sc. Hollandaise und Kartoffeln", date("2019-03-11"), euro("5.90"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-09-09`() {
    val url = javaClass.getResource("/menus/salt_n_pepper/2019-09-09.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 19
    offers shouldContain LunchOffer(0, "Vegetarisch: Spinat-Lasagne", date("2019-09-09"), euro("5.90"), providerId)
    offers shouldContain LunchOffer(0, "Lammbraten mit Speckbohnen und Petersilienkartoffeln", date("2019-09-10"), euro("6.30"), providerId)
  }
}
