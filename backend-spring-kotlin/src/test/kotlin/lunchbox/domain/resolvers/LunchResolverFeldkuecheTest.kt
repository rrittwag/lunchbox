package lunchbox.domain.resolvers /* ktlint-disable max-line-length */

import io.mockk.mockk
import java.net.URL
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.FELDKUECHE
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverFeldkuecheTest {

  private val ocrClient = mockk<OcrClient>()
  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverFeldkueche(DateValidator.alwaysValid(), ocrClient, htmlParser)
  private val providerId = FELDKUECHE.id
  private val httpMittagspauseDir = "https://www.feldkuechebkarow.de/s/cc_images"

  @Test
  fun `resolve image link for 2016-10-10`() {
    val url = javaClass.getResource("/menus/feldkueche/2016-10-10.html")

    val links = resolver().resolveImageLinks(url)

    links shouldHaveSize 1
    links shouldContain URL("$httpMittagspauseDir/teaserbox_25241614.jpg?t=1475778802")
  }

  @Test
  fun `resolve image link for 2017-07-17`() {
    val url = javaClass.getResource("/menus/feldkueche/2017-07-17.html")

    val links = resolver().resolveImageLinks(url)

    links shouldHaveSize 1
    links shouldContain URL("$httpMittagspauseDir/teaserbox_25241614.jpg?t=1500138543")
  }

  @Test
  fun `resolve image link for 2019-09-02`() {
    val url = javaClass.getResource("/menus/feldkueche/2019-09-02.html")

    val links = resolver().resolveImageLinks(url)

    links shouldHaveSize 1
    links shouldContain URL("$httpMittagspauseDir/teaserbox_25241614.jpg?t=1567365592")
  }

  @Test
  fun `resolve offers for week of 2016-10-10`() {
    val text = readFileContent("/menus/feldkueche/ocr/2016-10-10.jpg.txt")
    val week = weekOf("2016-10-10")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 10
    offers shouldContain LunchOffer(0, "Wurstgulasch", "Nudeln", week.monday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kartoffelsuppe", "mit Bockwurst", week.monday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Blutwurst Sauerkraut", "Kartoffeln", week.tuesday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Grüne Bohneneintopf", "Brot", week.tuesday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Gulasch", "mit Nudeln", week.wednesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Erbseneintopf", "mit Bockwurst", week.wednesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Eisbein Sauerkraut", "Kartoffeln", week.thursday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Brühnudeln", "mit Hähnchenfleisch Brot", week.thursday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Schichtkohl", "mit Kartoffeln", week.friday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Weißkohl", "mit Brot", week.friday, euro("3.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-10-17`() {
    val text = readFileContent("/menus/feldkueche/ocr/2016-10-17.jpg.txt")
    val week = weekOf("2016-10-17")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 11
    offers shouldContain LunchOffer(0, "Jägerschnitzel Tomatensauce", "Nudeln", week.monday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Mohreneintopf", "Brot", week.monday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Senfeier", "Kartoffeln Krautsalat", week.tuesday, euro("4.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Brühreis", "mit Hähnchenfleisch Brot", week.tuesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Königsberger Klopse", "Kartoffeln", week.wednesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kohlrabieintopf", "Brot", week.wednesday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Eisbein Sauerkraut", "Kartoffeln", week.thursday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Linseneintopf", "mit Bockwurst", week.thursday, euro("3.50"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Linseneintopf", "mit Knacker", week.thursday, euro("3.90"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Sahnegeschnetzeltes", "Nudeln", week.friday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Wirsingkohleintopf", "Brot", week.friday, euro("3.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-10-24`() {
    val text = readFileContent("/menus/feldkueche/ocr/2016-10-24.jpg.txt")
    val week = weekOf("2016-10-24")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 10
    offers shouldContain LunchOffer(0, "Nudeln mit Bolognese", "", week.monday, euro("4.50"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Grüne Bohneneintopf", "Brot", week.monday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Boulette Mischgemüse", "Kartoffeln", week.tuesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kartoffelsuppe", "mit Bockwurst", week.tuesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Gulasch", "Klöße Rotkohl", week.wednesday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Möhreneintopf", "Brot", week.wednesday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Eisbein Sauerkraut", "Kartoffeln", week.thursday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Erbseneintopf", "mit Bockwurst", week.thursday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Käse - Lauchsuppe", "Brot", week.friday, euro("4.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Chili con Carne", "Brot", week.friday, euro("4.20"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2016-10-31`() {
    val text = readFileContent("/menus/feldkueche/ocr/2016-10-31.jpg.txt")
    val week = weekOf("2016-10-31")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 10
    offers shouldContain LunchOffer(0, "Wurstgulasch", "mit Nudeln", week.monday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Soljanka", "mit Brot", week.monday, euro("3.80"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Blutwurst Sauerkraut", "Kartoffeln", week.tuesday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Weißkohleintopf", "Brot", week.tuesday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Gulasch", "mit Nudeln", week.wednesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Erbseneintopf", "mit Bockwurst", week.wednesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Eisbein Sauerkraut", "Kartoffeln", week.thursday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Brühnudeln", "mit Hähnchenfleisch Brot", week.thursday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Kesselgulasch", "mit Brot", week.friday, euro("4.20"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Wirsingkohleintopf", "Brot", week.friday, euro("3.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2017-02-06`() {
    val text = readFileContent("/menus/feldkueche/ocr/2017-02-06.jpg.txt")
    val week = weekOf("2017-02-06")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 10
    offers shouldContain LunchOffer(0, "Nudeln Carbonara", "", week.monday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Wirsingkohleineintopf", "mit Brot", week.monday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Schichtkohl", "mit Kartoffeln", week.tuesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Rosenkohleintopf", "mit Brot", week.tuesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Bouletten Mischgemüse", "Kartoffeln", week.wednesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kohlrabieintopf", "mit Brot", week.wednesday, euro("3.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Eisbein Sauerkraut", "Kartoffeln", week.thursday, euro("5.00"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kartoffelsuppe", "mit Bockwurst", week.thursday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Gulasch", "mit Nudeln", week.friday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Soljanka", "mit Brot", week.friday, euro("3.80"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2017-07-17`() {
    val text = readFileContent("/menus/feldkueche/ocr/2017-07-17.jpg.txt")
    val week = weekOf("2017-07-17")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 10
    offers shouldContain LunchOffer(0, "Nudeln Bolognese", "", week.monday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Bunter Gemüseeintopf", "Brot", week.monday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Blutwurst Sauerkraut", "Kartoffeln", week.tuesday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Brühnudeln", "mit Hähnchenfleisch Brot", week.tuesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Jägerschnitzel Tomatensauce", "Nudeln", week.wednesday, euro("4.60"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Erbseneintopf", "mit Bockwurst", week.wednesday, euro("4.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Gulasch", "mit Kartoffeln und Rotkohl", week.thursday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kräuterquark", "Kartoffeln Krautsalat", week.thursday, euro("4.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Schweinebraten Sauerkraut", "Kartoffeln", week.friday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Soljanka", "mit Brot", week.friday, euro("3.80"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-09-02`() {
    val text = readFileContent("/menus/feldkueche/ocr/2019-09-02.jpg.txt")
    val week = weekOf("2019-09-02")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 10
    offers shouldContain LunchOffer(0, "Nudeln mit Carbonara", "", week.monday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Weißkohleintopf", "mit Brot", week.monday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Blutwurst Sauerkraut", "Kartoffeln", week.tuesday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Kartoffelsuppe", "mit Bockwurst", week.tuesday, euro("4.00"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Pilz - Gulasch", "mit Nudeln", week.wednesday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Brühnudeln", "mit Hähnchenfleisch Brot", week.wednesday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Schichtkohl", "mit Kartoffeln", week.thursday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Möhreneintopf", "mit Brot", week.thursday, euro("3.50"), emptySet(), providerId)

    offers shouldContain LunchOffer(0, "Sahnegeschnetzeltes", "mit Nudeln", week.friday, euro("4.80"), emptySet(), providerId)
    offers shouldContain LunchOffer(0, "Erbseneintopf", "mit Bockwurst", week.friday, euro("4.00"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2019-09-23`() {
    val text = readFileContent("/menus/feldkueche/ocr/2019-09-23.jpg.txt")
    val week = weekOf("2019-09-23")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 9
    offers.filter { it.day == week.monday } shouldHaveSize 2
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for week of 2019-10-28`() {
    val text = readFileContent("/menus/feldkueche/ocr/2019-10-28.jpg.txt")
    val week = weekOf("2019-10-28")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 11
    offers.filter { it.day == week.monday } shouldHaveSize 2
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 2

    offers shouldContain LunchOffer(0, "Wirsingkohleintopf", "mit Brot", week.monday, euro("3.50"), emptySet(), providerId)
  }

  @Test
  fun `resolve offers for week of 2020-04-27`() {
    val text = readFileContent("/menus/feldkueche/ocr/2020-04-27.jpg.txt")
    val week = weekOf("2020-04-27")

    val offers = resolver().resolveOffersFromText(text)

    offers shouldHaveSize 8
    offers.filter { it.day == week.monday } shouldHaveSize 2
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 2
    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  private fun readFileContent(path: String): String {
    val url = javaClass.getResource(path)
    return url.readText(Charsets.UTF_8)
  }
}
