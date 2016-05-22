package domain.logic

import domain.models.{LunchProvider, LunchOffer}
import org.joda.money.Money
import java.time.LocalDate
import org.scalamock.scalatest.MockFactory
import org.scalatest.{Matchers, FlatSpec}

class LunchResolverSuppenkulttourSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve offers for week of 2015-03-02" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-03-02.html")

    val offers = resolver.resolve(url)

    offers should have size 20
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-03"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-04"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-05"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-06"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-03"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-04"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-05"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-06"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-03"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-04"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-05"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Süßkartoffel-Möhrensuppe: mit Erdnuss, Kokos, African Rub wahlweise mit Putenfleisch - probieren ist Pflicht", date("2015-03-06"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Pastinaken-Romanescoscreme-Suppe: herrlich cremig und kräftig im Geschmack", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Pasta mit Ossiwürstchengulasch: Hausrezept, sehr begehrt", date("2015-03-03"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Rote Beete Suppe „Derewenskija“: Russische Art der beliebten Rote Beete Suppe", date("2015-03-04"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Guay Tiau Kaek: Thai - Suppe mit Rind, Tofu, Soja, Erdnuss, Garnelen, Kokos, Curry, Limette, Mie Nudeln", date("2015-03-05"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Chili con Carne: mit Tomaten, Kidneybohnen, Mais, Chili, 100 % Rinderhackfleisch", date("2015-03-06"), euro("4.30"), Id))
  }

  it should "resolve offers for week of 2015-03-06" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-03-06.html")

    val offers = resolver.resolve(url)

    offers should have size 40
    offers.filter(_.day == date("2015-03-02")) should have size 4
    offers.filter(_.day == date("2015-03-03")) should have size 4
    offers.filter(_.day == date("2015-03-04")) should have size 4
    offers.filter(_.day == date("2015-03-05")) should have size 4
    offers.filter(_.day == date("2015-03-06")) should have size 4
    offers.filter(_.day == date("2015-03-09")) should have size 4
    offers.filter(_.day == date("2015-03-10")) should have size 4
    offers.filter(_.day == date("2015-03-11")) should have size 4
    offers.filter(_.day == date("2015-03-12")) should have size 4
    offers.filter(_.day == date("2015-03-13")) should have size 4
  }

  it should "resolve offers for week of 2015-03-11" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-03-11.html")

    val offers = resolver.resolve(url)

    offers should have size 40
    offers should contain (LunchOffer(0, "Pasta Tag - „Carne e Caprese”: Italienischer Hackfleischtopf mit Nudeln mit Tomaten, Mozzarrella, Hackfleisch, Basilikum", date("2015-03-10"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Südamerikanische Kartoffelsuppe: mit Mais, Tomaten, grünen Erbsen, Kartoffeln, Huhn", date("2015-03-16"), euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Currywursttopf: mit Currywurst, Kartoffeln, Paprika, Tomatensoße", date("2015-03-16"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Lauch - Erbsensuppe: mit Möhren, gelben Erbsen, Sellerie, Kardamom, Lauch wahlweise Schinkenwürfel", date("2015-03-16"), euro("4.30"), Id))

    offers should contain (LunchOffer(0, "Urad DAL: mit Urad Linsen, Tumerik, schwarze Senfkörner, Ingwer dazu Joghurt verrührt mit braunem Zucker (wahlweise)", date("2015-03-16"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Pasta Pollo: mit Rahmgeschnetzeltem vom Huhn, Gemüse, Sahne", date("2015-03-17"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Winzersuppe - Käsesuppe: mit Schmelzkäse, Champignons, Rinderhack verfeinert mit Weißwein", date("2015-03-18"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Metaxa Suppe: mit Schweinefleisch, Paprika, Zwiebeln, Sahne, Metaxa,", date("2015-03-19"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Präsidentensuppe: mit Hackfleisch, Tomaten, Sauerkraut, Gewürzgurken, wahlweise saure Sahne", date("2015-03-20"), euro("4.30"), Id))
  }

  it should "resolve offers for Easter week of 2015-03-30" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-03-30.html")

    val offers = resolver.resolve(url)

    offers should have size 16
    offers.filter(_.day == date("2015-03-30")) should have size 4
    offers.filter(_.day == date("2015-03-31")) should have size 4
    offers.filter(_.day == date("2015-04-01")) should have size 4
    offers.filter(_.day == date("2015-04-02")) should have size 4
    offers.filter(_.day == date("2015-04-03")) should have size 0

    offers should contain (LunchOffer(0, "orientalische Tomatensuppe: mit roten Linsen & Cous Cous, Lime, Kokos", date("2015-03-30"), euro("4.30"), Id))
  }

  it should "resolve offers for week of 2015-04-13" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-04-13.html")

    val offers = resolver.resolve(url)

    offers should have size 20

    offers should contain (LunchOffer(0, "Rosenkohleintopf: mit Kartoffeln, Möhren, Rosenkohl, Kohlrabi, wahlweise + Bratwurst", date("2015-04-13"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Pasta mit Ossiwürstchengulasch: mit Letscho, Würstchen, Paprika, süß - saure Note", date("2015-04-14"), euro("4.30"), Id))
  }

  it should "resolve offers for week of 2015-07-06" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-07-06.html")

    val offers = resolver.resolve(url)

    offers should have size 40

    offers should contain (LunchOffer(0, "Hackfleisch - Kartoffelsuppe: mit Hackfleisch, Quark, Möhren, Kohlrabi, Kartoffeln", date("2015-07-06"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Chicken Cheese Soup: mit Huhn, Käse, Brokkoli, Möhren, Mais", date("2015-07-06"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Pasta Texas-Würstchengulasch: mit Würstchen, Paprika, Wachtelbohnen, Mais", date("2015-07-07"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Blumenkohlcremesuppe: mit Gorgonzola, Pastinaken- und Zucchiniraspel", date("2015-07-08"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Hühnerfrikassee mit Reis: mit Huhn, grünen Erbsen, Mais, Möhren, Champignons", date("2015-07-09"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Currywursttopf: mit Currywurst, Kartoffeln, Paprika, Tomatensoße", date("2015-07-10"), euro("4.30"), Id))

    offers should contain (LunchOffer(0, "Brokkoli-Kartoffelcremesuppe: wahlweise mit angerösteten Mandelblättchen", date("2015-07-16"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Dänische Erbsen: mit Möhren, gelben Erbsen, Sellerie, Kardamom, Lauch wahlweise Schinkenwürfel", date("2015-07-16"), euro("4.30"), Id))
  }

  it should "resolve offers for week of 2015-07-20" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-07-20.html")

    val offers = resolver.resolve(url)

    offers should have size 20

    offers should contain (LunchOffer(0, "Pommersche Erbsensuppe: Erbsen, Wurzelgemüse, Thymian, Kassler", date("2015-07-20"), euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Indische Blumenkohl Suppe: mit grünen Erbsen, gelbem Curry, Kokos und Tomate", date("2015-07-20"), euro("4.30"), Id))
  }

  it should "resolve offers for week of 2015-09-07" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-09-07.html")

    val offers = resolver.resolve(url)

    offers.filter(_.day == date("2015-09-08")) should have size 4
    offers.filter(_.day == date("2015-09-14")) should have size 4
    offers should contain (LunchOffer(0, "Braune Linsensuppe: Würstchen, Kartoffeln, Möhren, Lauch, Essig & Zucker", date("2015-09-14"), euro("4.50"), Id))
  }

  it should "resolve offers for week of 2016-03-29" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2016-03-29.html")

    val offers = resolver.resolve(url)

    offers should have size 16

    offers.filter(_.day == date("2016-03-29")) should have size 4
    offers.filter(_.day == date("2016-04-01")) should have size 4
  }

  it should "resolve offers for week of 2016-05-09" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2016-05-09.html")

    val offers = resolver.resolve(url)

    offers should have size 36

    offers.filter(_.day == date("2016-05-16")) shouldBe empty // Feiertag

    offers should contain (LunchOffer(0, "Pasta „Berlin“: fruchtige, Curry-Tomatensoße, Currywurst, Paprika", date("2016-05-10"), euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Schnüsch: Gemüseeintopf aus Norddeutschland, saisonal quer durch den Garten mit Sahne & Milch", date("2016-05-11"), euro("4.50"), Id))
  }

  it should "resolve offers for week of 2016-05-23" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2016-05-23.html")

    val offers = resolver.resolve(url)

    offers.filter(_.day == date("2016-05-24")) should have size 4

    offers should contain (LunchOffer(0, "Soljanka: mit Fleisch, Kraut, Gurken, Saure Sahne", date("2016-05-27"), euro("4.50"), Id))
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverSuppenkulttour(validatorStub)
  }

  private val Id = LunchProvider.SUPPENKULTTOUR.id

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)

  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
}