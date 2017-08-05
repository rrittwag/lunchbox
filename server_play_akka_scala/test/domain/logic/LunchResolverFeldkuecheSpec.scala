package domain.logic

import java.net.URL
import java.time.{DayOfWeek, LocalDate}

import domain.models.{LunchOffer, LunchProvider}
import infrastructure.OcrClient
import org.joda.money.Money
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

import scala.io.Codec

/**
 * User: robbel
 * Date: 23.10.16
 */
class LunchResolverFeldkuecheSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve image link for 2016-10-10" in {
    val url = getClass.getResource("/mittagsplaene/feldkueche_2016-10-10.html")

    val links = resolver.resolveImageLinks(url)

    links should have size 1
    links should contain (new URL(HttpMittagspauseDir + "teaserbox_25241614.jpg?t=1475778802"))
  }

  it should "resolve image link for 2017-07-17" in {
    val url = getClass.getResource("/mittagsplaene/feldkueche_2017-07-17.html")

    val links = resolver.resolveImageLinks(url)

    links should have size 1
    links should contain (new URL(HttpMittagspauseDir + "teaserbox_25241614.jpg?t=1500138543"))
  }

  it should "resolve offers for week of 2016-10-10" in {
    val text = readFileContent("/mittagsplaene/feldkueche/feldkueche_2016-10-10_ocr.txt")
    val week = weekOf("2016-10-10")

    val offers = resolver.resolveOffersFromText(text)

    offers should have size 10
    //offers should contain (LunchOffer(0, "Wurstgulasch Nudeln", week.monday, euro("4.80"), Id)) // OCR erkennt 4€ statt 4,80€
    offers should contain (LunchOffer(0, "Kartoffelsuppe mit Bockwurst", week.monday, euro("3.50"), Id))

    //offers should contain (LunchOffer(0, "Blutwurst Sauerkraut Kartoffeln", week.tuesday, euro("4.80"), Id)) // OCR erkennt 4€ statt 4,80€
    offers should contain (LunchOffer(0, "Grüne Bohneneintopf Brot", week.tuesday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Gulasch mit Nudeln", week.wednesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Erbseneintopf mit Bockwurst", week.wednesday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Eisbein Sauerkraut Kartoffeln", week.thursday, euro("5.00"), Id))
    offers should contain (LunchOffer(0, "Brühnudeln mit Hähnchenfleisch Brot", week.thursday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Schichtkohl mit Kartoffeln", week.friday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Weißkohl mit Brot", week.friday, euro("3.00"), Id))
  }

  it should "resolve offers for week of 2016-10-17" in {
    val text = readFileContent("/mittagsplaene/feldkueche/feldkueche_2016-10-17_ocr.txt")
    val week = weekOf("2016-10-17")

    val offers = resolver.resolveOffersFromText(text)

    offers should have size 11
    offers should contain (LunchOffer(0, "Jägerschnitzel Tomatensauce Nudeln", week.monday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Mohreneintopf Brot", week.monday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Senfeier Kartoffeln Krautsalat", week.tuesday, euro("4.20"), Id))
    offers should contain (LunchOffer(0, "Brühreis mit Hähnchenfleisch Brot", week.tuesday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Königsberger Klopse Kartoffeln", week.wednesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Kohlrabieintopf Brot", week.wednesday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Eisbein Sauerkraut Kartoffeln", week.thursday, euro("5.00"), Id))
    offers should contain (LunchOffer(0, "Linseneintopf mit Bockwurst", week.thursday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Linseneintopf mit Knacker", week.thursday, euro("3.90"), Id))

    offers should contain (LunchOffer(0, "Sahnegeschnetzeltes Nudeln", week.friday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Wirsingkohleintopf Brot", week.friday, euro("3.00"), Id))
  }

  it should "resolve offers for week of 2016-10-24" in {
    val text = readFileContent("/mittagsplaene/feldkueche/feldkueche_2016-10-24_ocr.txt")
    val week = weekOf("2016-10-24")

    val offers = resolver.resolveOffersFromText(text)

    offers should have size 10
    offers should contain (LunchOffer(0, "Nudeln mit Bolognese", week.monday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Grüne Bohneneintopf Brot", week.monday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Boulette Mischgemüse Kartoffeln", week.tuesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Kartoffelsuppe mit Bockwurst", week.tuesday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Gulasch Klöße Rotkohl", week.wednesday, euro("4.80"), Id))
    offers should contain (LunchOffer(0, "Möhreneintopf Brot", week.wednesday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Eisbein Sauerkraut Kartoffeln", week.thursday, euro("5.00"), Id))
    offers should contain (LunchOffer(0, "Erbseneintopf mit Bockwurst", week.thursday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Käse - Lauchsuppe Brot", week.friday, euro("4.20"), Id))
    offers should contain (LunchOffer(0, "Chili con Carne Brot", week.friday, euro("4.20"), Id))
  }

  it should "resolve offers for week of 2016-10-31" in {
    val text = readFileContent("/mittagsplaene/feldkueche/feldkueche_2016-10-31_ocr.txt")
    val week = weekOf("2016-10-31")

    val offers = resolver.resolveOffersFromText(text)

    offers should have size 10
    offers should contain (LunchOffer(0, "Wurstgulasch mit Nudeln", week.monday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Soljanka mit Brot", week.monday, euro("3.80"), Id))

    offers should contain (LunchOffer(0, "Blutwurst Sauerkraut Kartoffeln", week.tuesday, euro("4.80"), Id))
    offers should contain (LunchOffer(0, "Weißkohleintopf Brot", week.tuesday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Gulasch mit Nudeln", week.wednesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Erbseneintopf mit Bockwurst", week.wednesday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Eisbein Sauerkraut Kartoffeln", week.thursday, euro("5.00"), Id))
    offers should contain (LunchOffer(0, "Brühnudeln mit Hähnchenfleisch Brot", week.thursday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Kesselgulasch mit Brot", week.friday, euro("4.20"), Id))
    offers should contain (LunchOffer(0, "Wirsingkohleintopf Brot", week.friday, euro("3.00"), Id))
  }

  it should "resolve offers for week of 2017-02-06" in {
    val text = readFileContent("/mittagsplaene/feldkueche/feldkueche_2017-02-06_ocr.txt")
    val week = weekOf("2017-02-06")

    val offers = resolver.resolveOffersFromText(text)

    offers should have size 10
    offers should contain (LunchOffer(0, "Nudeln Carbonara", week.monday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Wirsingkohleineintopf mit Brot", week.monday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Schichtkohl mit Kartoffeln", week.tuesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Rosenkohleintopf mit Brot", week.tuesday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Bouletten Mischgemüse Kartoffeln", week.wednesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Kohlrabieintopf mit Brot", week.wednesday, euro("3.00"), Id))

    offers should contain (LunchOffer(0, "Eisbein Sauerkraut Kartoffeln", week.thursday, euro("5.00"), Id))
    offers should contain (LunchOffer(0, "Kartoffelsuppe mit Bockwurst", week.thursday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Gulasch mit Nudeln", week.friday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Soljanka mit Brot", week.friday, euro("3.80"), Id))
  }

  it should "resolve offers for week of 2017-07-17" in {
    val text = readFileContent("/mittagsplaene/feldkueche/feldkueche_2017-07-17_ocr.txt")
    val week = weekOf("2017-07-17")

    val offers = resolver.resolveOffersFromText(text)

    offers should have size 10
    offers should contain (LunchOffer(0, "Nudeln Bolognese", week.monday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Bunter Gemüseeintopf Brot", week.monday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Blutwurst Sauerkraut Kartoffeln", week.tuesday, euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Brühnudeln mit Hähnchenfleisch Brot", week.tuesday, euro("3.50"), Id))

    offers should contain (LunchOffer(0, "Jägerschnitzel Tomatensauce Nudeln", week.wednesday, euro("4.60"), Id))
    offers should contain (LunchOffer(0, "Erbseneintopf mit Bockwurst", week.wednesday, euro("4.00"), Id))

    offers should contain (LunchOffer(0, "Gulasch mit Kartoffeln und Rotkohl", week.thursday, euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Kräuterquark Kartoffeln Krautsalat", week.thursday, euro("4.00"), Id))

    offers should contain (LunchOffer(0, "Schweinebraten Sauerkraut Kartoffeln", week.friday, euro("4.30"), Id))
    offers should contain (LunchOffer(0, "Soljanka mit Brot", week.friday, euro("3.80"), Id))
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)

    val ocrClientStub = stub[OcrClient]

    new LunchResolverFeldkueche(validatorStub, ocrClientStub)
  }

  private def readFileContent(path: String): String = {
    val url = getClass.getResource(path)

    implicit val codec = Codec.UTF8
    val source = scala.io.Source.fromURL(url)
    try source.mkString finally source.close()
  }

  val Id = LunchProvider.FELDKUECHE.id
  val HttpMittagspauseDir = "https://www.feldkuechebkarow.de/s/cc_images/"

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")

  private def weekOf(dateString: String) = Week(date(dateString))

  case class Week(dateInWeek: LocalDate) {
    def monday = dateInWeek.`with`(DayOfWeek.MONDAY)
    def tuesday = dateInWeek.`with`(DayOfWeek.TUESDAY)
    def wednesday = dateInWeek.`with`(DayOfWeek.WEDNESDAY)
    def thursday = dateInWeek.`with`(DayOfWeek.THURSDAY)
    def friday = dateInWeek.`with`(DayOfWeek.FRIDAY)
  }

}
