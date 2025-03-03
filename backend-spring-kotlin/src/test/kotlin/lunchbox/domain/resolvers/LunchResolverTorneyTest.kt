package lunchbox.domain.resolvers

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.TORNEY
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import lunchbox.util.url.UrlUtil.url
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test

class LunchResolverTorneyTest {
  private val ocrClient = mockk<OcrClient>()
  private val htmlParser = HtmlParser(mockk())

  private fun resolver() = LunchResolverTorney(DateValidator.alwaysValid(), ocrClient, htmlParser)

  private val providerId = TORNEY.id
  private val uploadsDir = "${TORNEY.menuUrl}wp-content/uploads"

  @Test
  fun `resolve image link for 2025-02-17`() {
    val url = javaClass.getResource("/menus/torney/2025-02-24.html")

    val links = resolver().resolveImageLinks(url)

    links shouldHaveSize 1
    links shouldContain url("$uploadsDir/2025/02/Tagesgericht-09.KW-2025.png")
  }

  @Test
  fun `parse monday`() {
    val week = weekOf("2025-02-24")

    resolver().parseMonday("$uploadsDir/2025/02/Tagesgericht-09.KW-2025.png") shouldBeEqualTo week.monday
    resolver().parseMonday("$uploadsDir/2025/02/Tagesgericht-09.2025.png") shouldBe null
  }

  @Test
  fun `resolve offers for week of 2025-02-10`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-07.KW-2025.png.txt")
    val week = weekOf("2025-02-10")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Mettpfanne",
        "mit Nudeln",
        week.monday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Kasslerkamm",
        "mit Rotkohl und Salzkartoffeln",
        week.monday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Mettpfanne",
        "mit Nudeln",
        week.friday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers.filter { it.day == week.tuesday } shouldHaveSize 2
    offers.filter { it.day == week.wednesday } shouldHaveSize 2
    offers.filter { it.day == week.thursday } shouldHaveSize 2
    offers shouldContain
      LunchOffer(
        0,
        "Kasslerkamm",
        "mit Rotkohl und Salzkartoffeln",
        week.friday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-02-17`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-08.KW-2025.png.txt")
    val week = weekOf("2025-02-17")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "mit Salzkartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Grützwurst",
        "mit Sauerkraut und Salzkartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-02-24`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-09.KW-2025.png.txt")
    val week = weekOf("2025-02-24")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Schnitzel",
        "mit Rahmgemüse und Salzkartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Wurstgulasch",
        "mit Nudeln",
        week.tuesday,
        null,
        emptySet(),
        providerId,
      )
  }

  private fun readFileContent(path: String): String {
    val url = javaClass.getResource(path)
    return url.readText(Charsets.UTF_8)
  }
}
