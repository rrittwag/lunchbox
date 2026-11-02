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

  private fun resolver(apiClient: TorneyApiClient = TorneyApiClient { url, _ -> url.readText() }) =
    LunchResolverTorney(
      DateValidator.alwaysValid(),
      ocrClient,
      htmlParser,
      apiClient,
    )

  private val providerId = TORNEY.id
  private val uploadsDir = "${TORNEY.menuUrl}wp-content/uploads"

  @Test
  fun `resolve prefers lunch endpoint when it contains products`() {
    val lunchJson = javaClass.getResource("/menus/torney/json/2026-11-02/lunch_branch133_cat6454.json").readText()
    val requestedEndpoints = mutableListOf<String>()
    val apiClient =
      TorneyApiClient { url, _ ->
        requestedEndpoints += url.path.substringAfterLast('/')
        lunchJson
      }

    resolver(apiClient).resolve() shouldHaveSize 10
    requestedEndpoints shouldBeEqualTo listOf("lunch")
  }

  @Test
  fun `resolve falls back to deals endpoint when lunch endpoint has no products`() {
    val dealsJson = javaClass.getResource("/menus/torney/json/2026-10-26/deals_branch133_cat6454.json").readText()
    val requestedEndpoints = mutableListOf<String>()
    val apiClient =
      TorneyApiClient { url, _ ->
        requestedEndpoints += url.path.substringAfterLast('/')
        if (url.path.endsWith("/lunch")) {
          """{"Data":{"data":[{"f":"2026-11-02","p":[]}]}}"""
        } else {
          dealsJson
        }
      }

    resolver(apiClient).resolve() shouldHaveSize 10
    requestedEndpoints shouldBeEqualTo listOf("lunch", "deals")
  }

  @Test
  fun `resolve offers for week of 2026-10-26`() {
    val url = javaClass.getResource("/menus/torney/json/2026-10-26/deals_branch133_cat6454.json")
    val week = weekOf("2026-10-26")

    val offers = resolver().resolveFromApi(url)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Bauernroulade",
        "mit Rotkohl und Salzkartoffeln",
        week.monday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bauernroulade",
        "mit Rotkohl und Salzkartoffeln",
        week.tuesday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bauernroulade",
        "mit Rotkohl und Salzkartoffeln",
        week.wednesday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bauernroulade",
        "mit Rotkohl und Salzkartoffeln",
        week.thursday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Bauernroulade",
        "mit Rotkohl und Salzkartoffeln",
        week.friday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengeschnetzeltes",
        "mit Salzkartoffeln/Nudeln/Reis",
        week.monday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengeschnetzeltes",
        "mit Salzkartoffeln/Nudeln/Reis",
        week.tuesday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengeschnetzeltes",
        "mit Salzkartoffeln/Nudeln/Reis",
        week.wednesday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengeschnetzeltes",
        "mit Salzkartoffeln/Nudeln/Reis",
        week.thursday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchengeschnetzeltes",
        "mit Salzkartoffeln/Nudeln/Reis",
        week.friday,
        euro("7.90"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2026-11-02`() {
    val url = javaClass.getResource("/menus/torney/json/2026-11-02/lunch_branch133_cat6454.json")
    val week = weekOf("2026-11-02")

    val offers = resolver().resolveFromApi(url)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "mit Salzkartoffeln",
        week.monday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "mit Salzkartoffeln",
        week.tuesday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "mit Salzkartoffeln",
        week.wednesday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "mit Salzkartoffeln",
        week.thursday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schichtkohl",
        "mit Salzkartoffeln",
        week.friday,
        euro("6.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel",
        "mit Rahmgemüse & Salzkartoffeln",
        week.monday,
        euro("8.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel",
        "mit Rahmgemüse & Salzkartoffeln",
        week.tuesday,
        euro("8.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel",
        "mit Rahmgemüse & Salzkartoffeln",
        week.wednesday,
        euro("8.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel",
        "mit Rahmgemüse & Salzkartoffeln",
        week.thursday,
        euro("8.90"),
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Schweineschnitzel",
        "mit Rahmgemüse & Salzkartoffeln",
        week.friday,
        euro("8.90"),
        emptySet(),
        providerId,
      )
  }

  // --- old implementation ---

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
        week.monday,
        null,
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-03-03`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-10.KW-2025.png.txt")
    val week = weekOf("2025-03-03")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Kohlroulade",
        "mit Salzkartoffeln",
        week.monday,
        null,
        emptySet(),
        providerId,
      )
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschenkel",
        "mit Salzkartoffeln und Buttergemüse",
        week.monday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-03-24`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-13.KW-2025.png.txt")
    val week = weekOf("2025-03-24")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Kohlroulade",
        "mit Salzkartoffeln",
        week.monday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
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
  }

  @Test
  fun `resolve offers for week of 2025-03-31`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-14.KW-2025.png.txt")
    val week = weekOf("2025-03-31")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Hackbraten",
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
        week.monday,
        null,
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-04-21`() {
    val text = readFileContent("/menus/torney/ocr/Tagesgericht-17.KW-2025.png.txt")
    val week = weekOf("2025-04-21")

    val offers = resolver().resolveOffersFromText(text, week.monday)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Kohlroulade",
        "mit Salzkartoffeln",
        week.monday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
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
  }

  @Test
  fun `resolve offers for week of 2025-05-19`() {
    val text = readFileContent("/menus/torney/ocr/Tages-gerichte-70.png.txt")
    val week = weekOf("2025-05-19")

    val offers = resolver().resolveOffersFromText(text, null)

    println(offers)
    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Kohlroulade",
        "mit Salzkartoffeln",
        week.monday,
        euro("7.50"),
        emptySet(),
        providerId,
      )
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
  }

  @Test
  fun `resolve offers for week of 2025-05-26`() {
    val text = readFileContent("/menus/torney/ocr/Tages-gerichte-71.png.txt")
    val week = weekOf("2025-05-26")

    val offers = resolver().resolveOffersFromText(text, null)

    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschenkel",
        "mit Buttergemüsesoße und Salzkartoffeln",
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
        week.monday,
        null,
        emptySet(),
        providerId,
      )
  }

  @Test
  fun `resolve offers for week of 2025-06-09`() {
    val text = readFileContent("/menus/torney/ocr/Tages-gerichte-74.png.txt")
    val week = weekOf("2025-06-09")

    val offers = resolver().resolveOffersFromText(text, null)

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
  fun `resolve offers for week of 2025-06-23`() {
    val text = readFileContent("/menus/torney/ocr/Tages-gerichte-76.png.txt")
    val week = weekOf("2025-06-23")

    val offers = resolver().resolveOffersFromText(text, null)
    println(offers)
    offers shouldHaveSize 10
    offers shouldContain
      LunchOffer(
        0,
        "Hähnchenschenkel",
        "mit Buttergemüsesoße und Salzkartoffeln",
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
        week.monday,
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
