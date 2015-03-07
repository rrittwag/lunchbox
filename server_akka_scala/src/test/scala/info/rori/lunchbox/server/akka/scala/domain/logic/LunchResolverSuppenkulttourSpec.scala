package info.rori.lunchbox.server.akka.scala.domain.logic

import info.rori.lunchbox.server.akka.scala.domain.model.{LunchProvider, LunchOffer}
import org.joda.money.Money
import org.joda.time.LocalDate
import org.scalatest.{Matchers, FlatSpec}

class LunchResolverSuppenkulttourSpec extends FlatSpec with Matchers {

  "LunchResolver for Suppenkulttour" should "resolve offers for week of 2015-03-02" in {
    val url = getClass.getResource("/mittagsplaene/suppenkulttour_2015-03-02.html")

    val offers = new LunchResolverSuppenkulttour().resolve(url)

    offers should have size 20
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-03"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-04"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-05"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Hackfleisch-Tandoori-Suppe: mit roten Linsen, Blumenkohl, Hackfleisch", date("2015-03-06"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), Id))
    offers should contain(LunchOffer(0, "Berliner Kartoffelsuppe: mit Würstchen, Kartoffeln, Möhren, Lauch, Majoran", date("2015-03-02"), euro("4.30"), Id))
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

  private val Id = LunchProvider.SUPPENKULTTOUR.id

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)

  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
}