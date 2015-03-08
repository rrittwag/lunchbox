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

  it should "resolve offers for week of 2015-02-16" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_16.02-20.02.2015neu.pdf")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain (LunchOffer(0,"Spaghetti Bolognese mit frischen Salat",date("2015-02-16"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Paniertes Schweineschnitzel Wiener Art mit Pommes und Salatbeilage",date("2015-02-16"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Rollmops mit Bratkartoffeln an Remouladensoße",date("2015-02-17"),euro("4.80"),Id))
    offers should contain (LunchOffer(0,"Paprikasahnegeschnetzeltes von der Pute mit Reis",date("2015-02-17"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Alles vom Schwein: Schweineschnitzel überbacken mit Ananas und Tomate-Mozzarella sowie Paniertes Schweineschnitzel dazu Gemüsevariation mit Pfeffersauce und Sahnesauce Pommes, Salzkartoffeln, Kartoffelspalten & Salatbuffet",date("2015-02-18"),euro("6.90"),Id))
    offers should contain (LunchOffer(0,"Grützwurst mit Sauerkraut an Salzkartoffeln",date("2015-02-19"),euro("5.20"),Id))
    offers should contain (LunchOffer(0,"Pasta Pfanne mit Hähnchenfleisch an leichtem Gemüse",date("2015-02-19"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Bauernroulade mit Speckbohnen und Salzkartoffeln",date("2015-02-20"),euro("5.20"),Id))
    offers should contain (LunchOffer(0,"Cordon Bleu mit Kaisergemüse und Salzkartoffeln",date("2015-02-20"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",date("2015-02-16"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",date("2015-02-17"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",date("2015-02-18"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",date("2015-02-19"),euro("5.50"),Id))
    offers should contain (LunchOffer(0,"Gemischter Salat mit Schweinefiletstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais )",date("2015-02-20"),euro("5.50"),Id))
  }

  it should "resolve offers for week of 2015-02-23" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_23.02-27.02.2015neu.pdf")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0, "Kotelett mit Bohnengemüse und Salzkartoffeln", date("2015-02-27"), euro("5.50"), Id))
  }

  it should "resolve offers for week of 2015-03-02" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_02.03-06.03.2015.pdf")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Gemüse-Nudel-Auflauf, Salatbeilage",date("2015-03-02"),euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Frikadellen an Zigeunersauce mit Pommes Frites",date("2015-03-02"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Lachsroulade „Fjord“ an Meerrettichsahnesauce mit Gemüsevariation und Salzkartoffeln",date("2015-03-03"),euro("5.00"),Id))
    offers should contain(LunchOffer(0,"Hähnchenbrust mit Tomate-Mozzarella überbacken dazu Kartoffelkroketten",date("2015-03-03"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Buffettag: Rinderbraten, Putensteaks mit Champignons, Kasslerbraten, Gemüsevariationen, Salzkartoffeln, Knödel und Salatbuffet",date("2015-03-04"),euro("6.90"),Id))
    offers should contain(LunchOffer(0,"Pastapfanne mit Hähnchenbruststreifen",date("2015-03-05"),euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Schweinebraten mit Rotkohl und Kartoffelklößen",date("2015-03-05"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gefüllte Kartoffeltaschen mit Kräuterfrischkäse an Sour Cream und Salat",date("2015-03-06"),euro("5.10"),Id))
    offers should contain(LunchOffer(0,"Grünkohltopf mit pikanter Knacker und Salzkartoffeln",date("2015-03-06"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",date("2015-03-02"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",date("2015-03-03"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",date("2015-03-04"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",date("2015-03-05"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Gemischter Salat mit Zanderstreifen (verschiedene Blattsalate, Gurke, Tomate, Paprika, Mais)",date("2015-03-06"),euro("5.50"),Id))
  }

  it should "resolve offers for week of 2015-03-09" in {
    val url = getClass.getResource("/mittagsplaene/hotel_am_ring/Mittagspause_09.03-13.03.15.pdf")

    val offers = new LunchResolverHotelAmRing().resolveFromPdf(url)

    offers should have size 14
    offers should contain(LunchOffer(0,"Wurstgulasch mit Penne",date("2015-03-09"),euro("4.50"),Id))
    offers should contain(LunchOffer(0,"Schlemmerschnitte mit Leipziger Allerlei & Petersilienkartoffeln",date("2015-03-09"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Sahnehering mit Salzkartoffeln & Salat",date("2015-03-10"),euro("4.80"),Id))
    offers should contain(LunchOffer(0,"Schweineschnitzel “Cordon bleu“ mit Kaisergemüse & Kroketten",date("2015-03-10"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Buffettag: Spießbraten, Hähnchenbrust, Bratwurst, Zigeunersauce, Gemüsevariation, Salzkartoffeln, Knödel & Dessertbuffet",date("2015-03-11"),euro("6.90"),Id))
    offers should contain(LunchOffer(0,"Kartoffelsuppe mit Wiener Würstchen, Dessert",date("2015-03-12"),euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Welsfilet in Eihülle an Petersiliensauce, glacierten Karotten & Salzkartoffeln",date("2015-03-12"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Currywurst mit Pommes Frites & Salat",date("2015-03-13"),euro("5.20"),Id))
    offers should contain(LunchOffer(0,"Hackröllchen mit Frischkäsefüllung und Gemüsereis",date("2015-03-13"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",date("2015-03-09"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",date("2015-03-10"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",date("2015-03-11"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",date("2015-03-12"),euro("5.50"),Id))
    offers should contain(LunchOffer(0,"Chef Salat (Käse, Kochschinken, Gurke, Tomate, Salat & Ei)",date("2015-03-13"),euro("5.50"),Id))
  }

  it should "parse date from PDF url" in {
    def parse(file: String): LocalDate = new LunchResolverHotelAmRing().parseMondayFromUrl(new URL("http://www.hotel-am-ring.de/" + HttpMittagspauseDir + file)).get

    parse("Mittagspause_09.02.-13.02.2015neu.pdf") should be (date("2015-02-09"))
    parse("Mittagspause_09.03-13.03.15.pdf") should be (date("2015-03-09"))
    parse("Mittagspause_02.03-06.03.2015.pdf") should be (date("2015-03-02"))
  }

  val Id = LunchProvider.HOTEL_AM_RING.id
  val HttpMittagspauseDir = "fileadmin/ordner_redaktion/dokumente/Mittagspause/"

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")

}