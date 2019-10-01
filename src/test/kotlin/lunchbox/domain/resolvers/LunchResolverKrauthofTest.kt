package lunchbox.domain.resolvers /* ktlint-disable max-line-length no-wildcard-imports */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.DAS_KRAUTHOF
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverKrauthofTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver(): LunchResolverKrauthof =
    LunchResolverKrauthof(DateValidator.alwaysValid(), htmlParser)

  private val providerId = DAS_KRAUTHOF.id

  private val httpUploadDir = "https://www.daskrauthof.de/wp-content/uploads"

  @Test
  fun `resolve PDF links for 2017-05-01`() {
    val url = javaClass.getResource("/menus/krauthof/2017-05-01.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpUploadDir/2017/04/KRAUTHOF-Lunch-02.05.-05.05.2017.pdf"
  }

  @Test
  fun `resolve offers for shorter week of 2017-05-01`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2017-05-01.pdf")
    val week = weekOf("2017-05-01")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 35 // TODO: eigentlich nur 28 Offers! 1. Mai ist frei!

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 7
    offers.filter { it.day == week.friday } shouldHaveSize 7

    offers shouldContain LunchOffer(0, "Karotten-Orangen-Suppe mit gebratenen Hähnchenstreifen", week.monday, euro("3.90"), providerId)
    offers shouldContain LunchOffer(0, "Knackige Blattsalate mit Kirschtomaten, Pinienkernen, marinierten Mozzarella, hausgemachtes Balsamicodressing", week.monday, euro("5.50"), providerId)
    offers shouldContain LunchOffer(0, "Gnocci mit würziger Blattspinat-Gorgonzolasauce, gerösteten Pinienkernen", week.monday, euro("6.60"), providerId)
    offers shouldContain LunchOffer(0, "Frische Tagliatelle mit saftigen Putenbruststreifen in Champignon-Kräuterrahm, mariniertem Ruccola", week.monday, euro("6.80"), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Rotbarschfilet auf Zitronenbuttersauce, Mandelbroccoli, Kräuterkartoffeln", week.monday, euro("7.20"), providerId)
    offers shouldContain LunchOffer(0, "Saftige Schweinesteaks mit Kräuterbutter, Wedges, Sour Cream, Salat", week.monday, euro("6.90"), providerId)
    offers shouldContain LunchOffer(0, "Feines Mousse von weißer Schokolade mit Kirschragout", week.monday, euro("3.10"), providerId)
  }

  @Test
  fun `resolve offers for shorter week of 2017-08-07`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2017-08-07.pdf")
    val week = weekOf("2017-08-07")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 35

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 7
    offers.filter { it.day == week.friday } shouldHaveSize 7

    offers shouldContain LunchOffer(0, "Herzhaftes Kartoffelsüppchen mit Scheiben von gebratenen Pfefferbeißern", week.monday, euro("3.70"), providerId)
    offers shouldContain LunchOffer(0, "Bunte Blattsalate mit gegrillter Paprika, eingelegten Artischocken, Kirschtomaten, Kräutern aus dem KRAUTHOF", week.monday, euro("4.90"), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Sellerieschnitzel (vegetarisch) auf Tomaten-Zucchini-Gemüse, in Rosmarin geschwenkte Kartoffeln", week.monday, euro("5.10"), providerId)
    offers shouldContain LunchOffer(0, "KRAUTHOF-Pizza mit Hähnchen, Hinterschinken, roten Zwiebeln, Strauchtomaten, Champignons, Kräuter-Hollandaise", week.monday, euro("7.10"), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Wildlachsfilet auf Blattspinat, Kräuterkartoffeln, Zitrone", week.monday, euro("7.30"), providerId)
    offers shouldContain LunchOffer(0, "Saftiges Schweinesteak im Zwiebel-Senf-Mantel mit Bratkartoffeln, Salat", week.monday, euro("6.20"), providerId)
    offers shouldContain LunchOffer(0, "Fruchtiges Zitronenmousse mit Himbeercoulis", week.monday, euro("2.90"), providerId)
  }

  @Test
  fun `resolve PDF links for 2017-09-25`() {
    val url = javaClass.getResource("/menus/krauthof/2017-09-25.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpUploadDir/2017/09/KRAUTHOF-Lunch-25.09-29.09.2017-.pdf"
  }

  @Test
  fun `resolve offers for week of 2017-09-25`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2017-09-25.pdf")
    val week = weekOf("2017-09-25")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 35

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 7
    offers.filter { it.day == week.friday } shouldHaveSize 7

    offers shouldContain LunchOffer(0, "Feines Kräutersüppchen mit Frühlingsgemüse, marinierten Shrimps", week.monday, euro("3.90"), providerId)
  }

  @Test
  fun `resolve offers for week of 2018-09-14`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2018-09-14.pdf")
    val week = weekOf("2018-09-14")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 35

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 7
    offers.filter { it.day == week.friday } shouldHaveSize 7

    offers shouldContain LunchOffer(0, "Geeistes Gurken-Buttermilchsüppchen mit Dillspitzen, Shrimps", week.monday, euro("3.90"), providerId)
    offers shouldContain LunchOffer(0, "Thai-Beef- Salat mit Chili, Bohnen, Champignons, Karotten, roten Zwiebeln, Sesam", week.monday, euro("5.20"), providerId)
    offers shouldContain LunchOffer(0, "Vegetarische Gemüsebolognese mit Spaghetti, Kräuterschmand", week.monday, euro("5.90"), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Hähnchenschnitzel mit Kohlrabi-Apfelgemüse, Kräuterkartoffeln", week.monday, euro("6.20"), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Tilapiafilet auf Dillsauce, Gurken-Zwiebelgemüse, Kartoffeln", week.monday, euro("6.50"), providerId)
    offers shouldContain LunchOffer(0, "Schweinesteak mit Feta-Nuss Haube mit Rahmsauce, Blumenkohl, Röstkartoffeln", week.monday, euro("6.90"), providerId)
    offers shouldContain LunchOffer(0, "Cappuccinomousse mit Orangenkompott", week.monday, euro("2.90"), providerId)
  }

  @Test
  fun `resolve PDF links for 2019-03-15`() {
    val url = javaClass.getResource("/menus/krauthof/2019-03-15.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpUploadDir/2019/03/KRAUTHOF.pdf"
  }
}
