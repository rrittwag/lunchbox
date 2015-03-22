package info.rori.lunchbox.server.akka.scala.domain.logic

import java.net.URL

import info.rori.lunchbox.server.akka.scala.domain.model.{LunchOffer, LunchProvider}
import org.joda.money.Money
import org.joda.time.LocalDate
import org.scalatest._

class LunchResolverAokCafeteriaSpec extends FlatSpec with Matchers {

  it should "resolve PDF links for 2015-03-17" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria_2015-03-17.html")

    val links = new LunchResolverAokCafeteria().resolvePdfLinks(url)

    links should have size 2
    links should contain (HttpMittagspauseDir + "AOK_16.03.-20.03..pdf")
    links should contain (HttpMittagspauseDir + "AOK_23.03.-27.03..pdf")
  }

  it should "resolve offers for week of 2015-03-16" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_16.03.-20.03..pdf")

    val offers = new LunchResolverAokCafeteria().resolveFromPdf(url)

    offers should have size 15
    offers should contain (LunchOffer(0,"Linseneintopf mit Möhren, Kartoffeln und Sellerie",date(s"$YearNow-03-16"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"""Spaghetti "Napoli" mit Oliven, Peperoni, geriebenem Käse und frischem Salat""",date(s"$YearNow-03-16"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Hähnchenschenkel mit Rotkohl und Petersilienkartoffeln",date(s"$YearNow-03-16"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"grünes Erbsensüppchen",date(s"$YearNow-03-17"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Rührei mit Petersilie, Frühlingslauch, Kartoffelpüree und Salatbeilage",date(s"$YearNow-03-17"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Kartoffel- Hackfleisch- Auflauf mit Lauch, Fetakäse und fischem Salat",date(s"$YearNow-03-17"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"pürierte Blumenkohl- cremesuppe",date(s"$YearNow-03-18"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Hefeklöße mit Blaubeeren",date(s"$YearNow-03-18"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Hamburger Schnitzel mit Röster und Gewürzgurken",date(s"$YearNow-03-18"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"weißer Bohneneintopf",date(s"$YearNow-03-19"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Kartoffelpuffer mit Apfelmus",date(s"$YearNow-03-19"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"gebratenes Jägerschnitzel mit fruchtiger Tomatensauce und Penne",date(s"$YearNow-03-19"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"Tagesangebot",date(s"$YearNow-03-20"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Brathering mit Kartoffelpüree und frischem Salat",date(s"$YearNow-03-20"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Schweinesteak mit Tomaten- Sauerrahm- häubchen, buntem Gemüse und Wildreis",date(s"$YearNow-03-20"),euro("5.30"),Id))
  }

  it should "resolve offers for week of 2015-03-23" in {
    val url = getClass.getResource("/mittagsplaene/aok_cafeteria/AOK_23.03.-27.03..pdf")

    val offers = new LunchResolverAokCafeteria().resolveFromPdf(url)

    offers should have size 15
    offers should contain (LunchOffer(0,"Kartoffelsuppe",date(s"$YearNow-03-23"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Spiegeleier mit Rahmspinat und Salzkartoffeln",date(s"$YearNow-03-23"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Chinapfanne mit Hähnchenfleisch, Reis und frischem Salat",date(s"$YearNow-03-23"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"Gemüsesuppe",date(s"$YearNow-03-24"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Sahnehering mit Butter, Salzkartoffeln und Salatbeilage",date(s"$YearNow-03-24"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Kochklops in Kapernsauce mit Salzkartoffeln und Rote Bete",date(s"$YearNow-03-24"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"Brokkolicremesuppe",date(s"$YearNow-03-25"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Eier in Senfsauce mit Salzkartoffeln und Rote Bete",date(s"$YearNow-03-25"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Schweinekotelett mit Rosenkohl und Salzkartoffeln",date(s"$YearNow-03-25"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"gelbes Erbsensüppchen",date(s"$YearNow-03-26"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Schweineleber mit Apfel- Zwiebelsauce und Kartoffelpüree",date(s"$YearNow-03-26"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Penne mit Champignonrahm und Kräuterdipp",date(s"$YearNow-03-26"),euro("5.30"),Id))
    offers should contain (LunchOffer(0,"Tagesangebot",date(s"$YearNow-03-27"),euro("3.50"),Id))
    offers should contain (LunchOffer(0,"Schupfnudelpfanne mit Gemüse und frischem Salat",date(s"$YearNow-03-27"),euro("4.50"),Id))
    offers should contain (LunchOffer(0,"Schweinesteak mit Senfzwiebeln und Pommes Frites",date(s"$YearNow-03-27"),euro("5.30"),Id))
  }

  it should "parse date from PDF url" in {
    def parse(file: String): LocalDate = new LunchResolverAokCafeteria().parseMondayFromUrl(new URL("http://www.hotel-am-ring.de/" + HttpMittagspauseDir + file)).get

    parse("AOK_16.03.-20.03..pdf") should be (date(s"$YearNow-03-16"))
    parse("AOK_23.03.-27.03..pdf") should be (date(s"$YearNow-03-23"))
  }

  val Id = LunchProvider.AOK_CAFETERIA.id
  val HttpMittagspauseDir = "fileadmin/ordner_redaktion/dokumente/Speisepl%C3%A4ne-Kantinen/"

  private def date(dateString: String): LocalDate = LocalDate.parse(dateString)
  private def euro(moneyString: String): Money = Money.parse(s"EUR $moneyString")
  private val YearNow = LocalDate.now.getYear

}