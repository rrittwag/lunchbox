package lunchbox.domain.resolvers /* ktlint-disable max-line-length */

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

    offers shouldHaveSize 28

    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 7
    offers.filter { it.day == week.friday } shouldHaveSize 7

    offers shouldContain LunchOffer(0, "Karotten-Orangen-Suppe", "mit gebratenen Hähnchenstreifen", week.tuesday, euro("3.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Knackige Blattsalate", "mit Kirschtomaten, Pinienkernen, marinierten Mozzarella, hausgemachtes Balsamicodressing", week.tuesday, euro("5.50"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gnocci", "mit würziger Blattspinat-Gorgonzolasauce, gerösteten Pinienkernen", week.tuesday, euro("6.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Frische Tagliatelle", "mit saftigen Putenbruststreifen in Champignon-Kräuterrahm, mariniertem Ruccola", week.tuesday, euro("6.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Rotbarschfilet", "auf Zitronenbuttersauce, Mandelbroccoli, Kräuterkartoffeln", week.tuesday, euro("7.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Saftige Schweinesteaks", "mit Kräuterbutter, Wedges, Sour Cream, Salat", week.tuesday, euro("6.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Feines Mousse", "von weißer Schokolade mit Kirschragout", week.tuesday, euro("3.10"), emptySet(), providerId)
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

    offers shouldContain LunchOffer(0, "Herzhaftes Kartoffelsüppchen", "mit Scheiben von gebratenen Pfefferbeißern", week.monday, euro("3.70"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Bunte Blattsalate", "mit gegrillter Paprika, eingelegten Artischocken, Kirschtomaten, Kräutern aus dem KRAUTHOF", week.monday, euro("4.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Sellerieschnitzel", "auf Tomaten-Zucchini-Gemüse, in Rosmarin geschwenkte Kartoffeln", week.monday, euro("5.10"), setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "KRAUTHOF-Pizza", "mit Hähnchen, Hinterschinken, roten Zwiebeln, Strauchtomaten, Champignons, Kräuter-Hollandaise", week.monday, euro("7.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Wildlachsfilet", "auf Blattspinat, Kräuterkartoffeln, Zitrone", week.monday, euro("7.30"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Saftiges Schweinesteak", "im Zwiebel-Senf-Mantel mit Bratkartoffeln, Salat", week.monday, euro("6.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Fruchtiges Zitronenmousse", "mit Himbeercoulis", week.monday, euro("2.90"), emptySet(), providerId)
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

    offers shouldContain LunchOffer(0, "Feines Kräutersüppchen", "mit Frühlingsgemüse, marinierten Shrimps", week.monday, euro("3.90"), emptySet(), providerId)
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

    offers shouldContain LunchOffer(0, "Geeistes Gurken-Buttermilchsüppchen", "mit Dillspitzen, Shrimps", week.monday, euro("3.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Thai-Beef- Salat", "mit Chili, Bohnen, Champignons, Karotten, roten Zwiebeln, Sesam", week.monday, euro("5.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Vegetarische Gemüsebolognese", "mit Spaghetti, Kräuterschmand", week.monday, euro("5.90"), setOf("vegetarisch"), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Hähnchenschnitzel", "mit Kohlrabi-Apfelgemüse, Kräuterkartoffeln", week.monday, euro("6.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Tilapiafilet", "auf Dillsauce, Gurken-Zwiebelgemüse, Kartoffeln", week.monday, euro("6.50"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Schweinesteak", "mit Feta-Nuss Haube mit Rahmsauce, Blumenkohl, Röstkartoffeln", week.monday, euro("6.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Cappuccinomousse", "mit Orangenkompott", week.monday, euro("2.90"), emptySet(), providerId)
  }

  @Test
  fun `resolve PDF links for 2019-03-15`() {
    val url = javaClass.getResource("/menus/krauthof/2019-03-15.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpUploadDir/2019/03/KRAUTHOF.pdf"
  }

  @Test
  fun `resolve offers for week of 2019-09-30`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2019-09-30.pdf")
    val week = weekOf("2019-09-30")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 28

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 0
    offers.filter { it.day == week.friday } shouldHaveSize 7
  }

  @Test
  fun `resolve offers for week of 2019-10-28`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2019-10-28.pdf")
    val week = weekOf("2019-10-28")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 28

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 0
    offers.filter { it.day == week.friday } shouldHaveSize 7

    offers shouldContain LunchOffer(0, "Cremige Schwarzwurzelsuppe", "mit marinierte Rote Beete", week.monday, euro("4.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Feine Blattsalate", "mit Hähnchenstreifen in Chilimarinade, Mango, Kirschtomaten, Sonnenblumenkernen", week.monday, euro("5.10"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Gemüseschnitzel", "an Karotten in Orangen-Vanillerahm, Kräuterkartoffeln", week.monday, euro("6.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Putensteak", "an Gorgonzolasauce, Blattspinat, kleinen Röstis", week.monday, euro("7.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gebratenes Rotbarschfilet", "an Steckrüben in Kräuterschmand, Kräuterkartoffeln", week.monday, euro("7.40"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Schnitzel", "an Kräuter-Champignons, Pommes Frites, Salat", week.monday, euro("6.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Grütze", "von gelben Früchten mit Kokos-Vanillecreme", week.monday, euro("3.10"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-01-27`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2020-01-27.pdf")
    val week = weekOf("2020-01-27")

    val offers = resolver().resolveFromPdf(url)

    offers.filter { it.day == week.monday } shouldHaveSize 7
    offers.filter { it.day == week.tuesday } shouldHaveSize 7
    offers.filter { it.day == week.wednesday } shouldHaveSize 7
    offers.filter { it.day == week.thursday } shouldHaveSize 7
    offers.filter { it.day == week.friday } shouldHaveSize 7
    offers shouldContain LunchOffer(0, "Veganes Paprika-Tomatencremesüppchen", "mit Rucolasahne, Sonnenblumenkernen", week.monday, euro("4.30"), setOf("vegan"), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-04-20`() {
    val url = javaClass.getResource("/menus/krauthof/pdf/2020-04-20.pdf")
    val week = weekOf("2020-04-20")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15

    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3

    offers shouldContain LunchOffer(0, "Grünkernkäsemedaillons", "mit Petersiliensauce, buntem Karottengemüse, Kartoffeln", week.monday, euro("6.90"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Gebratene Hähnchenbrust", "mit Champignons in leichtem Thymianrahm, Pommes frites", week.monday, euro("7.50"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Knuspriges Schnitzel", "mit Spiegelei, Bratkartoffeln, Gurkensalat", week.monday, euro("6.50"), emptySet(), providerId)
  }
}
