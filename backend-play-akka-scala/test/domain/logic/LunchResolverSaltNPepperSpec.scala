package domain.logic

import domain.models.{LunchProvider, LunchOffer}
import org.joda.money.Money
import java.time.LocalDate
import org.scalamock.scalatest.MockFactory

import org.scalatest._

class LunchResolverSaltNPepperSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve offers for week of 2015-06-19" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2015-06-19.html")

    val offers = resolver.resolve(url)

    offers should have size 24
    offers should contain (LunchOffer(0, "Grüne Bohneneintopf mit Kasslerfleisch", date("2015-06-15"), euro("3.90"), Id))
    offers should contain (LunchOffer(0, "Hähnchenkeule in Curry-Mango-Rahm, frische Buttermöhren, Kartoffeln", date("2015-06-15"), euro("5.20"), Id))
    offers should contain (LunchOffer(0, "Putenstreifen mit Pilzen in Käsesahnesauce auf Spaghetti", date("2015-06-15"), euro("5.70"), Id))
    offers should contain (LunchOffer(0, "Sahnemilchreis mit Kirschen, 1 Tasse Kaffee", date("2015-06-15"), euro("5.30"), Id))
    offers should contain (LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-15"), euro("6.90"), Id))

    offers should contain (LunchOffer(0, "Frische Paprikacremesuppe mit geröstetem Bacon", date("2015-06-16"), euro("3.90"), Id))
    offers should contain (LunchOffer(0, "Putenleber, hausgemachter Apfelrotkohl, hausgemachtes Kartoffelpüree", date("2015-06-16"), euro("5.20"), Id))
    offers should contain (LunchOffer(0, "Gefüllte Schnitzel „Cordon bleu“, mediterranem Gemüsegratin", date("2015-06-16"), euro("5.70"), Id))
    offers should contain (LunchOffer(0, "Blumenkohlgratin, 1 Dessert", date("2015-06-16"), euro("4.90"), Id))
    offers should contain (LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-16"), euro("6.90"), Id))

    offers should contain (LunchOffer(0, "Erbseneintopf mit Wiener Würstchenscheiben", date("2015-06-17"), euro("3.90"), Id))
    offers should contain (LunchOffer(0, "Frische Lachsfiletwürfel auf Bandnudeln, Spinat-Käse-Sauce", date("2015-06-17"), euro("5.70"), Id))
    offers should contain (LunchOffer(0, "Hühnerfrikassee mit Butterreis", date("2015-06-17"), euro("5.20"), Id))
    offers should contain (LunchOffer(0, "4 Kartoffelpuffer mit Apfelmus", date("2015-06-17"), euro("5.20"), Id))
    offers should contain (LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-17"), euro("6.90"), Id))

    offers should contain (LunchOffer(0, "Thai Suppe mit Hühnerfleisch und Frischem Koriander", date("2015-06-18"), euro("3.90"), Id))
    offers should contain (LunchOffer(0, "Frische Hähnchenbrust auf gebratenen Asianudeln, Kokos-Sauce", date("2015-06-18"), euro("5.20"), Id))
    offers should contain (LunchOffer(0, "Paprika-Gulasch oder Champignon-Gulasch mit Nudeln Sauerrahm", date("2015-06-18"), euro("5.70"), Id))
    offers should contain (LunchOffer(0, "2 Eier, Senfsauce, Buttergemüse, Kartoffeln, 1 Dessert", date("2015-06-18"), euro("4.90"), Id))
    offers should contain (LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-18"), euro("6.90"), Id))

    offers should contain (LunchOffer(0, "Spaghetti-Bolognese", date("2015-06-19"), euro("5.20"), Id))
    offers should contain (LunchOffer(0, "Kasslersteak mit Pommes frites und Weißkrautsalat", date("2015-06-19"), euro("5.70"), Id))
    offers should contain (LunchOffer(0, "Sommerpommes, 1 Dessert", date("2015-06-19"), euro("4.90"), Id))
    offers should contain (LunchOffer(0, "Wochenangebot: Rumpsteak (180gr.), Grillbutter, Pommes frites, Salatbeilage", date("2015-06-19"), euro("6.90"), Id))
  }

  it should "resolve offers for Easter week of 2015-03-30" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2015-03-30.html")

    val offers = resolver.resolve(url)

    offers should have size 19
    offers.filter(_.day == date("2015-03-30")) should have size 5
    offers.filter(_.day == date("2015-03-31")) should have size 5
    offers.filter(_.day == date("2015-04-01")) should have size 5
    offers.filter(_.day == date("2015-04-02")) should have size 4
    offers.filter(_.day == date("2015-04-03")) should have size 0
  }

  it should "resolve offers for week of 2015-05-29" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2015-05-29.html")

    val offers = resolver.resolve(url)

    offers should have size 19
    offers.filter(_.day == date("2015-05-25")) should have size 0
    offers.filter(_.day == date("2015-05-26")) should have size 5
    offers.filter(_.day == date("2015-05-27")) should have size 5
    offers.filter(_.day == date("2015-05-28")) should have size 5
    offers.filter(_.day == date("2015-05-29")) should have size 4
  }

  it should "resolve offers for week of 2015-07-11" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2015-07-11.html")

    val offers = resolver.resolve(url)

    offers should have size 24
  }

  it should "resolve offers for week of 2015-08-10" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2015-08-10.html")

    val offers = resolver.resolve(url)

    offers should have size 24
    offers should contain(LunchOffer(0, "Gefülltes Schnitzel „Toskaner Art“ mit Tomatensalat und Pommes frites", date("2015-08-11"), euro("5.70"), Id))
    offers should contain(LunchOffer(0, "Gefülltes Schnitzel „Cordon bleu“ mit buntem Krautsalat und Pommes frites", date("2015-08-12"), euro("5.70"), Id))
    offers should contain(LunchOffer(0, "Hacksteak „Toskana“ mit mediterraner Gemüsepfanne dazu Pommes frites", date("2015-08-14"), euro("5.20"), Id))
  }

  it should "resolve offers for week of 2016-02-16" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2016-02-16.html")

    val offers = resolver.resolve(url)

    offers should have size 24
  }

  it should "resolve offers for week of 2016-04-12" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2016-04-12.html")

    val offers = resolver.resolve(url)

    offers should have size 24
    offers should contain(LunchOffer(0, "Wochenangebot: Bunter Salat mit frischen Erdbeeren, gebratenem Hähnchenfleisch und hausgemachtem Erdbeer-Minze-Joghurt-Dressing", date("2016-04-11"), euro("5.90"), Id))
  }

  it should "resolve offers for week of 2018-09-14" in {
    val url = getClass.getResource("/mittagsplaene/salt_n_pepper/2018-09-14.html")

    val offers = resolver.resolve(url)

    offers should have size 19
    offers should contain(LunchOffer(0, "Serbischer Bohneneintopf", date("2018-09-10"), euro("3.50"), Id))
    offers should contain(LunchOffer(0, "Spaghetti Bolognese mit Reibekäse", date("2018-09-10"), euro("5.90"), Id))
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverSaltNPepper(validatorStub)
  }

  private val Id = LunchProvider.SALT_N_PEPPER.id
  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
}