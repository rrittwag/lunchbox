package lunchbox.domain.logic /* ktlint-disable max-line-length no-wildcard-imports */

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldEqual
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LunchResolverAokCafeteriaTest {

  private fun resolver() = LunchResolverAokCafeteria(DateValidator.alwaysValid())
  private val providerId = AOK_CAFETERIA.id

  private val httpMittagspauseDir = "fileadmin/ordner_redaktion/dokumente/Speisepl%C3%A4ne-Kantinen/"

  @Test
  fun `resolve PDF links for 2015-03-17`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/aok_cafeteria_2015-03-17.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 2
    links shouldContain (httpMittagspauseDir + "AOK_16.03.-20.03..pdf")
    links shouldContain (httpMittagspauseDir + "AOK_23.03.-27.03..pdf")
  }

  @Test
  fun `resolve PDF links for 2015-08-03`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/aok_cafeteria_2015-08-03.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 5
    links shouldContain (httpMittagspauseDir + "AOK_27.07.-31.07.2015.pdf")
    links shouldContain (httpMittagspauseDir + "AFA_03.08.-07.08..pdf")
    links shouldContain (httpMittagspauseDir + "AOK_10.08.-14.08..pdf")
    links shouldContain (httpMittagspauseDir + "AOK_17.08.-21.08..pdf")
    links shouldContain (httpMittagspauseDir + "AOK_24.08.-28.08..pdf")
  }

  @Test
  fun `resolve PDF links for 2017-05-01`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/aok_cafeteria_2017-05-01.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain (httpMittagspauseDir + "AOK01.05.2017-05.05.2017.pdf")
  }

  @Test
  fun `resolve PDF links for 2018-01-22`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/aok_cafeteria_2018-01-22.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain (httpMittagspauseDir + "AOK.pdf")
  }

  @Test
  fun `resolve PDF links for 2018-05-28`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/aok_cafeteria_2018-05-28.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain (httpMittagspauseDir + "AOK.pdf")
  }

  @Test
  fun `resolve PDF links for 2018-11-19`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/aok_cafeteria_2018-11-19.html")

    val links = resolver().resolvePdfLinks(url)

    links shouldHaveSize 1
    links shouldContain (httpMittagspauseDir + "AOK.pdf")
  }

  @Test
  fun `resolve offers for week of 2015-03-20`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK_16.03.-20.03..pdf")
    val week = weekOf("2015-03-20")

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
  fun `resolve offers for week of 2015-03-27`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK_23.03.-27.03..pdf")
    val week = weekOf("2015-03-27")

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
  fun `resolve offers for Easter week of 2015-04-02`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK_30.03.-02.04..pdf")
    val week = weekOf("2015-04-02")

    val offers = resolver().resolveFromPdf(url)

    offers shouldHaveSize 12
    offers.filter { it.day == week.monday } shouldHaveSize 3
    offers.filter { it.day == week.tuesday } shouldHaveSize 3
    offers.filter { it.day == week.wednesday } shouldHaveSize 3
    offers.filter { it.day == week.thursday } shouldHaveSize 3
    offers.filter { it.day == week.friday } shouldHaveSize 0
  }

  @Test
  fun `resolve offers for Easter week of 2015-04-10`() {
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK_06.04.-10.04..pdf")
    val week = weekOf("2015-04-10")

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
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK_20.07.-24.07.2015.pdf")
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
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AFA_03.08.-07.08..pdf")
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
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK01.05.2017-05.05.2017.pdf")
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
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/AOK_28.05.-01.06.2018.pdf")
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
    val url = javaClass.getResource("/menus/aok_cafeteria/pdf/2018-11-19/AOK.pdf")
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
  fun `parse date from PDF url`() {
    fun parse(lines: List<String>): LocalDate? = resolver().parseMondayFromStrings(lines)

    val yearNow = LocalDate.now().year

    parse(listOf("16.03.-20.03.2016")) shouldEqual weekOf("2016-03-20").monday
    parse(listOf("23.03.-27.03.")) shouldEqual weekOf("$yearNow-03-27").monday
    parse(listOf("16.03.-20.03.")) shouldEqual weekOf("$yearNow-03-20").monday
    parse(listOf("27.07.-31.07.2015 bla")) shouldEqual weekOf("2015-07-27").monday
  }
}
