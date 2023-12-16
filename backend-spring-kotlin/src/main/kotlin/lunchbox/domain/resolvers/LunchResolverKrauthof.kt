package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.DAS_KRAUTHOF
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.pdf.PdfExtractor
import lunchbox.util.string.StringParser
import lunchbox.util.url.UrlUtil.url
import org.joda.money.Money
import org.springframework.stereotype.Component
import java.net.URL
import java.time.LocalDate

@Component
class LunchResolverKrauthof(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser,
) : LunchResolver {
  override val provider = DAS_KRAUTHOF

  override fun resolve(): List<LunchOffer> {
    val pdfLinks = resolvePdfLinks(provider.menuUrl)
    return resolveFromPdfs(pdfLinks)
  }

  fun resolvePdfLinks(htmlUrl: URL): List<String> {
    val site = htmlParser.parse(htmlUrl)

    val links = site.select("a").map { it.attr("href") }
    return links.filter { it.matches(Regex(""".*/KRAUTHOF.*.pdf""")) }
  }

  fun resolveFromPdfs(pdfPaths: List<String>): List<LunchOffer> =
    // TODO: parallelize
    pdfPaths.map { resolveFromPdf(url(it)) }.flatten()

  fun resolveFromPdf(pdfUrl: URL): List<LunchOffer> {
    val pdfContent = PdfExtractor.extractStrings(pdfUrl)

    val monday = StringParser.parseMondayOfMostUsedWeek(pdfContent) ?: return emptyList()

    return resolveFromPdfContent(pdfContent, monday)
      .filter { dateValidator.isValid(it.day, provider) }
  }

  private fun resolveFromPdfContent(
    pdfContent: List<String>,
    monday: LocalDate,
  ): List<LunchOffer> {
    val rows = mutableListOf<OfferRow>()
    var currentRow: OfferRow? = null

    fun finishOffer() {
      currentRow?.let { if (it.isValid()) rows.add(it) }
      currentRow = null
    }

    val pdfContentOhneZusatzstoffe =
      pdfContent.filterNot { it.matches(Regex("""^\([a-z0-9 ()│|]+\)$""")) }

    for (line in pdfContentOhneZusatzstoffe.map { it.trim() }) {
      val matchOffer = Regex("""(.+)(\d+[.,]\d{2}) *€\*""").find(line)
      if (matchOffer != null) {
        val (text, priceString) = matchOffer.destructured
        finishOffer()
        currentRow = OfferRow(parseName(text), StringParser.parseMoney(priceString))
        continue
      }

      if (line.isEmpty()) {
        finishOffer()
        continue
      }

      val matchLastLine = Regex(""".*(inklusive Tagesgetränk|Zusatzstoffe).*""").find(line)
      if (matchLastLine != null) {
        finishOffer()
        continue
      }

      val thisRow = OfferRow(parseName(line), null)
      currentRow = currentRow?.merge(thisRow) ?: thisRow
    }

    val mondayOffers = rows.map { row -> createLunchOffer(row, monday) }

    // an allen Wochentagen gibt es das selbe Angebot
    return LongRange(0, 4).flatMap { weekdayLong ->
      mondayOffers.map { it.copy(day = monday.plusDays(weekdayLong)) }
    }
  }

  private fun createLunchOffer(
    row: OfferRow,
    monday: LocalDate,
  ): LunchOffer {
    val (title, description) = StringParser.splitOfferName(row.name)
    val titleOhneZusatz = title.replace(Regex("""\([a-z0-9, ]+\)"""), "").trim()
    val tags = parseTags(row.name)

    return LunchOffer(0, titleOhneZusatz, description, monday, row.price!!, tags, provider.id)
  }

  private fun parseTags(name: String): Set<String> =
    when {
      name.contains("vegan", ignoreCase = true) -> setOf("vegan")
      name.contains("vegetarisch", ignoreCase = true) -> setOf("vegetarisch")
      else -> emptySet()
    }

  private fun parseName(text: String): String =
    text.trim()
      .replace("  ", " ")
      .replace("–", "-")
      .replace(Regex(""" *[│|] *"""), ", ")
      .replace(" I ", ", ")
      .replace(" *,", ",")
      .trim()

  data class OfferRow(
    val name: String,
    val price: Money?,
  ) {
    fun merge(otherRow: OfferRow): OfferRow {
      val newName = listOf(name, otherRow.name).filter { it.isNotEmpty() }.joinToString(" ")
      return OfferRow(newName, price ?: otherRow.price)
    }

    fun isValid() = name.isNotEmpty() && price != null
  }
}
