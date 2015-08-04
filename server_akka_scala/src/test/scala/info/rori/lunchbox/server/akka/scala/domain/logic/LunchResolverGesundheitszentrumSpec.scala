package info.rori.lunchbox.server.akka.scala.domain.logic

import java.net.URL

import info.rori.lunchbox.server.akka.scala.domain.model.{LunchOffer, LunchProvider}
import org.joda.money.Money
import org.joda.time.LocalDate
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.io.Codec

class LunchResolverGesundheitszentrumSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve Wochenpläne for facebook page of 2015-07-05" in {
    val content = readFileContent("/mittagsplaene/gesundheitszentrum_2015-07-05.json")

    val wochenplaene = resolver.parseWochenplaene(content)

    wochenplaene should contain (Wochenplan(LocalDate.parse("2015-07-06"), "723372204440300"))
    wochenplaene should contain (Wochenplan(LocalDate.parse("2015-06-22"), "715616615215859"))
    wochenplaene should contain (Wochenplan(LocalDate.parse("2015-06-15"), "712691465508374"))
  }

  it should "parse URL of biggest image for 2015-07-06" in {
    val content = readFileContent("/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-07-06_facebook.json")

    val url = resolver.parseUrlOfBiggestImage(content)

    url should be (Some(new URL("""https://scontent.xx.fbcdn.net/hphotos-xtp1/t31.0-8/11709766_723372204440300_7573791609611941912_o.jpg""")))
  }

  it should "resolve offers for week of 2015-06-22" in {
    val text = readFileContent("/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-06-22_ocr.txt")
    val week = weekOf("2015-06-22")

    val offers = resolver.resolveOffersFromText(week.monday, text)

    offers should have size 20
    offers should contain (LunchOffer(0, "Wirsingkohleintopf", week.monday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Riesenkrakauer mit Sauerkraut und Salzkartoffeln", week.monday, euro("4.40"), Id))
    offers should contain (LunchOffer(0, "Steak “au four“ mit Buttererbsen und Kroketten", week.monday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Tortellini mit Tomatensauce", week.monday, euro("3.90"), Id))

    offers should contain (LunchOffer(0, "Germknödel mit Vanillesauce und Mohn", week.tuesday, euro("3.20"), Id))
    offers should contain (LunchOffer(0, "Jägerschnitzel (panierte Jagdwurst) mit Tomatensauce und Nudeln", week.tuesday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Gefüllter Schweinerücken mit Gemüse und Salzkartoffeln", week.tuesday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Eier-Spinatragout mit Petersilienkartoffeln", week.tuesday, euro("4.00"), Id))

    offers should contain (LunchOffer(0, "Vegetarisches Chili", week.wednesday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Schmorkohl mit Hackfleisch und Salzkartoffeln", week.wednesday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Paprikahähnchenkeule mit Gemüse und Salzkartoffeln", week.wednesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Schollenfilet mit Gemüsereis", week.wednesday, euro("4.80"), Id))

    offers should contain (LunchOffer(0, "Pichelsteiner Gemüseeintopf", week.thursday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Schweinegeschnetzeltes mit Pilzen dazu Reis", week.thursday, euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Gebratenes Putensteak mit Champignons-Rahmsauce und Spätzle", week.thursday, euro("4.90"), Id))
    offers should contain (LunchOffer(0, "Nudel-Gemüse-Auflauf", week.thursday, euro("3.90"), Id))

    offers should contain (LunchOffer(0, "Brühreis", week.friday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Hühnerfrikassee mit Champignons, Spargel und Reis", week.friday, euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Holzfällersteak mit geschmorten Zwiebeln, Pilzen und Bratkartoffeln", week.friday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Kräuterrüherei mit Kartoffelpüree", week.friday, euro("3.80"), Id))
  }

  it should "resolve offers for week of 2015-06-29" in {
    val text = readFileContent("/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-06-29_ocr.txt")
    val week = weekOf("2015-06-29")

    val offers = resolver.resolveOffersFromText(week.monday, text)

    offers should have size 20
    offers should contain (LunchOffer(0, "Bunter Gemüseeintopf", week.monday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Hausgemachte Boulette mit Mischgemüse und Kartoffelpüree", week.monday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Schweinekammbraten mit Rotkohl und Klöße", week.monday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Pasta mit Pilzrahmsauce", week.monday, euro("3.80"), Id))

    offers should contain (LunchOffer(0, "Grießbrei mit roten Früchten", week.tuesday, euro("2.90"), Id))
    offers should contain (LunchOffer(0, "Blutwurst mit Sauerkraut und Salzkartoffeln", week.tuesday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Schweinerollbraten mit grünen Bohnen und Salzkartoffeln", week.tuesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Pilzragout mit Semmelknödel", week.tuesday, euro("4.00"), Id))

    offers should contain (LunchOffer(0, "Minestrone", week.wednesday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Gefüllte Paprikaschote mit Tomatensauce und Reis", week.wednesday, euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Hähnchenbrust mit Wirsingrahmgemüse und Salzkartoffeln", week.wednesday, euro("4.70"), Id))
    offers should contain (LunchOffer(0, "Pangasiusfilet mit Honig-Senf-Dillrahmsauce dazu Salzkartoffeln", week.wednesday, euro("4.60"), Id))

    offers should contain (LunchOffer(0, "Feuertopf (Kidneybohnen, grüne Bohnen, Jagdwurst)", week.thursday, euro("2.90"), Id))
    offers should contain (LunchOffer(0, "Paprikasahnegulasch mit Nudeln", week.thursday, euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Rieseneisbein mit Sauerkraut und Salzkartoffeln", week.thursday, euro("4.90"), Id))
    offers should contain (LunchOffer(0, "Kartoffel-Tomaten-Zucchini-Auflauf mit Mozzarella überbacken", week.thursday, euro("3.90"), Id))

    offers should contain (LunchOffer(0, "Porreeeintopf", week.friday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Hackbraten mit Mischgemüse und Salzkartoffeln", week.friday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Wildgulasch mit Rotkohl und Klöße", week.friday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Matjestopf mit Bohnensalat und Kräuterkartoffeln", week.friday, euro("4.20"), Id))
  }

  it should "resolve offers for badly OCR-able plan of week of 2015-06-15" in {
    val text = readFileContent("/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-06-15_ocr.txt")
    val week = weekOf("2015-06-15")

    val offers = resolver.resolveOffersFromText(week.monday, text)

    offers should have size 20
    offers should contain (LunchOffer(0, "Vegetarische Linsensuppe mit Vollkornbrötchen", week.monday, euro("3.20"), Id))
    offers should contain (LunchOffer(0, "Ofenfrischer Leberkäs mit einem Setzei, Zwiebelsauce und Kartoffelpüree", week.monday, euro("4.20"), Id))
    offers should contain (LunchOffer(0, "Schweinekammsteak mit Rahmchampions, dazu Pommes frites", week.monday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Rührei mit Sauce “Funghi“ und Kartoffelpüree", week.monday, euro("3.80"), Id))

    offers should contain (LunchOffer(0, "Milchreis mit Zucker und Zimt", week.tuesday, euro("3.00"), Id))
    offers should contain (LunchOffer(0, "2 Setzeier (außer Haus Rührei) mit Spinat und Salzkartoffeln", week.tuesday, euro("3.80"), Id))
    offers should contain (LunchOffer(0, "Gemischtes Gulasch aus Rind und Schwein dazu Nudeln", week.tuesday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Pasta mit Blattspinat, Tomaten und Reibekäse (analog)", week.tuesday, euro("4.00"), Id))

    offers should contain (LunchOffer(0, "Gulaschsuppe", week.wednesday, euro("3.20"), Id))
    offers should contain (LunchOffer(0, "2 Bifteki mit Zigeunersauce und Reis", week.wednesday, euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Rinderbraten mit Rotkohl und Klößen", week.wednesday, euro("4.80"), Id))
    offers should contain (LunchOffer(0, "Forelle ‘Müllerin Art“ mit zerlassener Butter und Salzkartoffeln", week.wednesday, euro("4.60"), Id))

    offers should contain (LunchOffer(0, "Kohlrabieintopf", week.thursday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Pfefferfleisch mit Buttergemüse dazu Reis", week.thursday, euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Schweinekammsteak mit Rahmchampions, dazu Pommes frites", week.thursday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Kartoffel-Tomaten-Zucchini-Auflauf", week.thursday, euro("3.90"), Id))

    offers should contain (LunchOffer(0, "Ungarischer Sauerkrauttopf", week.friday, euro("2.60"), Id))
    offers should contain (LunchOffer(0, "Cevapcici mit Zigeunersauce und Reis", week.friday, euro("4.10"), Id))
    offers should contain (LunchOffer(0, "Hähnchenschnitzel “Wiener Art“ mit Pfefferrahmsauce, Gemüse und Kroketten", week.friday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Sahnehering mit Bratkartoffeln", week.friday, euro("3.70"), Id))
  }

  it should "resolve offers for short week of 2015-07-06" in {
    val text = readFileContent("/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-07-06_ocr.txt")
    val week = weekOf("2015-07-06")

    val offers = resolver.resolveOffersFromText(week.monday, text)

    offers should have size 16
    offers should contain (LunchOffer(0, "Serbischer Bohneneintopf", week.monday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Rostbratwurst mit Sauerkraut und Kartoffelpüree", week.monday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Thüringer Rostbrätl mit geschmorten Zwiebeln und Bratkartoffeln", week.monday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Gebackener Camembert mit Preiselbeeren, 1/2 Birne und Baguette", week.monday, euro("3.90"), Id))

    offers should contain (LunchOffer(0, "Hefeklöße mit Früchten", week.tuesday, euro("3.00"), Id))
    offers should contain (LunchOffer(0, "Pasta “Schuta“ (Hackfleisch und Paprika) auf Nudeln", week.tuesday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Hausgemachte Rinderroulade mit Rotkohl und Klößen", week.tuesday, euro("4.90"), Id))
    offers should contain (LunchOffer(0, "Gefüllte Paprika (vegetarisch) mit Vollkornreis und Salat", week.tuesday, euro("4.20"), Id))

    offers should contain (LunchOffer(0, "Gelber Erbseneintopf", week.wednesday, euro("2.40"), Id))
    offers should contain (LunchOffer(0, "Königsberger Klopse mit Kapernsauce und Salzkartoffeln", week.wednesday, euro("4.00"), Id))
    offers should contain (LunchOffer(0, "Lachs mit Makkaroni und Möhren-Sellerie-Salat", week.wednesday, euro("4.80"), Id))
    offers should contain (LunchOffer(0, "Mexikanischer Nudelauflauf", week.wednesday, euro("4.00"), Id))

    offers should contain (LunchOffer(0, "Reitersuppe (Hackfleisch, grüne Bohnen, Champignons, Paprika)", week.thursday, euro("3.20"), Id))
    offers should contain (LunchOffer(0, "Chinapfanne mit Reis", week.thursday, euro("4.20"), Id))
    offers should contain (LunchOffer(0, "Gemischtes Gulasch aus Rind und Schwein dazu Nudeln", week.thursday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Kartoffel-Blumenkohl-Gratin", week.thursday, euro("3.90"), Id))

    offers.filter(_.day == week.friday) should have size 0
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverGesundheitszentrum(validatorStub)
  }

  private def readFileContent(path: String): String = {
    val url = getClass.getResource(path)

    implicit val codec = Codec.UTF8
    val source = scala.io.Source.fromURL(url)
    try source.mkString finally source.close()
  }

  private val Id = LunchProvider.GESUNDHEITSZENTRUM.id
  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")

  private def weekOf(dateString: String) = Week(date(dateString))

  case class Week(dateInWeek: LocalDate) {
    def monday = dateInWeek.withDayOfWeek(1)
    def tuesday = dateInWeek.withDayOfWeek(2)
    def wednesday = dateInWeek.withDayOfWeek(3)
    def thursday = dateInWeek.withDayOfWeek(4)
    def friday = dateInWeek.withDayOfWeek(5)
  }
}