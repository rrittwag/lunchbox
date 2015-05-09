package info.rori.lunchbox.server.akka.scala.domain.logic

import java.net.URL

import info.rori.lunchbox.server.akka.scala.domain.model.{LunchProvider, LunchOffer}
import org.joda.money.Money
import org.joda.time.LocalDate

import org.scalatest._

class LunchResolverHotelAmRingSpec extends FlatSpec with Matchers {

  it should "resolve PDF links for 2015-02-16" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring_2015-02-16.html")

    val links = new LunchResolverHotelAmRing().resolvePdfLinks(url)

    links should have size 3
    links should contain (HttpMittagspauseDir + "Mittagspause_09.02.-13.02.2015neu.pdf")
    links should contain (HttpMittagspauseDir + "Mittagspause_23.02-27.02.2015neu.pdf")
    links should contain (HttpMittagspauseDir + "Mittagspause_16.02-20.02.2015neu.pdf")
  }

  it should "resolve PDF links for 2015-03-02" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring_2015-03-02.html")

    val links = new LunchResolverHotelAmRing().resolvePdfLinks(url)

    links should have size 3
    links should contain (HttpMittagspauseDir + "Mittagspause_09.03-13.03.15.pdf")
    links should contain (HttpMittagspauseDir + "Mittagspause_09.02.-13.02.2015neu.pdf")
    links should contain (HttpMittagspauseDir + "Mittagspause_02.03-06.03.2015.pdf")
  }

  it should "resolve PDF links for 2015-04-27" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring_2015-04-27.html")

    val links = new LunchResolverHotelAmRing().resolvePdfLinks(url)

    links should have size 3
    links should contain (HttpMittagspauseDir + "Mittagspause_20.04.-24.04.2015.pdf")
    links should contain (HttpMittagspauseDir + "Mittagspause_27.04.-01.05.pdf")
    links should contain (HttpMittagspauseDir + "Mittagspause_04.05.-08.05.15.pdf")
  }

  it should "resolve offers for week of 2015-02-20" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_16.02-20.02.2015neu.pdf")
    val week = weekOf(s"$YearNow-02-20")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain (LunchOffer(0,"Spaghetti Bolognese mit frischen Salat",week.monday,euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Paniertes Schweineschnitzel Wiener Art mit Pommes und Salatbeilage",week.monday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Rollmops mit Bratkartoffeln an Remouladensoße",week.tuesday,euro("4.80"),Id))
    offers should contain (LunchOffer(0,"Paprikasahnegeschnetzeltes von der Pute mit Reis",week.tuesday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Alles vom Schwein: Schweineschnitzel überbacken mit Ananas und Tomate-Mozzarella sowie Paniertes Schweineschnitzel dazu Gemüsevariation mit Pfeffersauce und Sahnesauce Pommes, Salzkartoffeln, Kartoffelspalten & Salatbuffet",week.wednesday,euro("6.90"),Id))
    offers should contain (LunchOffer(0,"Grützwurst mit Sauerkraut an Salzkartoffeln",week.thursday,euro("5.20"),Id))
    offers should contain (LunchOffer(0,"Pasta Pfanne mit Hähnchenfleisch an leichtem Gemüse",week.thursday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Bauernroulade mit Speckbohnen und Salzkartoffeln",week.friday,euro("5.20"),Id))
    offers should contain (LunchOffer(0,"Cordon Bleu mit Kaisergemüse und Salzkartoffeln",week.friday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Salat der Woche: Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",week.monday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Salat der Woche: Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",week.tuesday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Salat der Woche: Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",week.wednesday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Salat der Woche: Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",week.thursday,euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Salat der Woche: Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",week.friday,euro("5.50"),Id))
  }

  it should "resolve offers for week of 2015-02-27" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_23.02-27.02.2015neu.pdf")
    val week = weekOf(s"$YearNow-02-27")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0, "Kotelett mit Bohnengemüse und Salzkartoffeln", week.friday, euro("5.50"), Id))
  }

  it should "resolve offers for week of 2015-03-06" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_02.03-06.03.2015.pdf")
    val week = weekOf(s"$YearNow-03-06")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Gemüse-Nudel-Auflauf, Salatbeilage",week.monday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Frikadellen an Zigeunersauce mit Pommes Frites",week.monday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Lachsroulade „Fjord“ an Meerrettichsahnesauce mit Gemüsevariation und Salzkartoffeln",week.tuesday,euro("5.00"),Id))
    offers should contain(LunchOffer(0,"Hähnchenbrust mit Tomate-Mozzarella überbacken dazu Kartoffelkroketten",week.tuesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Buffettag: Rinderbraten, Putensteaks mit Champignons, Kasslerbraten, Gemüsevariationen, Salzkartoffeln, Knödel und Salatbuffet",week.wednesday,euro("6.90"),Id))
    offers should contain(LunchOffer(0,"Pastapfanne mit Hähnchenbruststreifen",week.thursday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Schweinebraten mit Rotkohl und Kartoffelklößen",week.thursday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gefüllte Kartoffeltaschen mit Kräuterfrischkäse an Sour Cream und Salat",week.friday,euro("5.10"),Id))
    offers should contain(LunchOffer(0,"Grünkohltopf mit pikanter Knacker und Salzkartoffeln",week.friday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",week.monday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",week.tuesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",week.wednesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",week.thursday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",week.friday,euro("5.50"),Id))
  }

  it should "resolve offers for week of 2015-03-13" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_09.03-13.03.15.pdf")
    val week = weekOf(s"$YearNow-03-13")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Wurstgulasch mit Penne",week.monday,euro("4.50"),Id))
    offers should contain(LunchOffer(0,"Schlemmerschnitte mit Leipziger Allerlei & Petersilienkartoffeln",week.monday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Sahnehering mit Salzkartoffeln & Salat",week.tuesday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Schweineschnitzel “Cordon bleu“ mit Kaisergemüse & Kroketten",week.tuesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Buffettag: Spießbraten, Hähnchenbrust, Bratwurst, Zigeunersauce, Gemüsevariation, Salzkartoffeln, Knödel & Dessertbuffet",week.wednesday,euro("6.90"),Id))
    offers should contain(LunchOffer(0,"Kartoffelsuppe mit Wiener Würstchen, Dessert",week.thursday,euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Welsfilet in Eihülle an Petersiliensauce, glacierten Karotten & Salzkartoffeln",week.thursday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Currywurst mit Pommes Frites & Salat",week.friday,euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Hackröllchen mit Frischkäsefüllung und Gemüsereis",week.friday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",week.monday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",week.tuesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",week.wednesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",week.thursday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",week.friday,euro("5.50"),Id))
  }

  it should "resolve offers for week of 2015-03-20" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_16.03.-20.03.2015.pdf")
    val week = weekOf(s"$YearNow-03-20")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Milchreis mit heißen Früchten",week.monday,euro("4.50"),Id))
    offers should contain(LunchOffer(0,"Hähnchenroulade (gefüllt mit Spinat und Frischkäse) mit Karotten-Kohlrabigemüse & Salzkartoffeln",week.monday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Schichtkohl mit Salzkartoffeln & Dessert",week.tuesday,euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Paniertes Schweinekotelett mit Brokkoli- & Blumenkohlröschen, Kartoffelkroketten",week.tuesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Buffettag: Rinderbraten, Putensteaks mit Champignons, Fischfilet, Gemüsevariation, Salzkartoffeln, Knödel & Salatbuffet mit Dressingauswahl",week.wednesday,euro("6.90"),Id))
    offers should contain(LunchOffer(0,"Zwei Minutensteaks mit frischen Champignons, Salat & Baguettebrot",week.thursday,euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Gebratenes Seelachsfilet an Kräuterrahmsauce mit Lotusgemüse & Wildreis",week.thursday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Königsberger Kochklops mit Salzkartoffeln und Salat",week.friday,euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Schweinesteak überbacken mit Tomate-Mozzarella, dazu Pommes frites & Salat",week.friday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate-Mozzarella mit Zwiebellauch und Basilikum",week.monday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate-Mozzarella mit Zwiebellauch und Basilikum",week.tuesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate-Mozzarella mit Zwiebellauch und Basilikum",week.wednesday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate-Mozzarella mit Zwiebellauch und Basilikum",week.thursday,euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate-Mozzarella mit Zwiebellauch und Basilikum",week.friday,euro("5.50"),Id))
  }

  it should "resolve offers for Easter week of 2015-04-03" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_30.04-03.04.15.pdf")
    val week = weekOf(s"$YearNow-04-03")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 11
    offers.filter(_.day == week.monday) should have size 3
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 2
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 0
  }

  it should "resolve offers for Easter week of 2015-04-10" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_06.04.15-10.04.15.pdf")
    val week = weekOf(s"$YearNow-04-10")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 11
    offers.filter(_.day == week.monday) should have size 0
    offers.filter(_.day == week.tuesday) should have size 3
    offers.filter(_.day == week.wednesday) should have size 2
    offers.filter(_.day == week.thursday) should have size 3
    offers.filter(_.day == week.friday) should have size 3
  }

  it should "resolve offers for week of 2015-04-17" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_13.04.-17.04.2015.pdf")
    val week = weekOf(s"$YearNow-04-17")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate - Mozzarella mit Lauch & Basilikum",week.monday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate - Mozzarella mit Lauch & Basilikum",week.tuesday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate - Mozzarella mit Lauch & Basilikum",week.wednesday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate - Mozzarella mit Lauch & Basilikum",week.thursday,euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Salat der Woche: Tomate - Mozzarella mit Lauch & Basilikum",week.friday,euro("4.80"),Id))
  }

  it should "resolve offers for week of 2015-04-24" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_20.04.-24.04.2015.pdf")
    val week = weekOf(s"$YearNow-04-24")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Hefeklöße mit Blaubeeren",week.monday,euro("4.50"),Id))
    offers should contain(LunchOffer(0,"Panierte Putenbrust mit Letscho & Bratkartoffeln",week.monday,euro("5.50"),Id))
  }

  it should "resolve offers for week of 2015-05-08" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_04.05.-08.05.15.pdf")
    val week = weekOf(s"$YearNow-05-08")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Zwei Minutensteaks mit frischen Champignons, Salat & Baguettebrot",week.thursday,euro("5.20"),Id))
    offers should contain(LunchOffer(0,"gebratenes Wildlachsfilet an Kräuterrahmsauce mit Lotusgemüse und Wildreis",week.thursday,euro("5.50"),Id))
  }

  it should "parse date from PDF url" in {
    def parse(file: String): LocalDate = new LunchResolverHotelAmRing().parseMondayFromUrl(new URL("http://www.hotel-am-ring.de/" + HttpMittagspauseDir + file)).get

    parse("Mittagspause_09.02.-13.02.2015neu.pdf") should be (weekOf(s"$YearNow-02-13").monday)
    parse("Mittagspause_09.03-13.03.15.pdf") should be (weekOf(s"$YearNow-03-13").monday)
    parse("Mittagspause_02.03-06.03.2015.pdf") should be (weekOf(s"$YearNow-03-06").monday)
    parse("Mittagspause_27.04.-01.05.pdf") should be (weekOf(s"$YearNow-05-01").monday)
  }

  val Id = LunchProvider.HOTEL_AM_RING.id
  val HttpMittagspauseDir = "fileadmin/ordner_redaktion/dokumente/Mittagspause/"

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
  private val YearNow = LocalDate.now.getYear

  private def weekOf(dateString: String) = Week(date(dateString))

  case class Week(dateInWeek: LocalDate) {
    def monday = dateInWeek.withDayOfWeek(1)
    def tuesday = dateInWeek.withDayOfWeek(2)
    def wednesday = dateInWeek.withDayOfWeek(3)
    def thursday = dateInWeek.withDayOfWeek(4)
    def friday = dateInWeek.withDayOfWeek(5)
  }
}