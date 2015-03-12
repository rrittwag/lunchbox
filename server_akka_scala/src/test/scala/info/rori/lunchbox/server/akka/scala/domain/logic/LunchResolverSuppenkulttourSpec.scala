package info.rori.lunchbox.server.akka.scala.domain.logic

import info.rori.lunchbox.server.akka.scala.domain.model.{LunchProvider, LunchOffer}
import org.joda.money.Money
import org.joda.time.LocalDate
import org.scalatest.{Matchers, FlatSpec}

class LunchResolverSuppenkulttourSpec extends FlatSpec with Matchers {

  it should "resolve offers for week of 2015-03-02" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-03-02.html")

    val offers = new LunchResolverSuppenkulttour().resolve(url)

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

    val offers = new LunchResolverSuppenkulttour().resolve(url)

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

    val offers = new LunchResolverSuppenkulttour().resolve(url)

    offers should have size 40
    offers should contain (LunchOffer(0,"Pasta Tag - „Carne e Caprese”: Italienischer Hackfleischtopf mit Nudeln mit Tomaten, Mozzarrella, Hackfleisch, Basilikum",date("2015-03-10"),euro("4.30"),Id))
    offers should contain (LunchOffer(0,"Südamerikanische Kartoffelsuppe: mit Mais, Tomaten, grünen Erbsen, Kartoffeln, Huhn",date("2015-03-16"),euro("4.10"),Id))
    offers should contain (LunchOffer(0,"Currywursttopf: mit Currywurst, Kartoffeln, Paprika, Tomatensoße",date("2015-03-16"),euro("4.30"),Id))
    offers should contain (LunchOffer(0,"Lauch - Erbsensuppe: mit Möhren, gelben Erbsen, Sellerie, Kardamom, Lauch wahlweise Schinkenwürfel",date("2015-03-16"),euro("4.30"),Id))

    offers should contain (LunchOffer(0, "Urad DAL: mit Urad Linsen, Tumerik, schwarze Senfkörner, Ingwer dazu Joghurt verrührt mit braunem Zucker (wahlweise)",date("2015-03-16"),euro("4.30"),Id))
    offers should contain (LunchOffer(0, "Pasta Pollo: mit Rahmgeschnetzeltem vom Huhn, Gemüse, Sahne",date("2015-03-17"),euro("4.30"),Id))
    offers should contain (LunchOffer(0, "Winzersuppe - Käsesuppe: mit Schmelzkäse, Champignons, Rinderhack verfeinert mit Weißwein (2,3,4)",date("2015-03-18"),euro("4.30"),Id))
    offers should contain (LunchOffer(0, "Metaxa Suppe: mit Schweinefleisch, Paprika, Zwiebeln, Sahne, Metaxa,",date("2015-03-19"),euro("4.30"),Id))
    offers should contain (LunchOffer(0, "Präsidentensuppe: mit Hackfleisch, Tomaten, Sauerkraut, Gewürzgurken, wahlweise saure Sahne",date("2015-03-20"),euro("4.30"),Id))
  }

  private val Id = LunchProvider.SUPPENKULTTOUR.id

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)

  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
}