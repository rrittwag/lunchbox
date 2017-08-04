package domain.logic

import java.time.{DayOfWeek, LocalDate}

import domain.models.{LunchOffer, LunchProvider}
import org.joda.money.Money
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, Matchers}

class LunchResolverKrauthofSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve PDF links for 2017-05-01" in {
    val url = getClass.getResource("/mittagsplaene/daskrauthof_2017-05-01.html")

    val links = resolver.resolvePdfLinks(url)

    links should have size 1
    links should contain (HttpUploadDir + "2017/04/KRAUTHOF-Lunch-02.05.-05.05.2017.pdf")
  }

  it should "resolve offers for shorter week of 2017-05-01" in {
    val url = getClass.getResource("/mittagsplaene/daskrauthof/KRAUTHOF-Lunch-02.05.-05.05.2017.pdf")
    val week = weekOf(s"2017-05-01")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 35 // TODO: eigentlich nur 28 Offers! 1. Mai ist frei!

    offers.filter(_.day == week.monday) should have size 7
    offers.filter(_.day == week.tuesday) should have size 7
    offers.filter(_.day == week.wednesday) should have size 7
    offers.filter(_.day == week.thursday) should have size 7
    offers.filter(_.day == week.friday) should have size 7

    offers should contain(LunchOffer(0, "Karotten-Orangen-Suppe mit gebratenen Hähnchenstreifen", week.monday, euro("3.90"), Id))
    offers should contain(LunchOffer(0, "Knackige Blattsalate mit Kirschtomaten, Pinienkernen, marinierten Mozzarella, hausgemachtes Balsamicodressing", week.monday, euro("5.50"), Id))
    offers should contain(LunchOffer(0, "Gnocci mit würziger Blattspinat-Gorgonzolasauce, gerösteten Pinienkernen", week.monday, euro("6.60"), Id))
    offers should contain(LunchOffer(0, "Frische Tagliatelle mit saftigen Putenbruststreifen in Champignon-Kräuterrahm, mariniertem Ruccola", week.monday, euro("6.80"), Id))
    offers should contain(LunchOffer(0, "Gebratenes Rotbarschfilet auf Zitronenbuttersauce, Mandelbroccoli, Kräuterkartoffeln", week.monday, euro("7.20"), Id))
    offers should contain(LunchOffer(0, "Saftige Schweinesteaks mit Kräuterbutter, Wedges, Sour Cream, Salat", week.monday, euro("6.90"), Id))
    offers should contain(LunchOffer(0, "Feines Mousse von weißer Schokolade mit Kirschragout", week.monday, euro("3.10"), Id))
  }

  it should "resolve offers for shorter week of 2017-08-07" in {
    val url = getClass.getResource("/mittagsplaene/daskrauthof/KRAUTHOF-Lunch-07.08-11.08.2017.pdf")
    val week = weekOf(s"2017-08-07")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 35

    offers.filter(_.day == week.monday) should have size 7
    offers.filter(_.day == week.tuesday) should have size 7
    offers.filter(_.day == week.wednesday) should have size 7
    offers.filter(_.day == week.thursday) should have size 7
    offers.filter(_.day == week.friday) should have size 7

    offers should contain(LunchOffer(0, "Herzhaftes Kartoffelsüppchen mit Scheiben von gebratenen Pfefferbeißern", week.monday, euro("3.70"), Id))
    offers should contain(LunchOffer(0, "Bunte Blattsalate mit gegrillter Paprika, eingelegten Artischocken, Kirschtomaten, Kräutern aus dem KRAUTHOF", week.monday, euro("4.90"), Id))
    offers should contain(LunchOffer(0, "Knuspriges Sellerieschnitzel (vegetarisch) auf Tomaten-Zucchini-Gemüse, in Rosmarin geschwenkte Kartoffeln", week.monday, euro("5.10"), Id))
    offers should contain(LunchOffer(0, "KRAUTHOF-Pizza mit Hähnchen, Hinterschinken, roten Zwiebeln, Strauchtomaten, Champignons, Kräuter-Hollandaise", week.monday, euro("7.10"), Id))
    offers should contain(LunchOffer(0, "Gebratenes Wildlachsfilet auf Blattspinat, Kräuterkartoffeln, Zitrone", week.monday, euro("7.30"), Id))
    offers should contain(LunchOffer(0, "Saftiges Schweinesteak im Zwiebel-Senf-Mantel mit Bratkartoffeln, Salat", week.monday, euro("6.20"), Id))
    offers should contain(LunchOffer(0, "Fruchtiges Zitronenmousse mit Himbeercoulis", week.monday, euro("2.90"), Id))
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverKrauthof(validatorStub)
  }

  val Id = LunchProvider.DAS_KRAUTHOF.id
  val HttpUploadDir = "https://www.daskrauthof.de/wp-content/uploads/"

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
