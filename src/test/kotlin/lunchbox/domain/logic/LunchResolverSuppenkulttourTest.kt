package lunchbox.domain.logic /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverSuppenkulttourTest {

  private fun resolver() = LunchResolverSuppenkulttour(DateValidator.alwaysValid())
  private val providerId = SUPPENKULTTOUR.id

  @Test
  fun `resolve offers for week of 2015-03-02`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-03-02.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 20
    offers shouldContain LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-02"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-03"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-04"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-05"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-06"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-03"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-04"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-05"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-06"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-02"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-03"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-04"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-05"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-06"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Pastinaken-Romanescoscreme-Suppe: herrlich cremig und kräftig im Geschmack", date("2015-03-02"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Pasta mit Ossiwürstchengulasch: Hausrezept, sehr begehrt", date("2015-03-03"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Rote Beete Suppe „Derewenskija“: Russische Art der beliebten Rote Beete Suppe", date("2015-03-04"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Guay Tiau Kaek: Thai - Suppe mit Rind, Tofu, Soja, Erdnuss, Garnelen, Kokos, Curry, Limette, Mie Nudeln", date("2015-03-05"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Chili con Carne: mit Tomaten, Kidneybohnen, Mais, Chili, 100 % Rinderhackfleisch", date("2015-03-06"), euro("4.30"), providerId)
  }

  @Test
  fun `resolve offers for week of 2015-03-06`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-03-06.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40
    offers.filter { it.day == date("2015-03-02") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-03") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-04") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-05") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-06") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-09") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-10") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-11") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-12") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-13") } shouldHaveSize 4
  }

  @Test
  fun `resolve offers for week of 2015-03-11`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-03-11.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40
    offers shouldContain LunchOffer(0, "Pasta Tag - „Carne e Caprese”: Italienischer Hackfleischtopf mit Nudeln mit Tomaten, Mozzarrella, Hackfleisch, Basilikum", date("2015-03-10"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Südamerikanische Kartoffelsuppe: mit Mais, Tomaten, grünen Erbsen, Kartoffeln, Huhn", date("2015-03-16"), euro("4.10"), providerId)
    offers shouldContain LunchOffer(0, "Currywursttopf: mit Currywurst, Kartoffeln, Paprika, Tomatensoße", date("2015-03-16"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Lauch - Erbsensuppe: mit Möhren, gelben Erbsen, Sellerie, Kardamom, Lauch wahlweise Schinkenwürfel", date("2015-03-16"), euro("4.30"), providerId)

    offers shouldContain LunchOffer(0, "Urad DAL: mit Urad Linsen, Tumerik, schwarze Senfkörner, Ingwer dazu Joghurt verrührt mit braunem Zucker (wahlweise)", date("2015-03-16"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Pasta Pollo: mit Rahmgeschnetzeltem vom Huhn, Gemüse, Sahne", date("2015-03-17"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Winzersuppe - Käsesuppe: mit Schmelzkäse, Champignons, Rinderhack verfeinert mit Weißwein", date("2015-03-18"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Metaxa Suppe: mit Schweinefleisch, Paprika, Zwiebeln, Sahne, Metaxa,", date("2015-03-19"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Präsidentensuppe: mit Hackfleisch, Tomaten, Sauerkraut, Gewürzgurken, wahlweise saure Sahne", date("2015-03-20"), euro("4.30"), providerId)
  }

  @Test
  fun `resolve offers for Easter week of 2015-03-30`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-03-30.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 16
    offers.filter { it.day == date("2015-03-30") } shouldHaveSize 4
    offers.filter { it.day == date("2015-03-31") } shouldHaveSize 4
    offers.filter { it.day == date("2015-04-01") } shouldHaveSize 4
    offers.filter { it.day == date("2015-04-02") } shouldHaveSize 4
    offers.filter { it.day == date("2015-04-03") } shouldHaveSize 0

    offers shouldContain LunchOffer(0, "orientalische Tomatensuppe: mit roten Linsen & Cous Cous, Lime, Kokos", date("2015-03-30"), euro("4.30"), providerId)
  }

  @Test
  fun `resolve offers for week of 2015-04-13`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-04-13.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 20

    offers shouldContain LunchOffer(0, "Rosenkohleintopf: mit Kartoffeln, Möhren, Rosenkohl, Kohlrabi, wahlweise + Bratwurst", date("2015-04-13"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Pasta mit Ossiwürstchengulasch: mit Letscho, Würstchen, Paprika, süß - saure Note", date("2015-04-14"), euro("4.30"), providerId)
  }

  @Test
  fun `resolve offers for week of 2015-07-06`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-07-06.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 40

    offers shouldContain LunchOffer(0, "Hackfleisch - Kartoffelsuppe: mit Hackfleisch, Quark, Möhren, Kohlrabi, Kartoffeln", date("2015-07-06"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Chicken Cheese Soup: mit Huhn, Käse, Brokkoli, Möhren, Mais", date("2015-07-06"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Pasta Texas-Würstchengulasch: mit Würstchen, Paprika, Wachtelbohnen, Mais", date("2015-07-07"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Blumenkohlcremesuppe: mit Gorgonzola, Pastinaken- und Zucchiniraspel", date("2015-07-08"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Hühnerfrikassee mit Reis: mit Huhn, grünen Erbsen, Mais, Möhren, Champignons", date("2015-07-09"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Currywursttopf: mit Currywurst, Kartoffeln, Paprika, Tomatensoße", date("2015-07-10"), euro("4.30"), providerId)

    offers shouldContain LunchOffer(0, "Brokkoli-Kartoffelcremesuppe: wahlweise mit angerösteten Mandelblättchen", date("2015-07-16"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Dänische Erbsen: mit Möhren, gelben Erbsen, Sellerie, Kardamom, Lauch wahlweise Schinkenwürfel", date("2015-07-16"), euro("4.30"), providerId)
  }

  @Test
  fun `resolve offers for week of 2015-07-20`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-07-20.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 20

    offers shouldContain LunchOffer(0, "Pommersche Erbsensuppe: Erbsen, Wurzelgemüse, Thymian, Kassler", date("2015-07-20"), euro("4.30"), providerId)
    offers shouldContain LunchOffer(0, "Indische Blumenkohl Suppe: mit grünen Erbsen, gelbem Curry, Kokos und Tomate", date("2015-07-20"), euro("4.30"), providerId)
  }

  @Test
  fun `resolve offers for week of 2015-09-07`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2015-09-07.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2015-09-08") } shouldHaveSize 4
    offers.filter { it.day == date("2015-09-14") } shouldHaveSize 4
    offers shouldContain LunchOffer(0, "Braune Linsensuppe: Würstchen, Kartoffeln, Möhren, Lauch, Essig & Zucker", date("2015-09-14"), euro("4.50"), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-03-29`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2016-03-29.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 16

    offers.filter { it.day == date("2016-03-29") } shouldHaveSize 4
    offers.filter { it.day == date("2016-04-01") } shouldHaveSize 4
  }

  @Test
  fun `resolve offers for week of 2016-05-09`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2016-05-09.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 36

    offers.filter { it.day == date("2016-05-16") } shouldHaveSize 0 // Feiertag

    offers shouldContain LunchOffer(0, "Pasta „Berlin“: fruchtige, Curry-Tomatensoße, Currywurst, Paprika", date("2016-05-10"), euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Schnüsch: Gemüseeintopf aus Norddeutschland, saisonal quer durch den Garten mit Sahne & Milch", date("2016-05-11"), euro("4.50"), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-05-23`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2016-05-23.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2016-05-24") } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Soljanka: mit Fleisch, Kraut, Gurken, Saure Sahne", date("2016-05-27"), euro("4.50"), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-06-20`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2016-06-20.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2016-06-20") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-21") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-22") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-23") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-24") } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Pasta Bavaria: Gabelspaghetti mit original bayrischem Wurstgulach", date("2016-06-21"), euro("4.50"), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-06-27`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2016-06-27.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2016-06-27") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-28") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-29") } shouldHaveSize 4
    offers.filter { it.day == date("2016-06-30") } shouldHaveSize 4
    offers.filter { it.day == date("2016-07-01") } shouldHaveSize 4
  }

  @Test
  fun `resolve offers for week of 2017-01-30`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2017-01-30.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2017-01-30") } shouldHaveSize 4
    offers.filter { it.day == date("2017-01-31") } shouldHaveSize 4
    offers.filter { it.day == date("2017-02-01") } shouldHaveSize 4
    offers.filter { it.day == date("2017-02-02") } shouldHaveSize 4
    offers.filter { it.day == date("2017-02-03") } shouldHaveSize 4
  }

  @Test
  fun `resolve offers for week of 2017-09-04`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2017-09-04.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2017-09-04") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-05") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-06") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-07") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-08") } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Kartoffel-Zucchinicreme: mit Champignons, Paprika, wahlweise + Huhn", date("2017-09-04"), euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: Wiener, Karotten, Lauch, Kartoffeln, Majoran", date("2017-09-04"), euro("4.70"), providerId)
    offers shouldContain LunchOffer(0, "Pasta Pomodori: Soße aus gaaanz vielen frischen Tomaten, Parmesan, Rucola", date("2017-09-12"), euro("4.70"), providerId)
  }

  @Test
  fun `resolve offers for week of 2017-09-25`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2017-09-25.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2017-09-25") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-26") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-27") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-28") } shouldHaveSize 4
    offers.filter { it.day == date("2017-09-29") } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Pommersche Erbsensuppe: grüne Erbsen, Kartoffeln, Wurzelgemüse, Wiener", date("2017-09-29"), euro("4.70"), providerId)
    //    offers shouldContain LunchOffer(0, "Berliner Kartoffelsuppe: Wiener, Karotten, Lauch, Kartoffeln, Majoran", date("2017-09-04"), euro("4.70"), providerId)
    //    offers shouldContain LunchOffer(0, "Pasta Pomodori: Soße aus gaaanz vielen frischen Tomaten, Parmesan, Rucola", date("2017-09-12"), euro("4.70"), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-06-10`() {
    val url = javaClass.getResource("/menus/suppenkulttour/2019-06-10.html")

    val offers = resolver().resolve(url)

    offers.filter { it.day == date("2019-06-10") } shouldHaveSize 0
    offers.filter { it.day == date("2019-06-11") } shouldHaveSize 4
    offers.filter { it.day == date("2019-06-12") } shouldHaveSize 4
    offers.filter { it.day == date("2019-06-13") } shouldHaveSize 4
    offers.filter { it.day == date("2019-06-14") } shouldHaveSize 4

    offers shouldContain LunchOffer(0, "Pasta „Pollo“ (enthält: Huhn, Möhren, Zucchini, Cherrytomaten in einer cremigen Tomatensoße, verfeinert mit Olivenöl, Frischkäse, Mozzarella, Basilikum, Thymian", date("2019-06-11"), euro("4.70"), providerId)
  }
}
