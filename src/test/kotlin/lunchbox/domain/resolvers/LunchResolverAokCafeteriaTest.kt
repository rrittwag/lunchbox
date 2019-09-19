package lunchbox.domain.resolvers /* ktlint-disable max-line-length no-wildcard-imports */

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LunchResolverAokCafeteriaTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverAokCafeteria(DateValidator.alwaysValid(), htmlParser)
  private val providerId = AOK_CAFETERIA.id

  private val httpPdfDir = "fileadmin/ordner_redaktion/dokumente/Speisepl%C3%A4ne-Kantinen"

  @Test
  fun `resolve PDF links for 2015-03-17`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2015-03-17.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 2
    links shouldContain "$httpPdfDir/AOK_16.03.-20.03..pdf"
    links shouldContain "$httpPdfDir/AOK_23.03.-27.03..pdf"
  }

  @Test
  fun `resolve PDF links for 2015-08-03`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2015-08-03.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 5
    links shouldContain "$httpPdfDir/AOK_27.07.-31.07.2015.pdf"
    links shouldContain "$httpPdfDir/AFA_03.08.-07.08..pdf"
    links shouldContain "$httpPdfDir/AOK_10.08.-14.08..pdf"
    links shouldContain "$httpPdfDir/AOK_17.08.-21.08..pdf"
    links shouldContain "$httpPdfDir/AOK_24.08.-28.08..pdf"
  }

  @Test
  fun `resolve PDF links for 2017-05-01`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2017-05-01.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpPdfDir/AOK01.05.2017-05.05.2017.pdf"
  }

  @Test
  fun `resolve PDF links for 2018-01-22`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2018-01-22.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpPdfDir/AOK.pdf"
  }

  @Test
  fun `resolve PDF links for 2018-05-28`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2018-05-28.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpPdfDir/AOK.pdf"
  }

  @Test
  fun `resolve PDF links for 2018-11-19`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/2018-11-19.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain "$httpPdfDir/AOK.pdf"
  }

  @Test
  fun `resolve offers for week of 2015-03-16`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2015-03-16.pdf")
    val week = weekOf("2015-03-16")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15
    offers shouldContain LunchOffer(0, "Linseneintopf mit Möhren, Kartoffeln und Sellerie", week.monday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, """Spaghetti "Napoli" mit Oliven, Peperoni, geriebenem Käse und frischem Salat""", week.monday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Hähnchenschenkel mit Rotkohl und Petersilienkartoffeln", week.monday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "grünes Erbsensüppchen", week.tuesday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Rührei mit Petersilie, Frühlingslauch, Kartoffelpüree und Salatbeilage", week.tuesday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Kartoffel- Hackfleisch- Auflauf mit Lauch, Fetakäse und fischem Salat", week.tuesday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "pürierte Blumenkohl- cremesuppe", week.wednesday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Hefeklöße mit Blaubeeren", week.wednesday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Hamburger Schnitzel mit Röster und Gewürzgurken", week.wednesday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "weißer Bohneneintopf", week.thursday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Kartoffelpuffer mit Apfelmus", week.thursday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "gebratenes Jägerschnitzel mit fruchtiger Tomatensauce und Penne", week.thursday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "Tagesangebot", week.friday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Brathering mit Kartoffelpüree und frischem Salat", week.friday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Schweinesteak mit Tomaten- Sauerrahm- häubchen, buntem Gemüse und Wildreis", week.friday, euro("5.30"), providerId)
  }

  @Test
  fun `resolve offers for week of 2015-03-23`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2015-03-23.pdf")
    val week = weekOf("2015-03-23")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15
    offers shouldContain LunchOffer(0, "Kartoffelsuppe", week.monday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Spiegeleier mit Rahmspinat und Salzkartoffeln", week.monday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Chinapfanne mit Hähnchenfleisch, Reis und frischem Salat", week.monday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "Gemüsesuppe", week.tuesday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Sahnehering mit Butter, Salzkartoffeln und Salatbeilage", week.tuesday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Kochklops in Kapernsauce mit Salzkartoffeln und Rote Bete", week.tuesday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "Brokkolicremesuppe", week.wednesday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Eier in Senfsauce mit Salzkartoffeln und Rote Bete", week.wednesday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Schweinekotelett mit Rosenkohl und Salzkartoffeln", week.wednesday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "gelbes Erbsensüppchen", week.thursday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Schweineleber mit Apfel- Zwiebelsauce und Kartoffelpüree", week.thursday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Penne mit Champignonrahm und Kräuterdipp", week.thursday, euro("5.30"), providerId)
    offers shouldContain LunchOffer(0, "Tagesangebot", week.friday, euro("3.50"), providerId)
    offers shouldContain LunchOffer(0, "Schupfnudelpfanne mit Gemüse und frischem Salat", week.friday, euro("4.50"), providerId)
    offers shouldContain LunchOffer(0, "Schweinesteak mit Senfzwiebeln und Pommes Frites", week.friday, euro("5.30"), providerId)
  }

  @Test
  fun `resolve offers for Easter week of 2015-03-30`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2015-03-30.pdf")
    val week = weekOf("2015-03-30")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 12
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for Easter week of 2015-04-06`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2015-04-06.pdf")
    val week = weekOf("2015-04-06")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 12
    offers.filter { it.day == week.monday } shouldHaveSize 0
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2015-07-20`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2015-07-20.pdf")
    val week = weekOf("2015-07-20")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2015-08-03`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2015-08-03.pdf")
    val week = weekOf("2015-08-03")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2017-05-01`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2017-05-01.pdf")
    val week = weekOf("2017-05-01")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 12
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2018-05-28`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2018-05-28.pdf")
    val week = weekOf("2018-05-28")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `resolve offers for week of 2018-11-19`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2018-11-19.pdf")
    val week = weekOf("2018-11-19")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 15
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 3
  }

  @Test
  fun `parse date by PDF url`() {
    fun parse(lines: List<String>): LocalDate? = resolver().parseMondayFromStrings(lines)

    val yearNow = LocalDate.now().year

    parse(listOf("16.03.-20.03.2016")) shouldEqual weekOf("2016-03-20").monday
    parse(listOf("23.03.-27.03.")) shouldEqual weekOf("$yearNow-03-27").monday
    parse(listOf("16.03.-20.03.")) shouldEqual weekOf("$yearNow-03-20").monday
    parse(listOf("27.07.-31.07.2015 bla")) shouldEqual weekOf("2015-07-27").monday
  }

  @Test
  fun `resolve offers for week of 2019-09-16`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2019-09-16.pdf")
    val week = weekOf("2019-09-16")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 13
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 2
    offers.filter { it.day == week.friday } shouldHaveSize 2
  }
}
