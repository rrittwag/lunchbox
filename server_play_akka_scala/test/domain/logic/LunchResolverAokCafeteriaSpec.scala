package domain.logic

import domain.models.{LunchOffer, LunchProvider}
import java.time.{DayOfWeek, LocalDate}

import org.joda.money.Money
import org.scalatest._
import org.scalamock.scalatest.MockFactory

class LunchResolverAokCafeteriaSpec extends FlatSpec with Matchers with MockFactory {

  it should "resolve PDF links for 2015-03-17" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria_2015-03-17.html")

    val links = resolver.resolvePdfLinks(url)

    links should have size 2
    links should contain (HttpMittagspauseDir + "AOK_16.03.-20.03..pdf")
    links should contain (HttpMittagspauseDir + "AOK_23.03.-27.03..pdf")
  }

  it should "resolve PDF links for 2015-08-03" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria_2015-08-03.html")

    val links = resolver.resolvePdfLinks(url)

    links should have size 5
    links should contain (HttpMittagspauseDir + "AOK_27.07.-31.07.2015.pdf")
    links should contain (HttpMittagspauseDir + "AFA_03.08.-07.08..pdf")
    links should contain (HttpMittagspauseDir + "AOK_10.08.-14.08..pdf")
    links should contain (HttpMittagspauseDir + "AOK_17.08.-21.08..pdf")
    links should contain (HttpMittagspauseDir + "AOK_24.08.-28.08..pdf")
  }

  it should "resolve PDF links for 2017-05-01" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria_2017-05-01.html")

    val links = resolver.resolvePdfLinks(url)

    links should have size 1
    links should contain (HttpMittagspauseDir + "AOK01.05.2017-05.05.2017.pdf")
  }

  it should "resolve offers for week of 2015-03-20" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_16.03.-20.03..pdf")
    val week = weekOf(s"2015-03-20")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 15
    offers should contain (LunchOffer(0, "Linseneintopf mit Möhren, Kartoffeln und Sellerie", week.monday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, """Spaghetti "Napoli" mit Oliven, Peperoni, geriebenem Käse und frischem Salat""", week.monday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Hähnchenschenkel mit Rotkohl und Petersilienkartoffeln", week.monday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "grünes Erbsensüppchen", week.tuesday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Rührei mit Petersilie, Frühlingslauch, Kartoffelpüree und Salatbeilage", week.tuesday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Kartoffel- Hackfleisch- Auflauf mit Lauch, Fetakäse und fischem Salat", week.tuesday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "pürierte Blumenkohl- cremesuppe", week.wednesday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Hefeklöße mit Blaubeeren", week.wednesday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Hamburger Schnitzel mit Röster und Gewürzgurken", week.wednesday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "weißer Bohneneintopf", week.thursday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Kartoffelpuffer mit Apfelmus", week.thursday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "gebratenes Jägerschnitzel mit fruchtiger Tomatensauce und Penne", week.thursday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "Tagesangebot", week.friday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Brathering mit Kartoffelpüree und frischem Salat", week.friday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Schweinesteak mit Tomaten- Sauerrahm- häubchen, buntem Gemüse und Wildreis", week.friday, euro("5.30"), Id))
  }

  it should "resolve offers for week of 2015-03-27" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_23.03.-27.03..pdf")
    val week = weekOf(s"2015-03-27")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 15
    offers should contain (LunchOffer(0, "Kartoffelsuppe", week.monday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Spiegeleier mit Rahmspinat und Salzkartoffeln", week.monday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Chinapfanne mit Hähnchenfleisch, Reis und frischem Salat", week.monday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "Gemüsesuppe", week.tuesday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Sahnehering mit Butter, Salzkartoffeln und Salatbeilage", week.tuesday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Kochklops in Kapernsauce mit Salzkartoffeln und Rote Bete", week.tuesday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "Brokkolicremesuppe", week.wednesday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Eier in Senfsauce mit Salzkartoffeln und Rote Bete", week.wednesday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Schweinekotelett mit Rosenkohl und Salzkartoffeln", week.wednesday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "gelbes Erbsensüppchen", week.thursday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Schweineleber mit Apfel- Zwiebelsauce und Kartoffelpüree", week.thursday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Penne mit Champignonrahm und Kräuterdipp", week.thursday, euro("5.30"), Id))
    offers should contain (LunchOffer(0, "Tagesangebot", week.friday, euro("3.50"), Id))
    offers should contain (LunchOffer(0, "Schupfnudelpfanne mit Gemüse und frischem Salat", week.friday, euro("4.50"), Id))
    offers should contain (LunchOffer(0, "Schweinesteak mit Senfzwiebeln und Pommes Frites", week.friday, euro("5.30"), Id))
  }

  it should "resolve offers for Easter week of 2015-04-02" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_30.03.-02.04..pdf")
    val week = weekOf(s"2015-04-02")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 12
    offers.filter(_.day == week.monday) should have size 3
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 3
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 0
  }

  it should "resolve offers for Easter week of 2015-04-10" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_06.04.-10.04..pdf")
    val week = weekOf(s"2015-04-10")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 12
    offers.filter(_.day == week.monday) should have size 0
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 3
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 3
  }

  it should "resolve offers for week of 2015-07-20" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_20.07.-24.07.2015.pdf")
    val week = weekOf(s"2015-07-20")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 15
    offers.filter(_.day == week.monday) should have size 3
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 3
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 3
  }

  it should "resolve offers for week of 2015-08-03" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AFA_03.08.-07.08..pdf")
    val week = weekOf(s"2015-08-03")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 15
    offers.filter(_.day == week.monday) should have size 3
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 3
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 3
  }

  it should "resolve offers for week of 2017-05-01" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK01.05.2017-05.05.2017.pdf")
    val week = weekOf(s"2017-05-01")

    val offers = resolver.resolveFromPdf(url)

    offers should have size 12
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 3
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 3
  }

  it should "parse date from PDF url" in {
    def parse(lines: Seq[String]): LocalDate = resolver.parseMondayByTexts(lines).get

    parse(Seq("16.03.-20.03.2016")) should be (weekOf(s"2016-03-20").monday)
    parse(Seq("23.03.-27.03.")) should be (weekOf(s"$YearNow-03-27").monday)
    parse(Seq("16.03.-20.03.")) should be (weekOf(s"$YearNow-03-20").monday)
    parse(Seq("27.07.-31.07.2015 bla")) should be (weekOf(s"2015-07-27").monday)
  }

  private def resolver = {
    val validatorStub = stub[DateValidator]
    (validatorStub.isValid _).when(*).returning(true)
    new LunchResolverAokCafeteria(validatorStub)
  }

  val Id = LunchProvider.AOK_CAFETERIA.id
  val HttpMittagspauseDir = "fileadmin/ordner_redaktion/dokumente/Speisepl%C3%A4ne-Kantinen/"

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
  private val YearNow = LocalDate.now.getYear

  private def weekOf(dateString: String) = Week(date(dateString))

  case class Week(dateInWeek: LocalDate) {
    def monday = dateInWeek.`with`(DayOfWeek.MONDAY)
    def tuesday = dateInWeek.`with`(DayOfWeek.TUESDAY)
    def wednesday = dateInWeek.`with`(DayOfWeek.WEDNESDAY)
    def thursday = dateInWeek.`with`(DayOfWeek.THURSDAY)
    def friday = dateInWeek.`with`(DayOfWeek.FRIDAY)
  }
}