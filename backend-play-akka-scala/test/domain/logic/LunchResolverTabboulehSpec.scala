package domain.logic

import java.time.LocalDate

import domain.models.{LunchOffer, LunchProvider}
import org.joda.money.Money
import org.scalamock.scalatest.MockFactory
import org.scalatest._

class LunchResolverTabboulehSpec extends FlatSpec with Matchers with MockFactory {

  ignore should "resolve offers for week of 2018-08-31" in {
    val url = getClass.getResource("/mittagsplaene/tabbouleh/2018-08-31.html")

    val offers = resolver.resolve(url)

    offers should have size 30

    // Woche vom 27.08.-31.08.2018
    offers should contain(LunchOffer(0, "Batata mehschije (Gefüllte Kartoffeln mit Rindfleisch in Tomatensauce dazu Basmatireis)", date("2018-08-27"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Hähnchenfilet mit frischem Saison-Gemüse an Kräuterkartoffeln", date("2018-08-27"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Pasta mit Pfifferlinge an Sahnerahm", date("2018-08-27"), euro("4.90"), Id))

    offers should contain(LunchOffer(0, "Blattspinat mit Salzkartoffeln und Spiegelei", date("2018-08-28"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Putenschnitzel an frischem Saison-Gemüse in Curryrahm", date("2018-08-28"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Pasta mit Tomatensauce und Käse überbacken", date("2018-08-28"), euro("4.90"), Id))

    offers should contain(LunchOffer(0, "Fasulie (Weiße Bohnen und Rindfleisch in Tomatensoße und Reis)", date("2018-08-29"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Rotbarsch in Dillsahnesauce dazu Prinzessbohnen", date("2018-08-29"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Vegetarische Lasagne mit Salatbeilage", date("2018-08-29"), euro("4.90"), Id))

    offers should contain(LunchOffer(0, "Fischfilet an Senfsauce dazu Mandelreis", date("2018-08-30"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Sommersalat mit Melone, Schafskäse und Kirschtomaten", date("2018-08-30"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Wiener Schnitzel mit Kartoffelsalat gebratener Reis mit Krabben und Ananas", date("2018-08-30"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Norwegischer Lachs auf Gemüserisotto", date("2018-08-31"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Gemüseteller mit Hähnchenspieß und Kartoffelgratin", date("2018-08-31"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Käsespätzle mit Salatbeilage", date("2018-08-31"), euro("4.90"), Id))

    // Woche vom 03.09.-07.09.2018
    offers should contain(LunchOffer(0, "Pasta mit Mangold, Zucchinistreifen dazu geröstete Mandeln in Kokosrahm", date("2018-09-03"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Sanft geschmortes Rindergulasch dazu cremiger Kartoffelpüree", date("2018-09-03"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Rinderfrikadelle mit Paprikasauce und Bratkartoffeln", date("2018-09-03"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Pasta mit frisches Gemüse der Saison in Tomatensauce", date("2018-09-04"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Hähnchenbrustfilet an Champignonsahnesauce und Kartoffelgratin", date("2018-09-04"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Curry Wurst mit Pommes", date("2018-09-04"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Pasta mit Limonensauce und Lachswürfel", date("2018-09-05"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Frisches Gemüse der Saison mit Käse überbacken dazu Kräuterkartoffeln", date("2018-09-05"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Rinderroulade wahlweise mit Kartoffelklöse oder Spätzle", date("2018-09-05"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Kartoffel-Brokkoli Gratin", date("2018-09-06"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Prinzessbohnen an Tomaten – Koriandersauce und Reis", date("2018-09-06"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Putengeschnetzeltes mit Kirschtomaten und Ruccola dazu frisches Gemüse der Saison", date("2018-09-06"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Gebratener Rotbarschfilet mit Blattspinat und Salzkartoffeln dazu Zitronensauce", date("2018-09-07"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Pasta an Hackfleisch – Tomatensauce", date("2018-09-07"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Bockwurst mit Kartoffelsalat", date("2018-09-07"), euro("5.90"), Id))
  }

  ignore should "resolve offers for week of 2018-09-14" in {
    val url = getClass.getResource("/mittagsplaene/tabbouleh/2018-09-07.html")

    val offers = resolver.resolve(url)

    offers should have size 31

    // Woche vom 10.09. - 14.09.2018
    offers should contain(LunchOffer(0, "Ofenfrische Lasagne mit Rinderhackfleisch", date("2018-09-10"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Libanesische Linsensuppe mit Fladenbrotcroutons", date("2018-09-10"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Pasta mit frischen Blattspinat an Kräuterrahm", date("2018-09-10"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Hähncheninnenfilet an Champignonsahnesauce, frisches Gemüse und Kartoffelgratin", date("2018-09-10"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Hähnchenbrustfilet Natur gebraten auf Basmatireis und Broccoliröschen", date("2018-09-11"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Eierpfannkuchen gefüllt mit frischem Gemüse", date("2018-09-11"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Kartoffel – Rindfleischeintopf mit frischem Koriander dazu Hirse", date("2018-09-11"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Norwegischer Lachs mit Mandelreis und Gemüse", date("2018-09-12"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Pasta mit Zucchinistreifen, frischen Champignons, Zwiebeln und Kirschtomaten", date("2018-09-12"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Kartoffel – Lauch- Rindfleisch- Gratin", date("2018-09-12"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Pasta mit Thunfisch, Kirschtomaten und Oliven an Tomatensauce", date("2018-09-13"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Putenbruststreifen mit Mangold, frische Champignons und Sherry Tomaten an Orangen-Chilli-Sauce dazu Salzkartoffeln", date("2018-09-13"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Fischfilet an Curryrahm mit frischem Gemüse und Reis", date("2018-09-13"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "Pasta mit fruchtiger Tomatensauce", date("2018-09-14"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Rotbarsch fritiert mit wahlweise Kräuterkartoffeln oder Kartoffelrösti", date("2018-09-14"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Rindergeschnetzeltes mit Kirschtomaten und Ruccola dazu Basmatireis", date("2018-09-14"), euro("5.90"), Id))
  }

  it should "resolve offers for week of 2019-04-01" in {
    val url = getClass.getResource("/mittagsplaene/tabbouleh/2019-04-01.html")

    val offers = resolver.resolve(url)

    offers should have size 15

    // Woche vom 01.04. - 05.04.2019
    offers should contain(LunchOffer(0, "Hähnchenfleisch mit Paprika in Tomatensauce, dazu Reis & grüne Bohnen", date("2019-04-01"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Nudeln Bolognese Art", date("2019-04-01"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Gemüseauflauf mit Mozzarella überbacken", date("2019-04-01"), euro("4.90"), Id))

    offers should contain(LunchOffer(0, "Hähnchenbrust in Currysauce & dazu Reis", date("2019-04-02"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Bohneneintopf mit Kartoffelwürfel & Rindfleisch", date("2019-04-02"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Kippe mit Joghurt & Salat", date("2019-04-02"), euro("4.90"), Id))

    offers should contain(LunchOffer(0, "Chicken Wings mit gebackenen Kartoffelecken", date("2019-04-03"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "gefüllte Paprikaschoten in Tomatensauce & dazu Reis", date("2019-04-03"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Okra-Schoten in Olivenöl gebraten mit Zwiebeln & dazu Reis", date("2019-04-03"), euro("4.90"), Id))

    offers should contain(LunchOffer(0, "Rinderleber mit Zwiebeln & Apfelscheiben, dazu Kartoffelpüree", date("2019-04-04"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Nudeln in Käsesauce", date("2019-04-04"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "überbackener Kartoffelauflauf mit Gemüse & Rinderhackfleisch", date("2019-04-04"), euro("5.90"), Id))

    offers should contain(LunchOffer(0, "gebackenes Fischfilet mit Erbsen & Kartoffeln", date("2019-04-05"), euro("5.90"), Id))
    offers should contain(LunchOffer(0, "Kartoffelsuppe mit Einlage", date("2019-04-05"), euro("4.90"), Id))
    offers should contain(LunchOffer(0, "überbackener Nudelauflauf mit Gemüse & Rinderhackfleisch", date("2019-04-05"), euro("5.90"), Id))
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverTabbouleh(validatorStub)
  }

  private val Id = LunchProvider.TABBOULEH.id

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)

  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
}