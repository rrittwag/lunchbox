package lunchbox.domain.resolvers

import java.net.URL
import java.time.LocalDate
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.util.date.DateValidator
import lunchbox.util.date.HolidayUtil
import lunchbox.util.html.HtmlParser
import lunchbox.util.pdf.PdfExtractor
import lunchbox.util.pdf.TextGroup
import lunchbox.util.pdf.TextLine
import lunchbox.util.string.StringParser
import mu.KotlinLogging
import org.joda.money.Money
import org.springframework.stereotype.Component

@Component
class LunchResolverAokCafeteria(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser
) : LunchResolver {

  private val logger = KotlinLogging.logger {}

  override val provider = AOK_CAFETERIA

  override fun resolve(): List<LunchOffer> {
    val pdfLinks = resolvePdfLinks(provider.menuUrl)
    return resolveFromPdfs(pdfLinks)
  }

  fun resolvePdfLinks(htmlUrl: URL): List<String> {
    val site = htmlParser.parse(htmlUrl)

    val links = site.select("a").map { it.attr("href") }
    return links.filter { it.matches(Regex(""".*/[a-zA-Z]{3}[0-9_.\-]*.pdf""")) }.distinct()
  }

  fun resolveFromPdfs(relativePdfPaths: List<String>): List<LunchOffer> =
    // TODO: parallelize
    relativePdfPaths.map { resolveFromPdf(URL("https://www.hotel-am-ring.de/$it")) }.flatten()

  fun resolveFromPdf(pdfUrl: URL): List<LunchOffer> {
    val lines = PdfExtractor.extractLines(pdfUrl)

    val monday = parseMonday(lines)
    if (monday == null || !dateValidator.isValid(monday))
      return emptyList()
    return resolveFromPdfContent(pdfUrl, lines, monday)
      .filterNot { HolidayUtil.isHoliday(it.day, provider.location) }
  }

  private fun resolveFromPdfContent(
    pdfUrl: URL,
    lines: List<TextLine>,
    monday: LocalDate
  ): List<LunchOffer> {
    val section2lines = groupBySection(lines)

    val priceLines = section2lines[PdfSection.TABLE_HEADER]
    if (priceLines == null || priceLines.isEmpty()) {
      logger.warn { "Preis-Header nicht gefunden in $pdfUrl" }
      return emptyList()
    }

    val priceColumns = parsePriceColumns(priceLines[0])

    val offers = mutableListOf<LunchOffer>()

    for (weekday in PdfSection.weekdayValues) {
      val weekdayLines = section2lines[weekday]
      if (weekdayLines == null || weekdayLines.isEmpty())
        continue

      for (priceColumn in priceColumns) {
        val offer = parseDayOffer(weekdayLines, weekday, monday, priceColumn)
          ?: continue
        offers += offer
      }
    }
    return offers
  }

  fun parsePriceColumns(priceHeader: TextLine): List<PriceColumn> {
    val priceTexts = distinctPriceTexts(priceHeader.texts)
    return priceTexts.mapNotNull { parsePriceColumn(it) }
  }

  fun parsePriceColumn(priceText: TextGroup): PriceColumn? {
    val x = priceText.xMid()
    val price = StringParser.parseMoney(priceText.toString()) ?: return null
    return PriceColumn(x, price)
  }

  fun parseMonday(lines: List<TextLine>): LocalDate? =
    StringParser.parseMondayOfMostUsedWeek(lines.map { it.toString() })

  private fun groupBySection(lines: List<TextLine>): Map<PdfSection, List<TextLine>> {
    val header = lines.find { it.allTextsMatch(Regex("""^\d+[.,]\d{2} *€$""")) }
      ?: return emptyMap()

    val result = mutableMapOf<PdfSection, List<TextLine>>()
    result += PdfSection.TABLE_HEADER to listOf(header)

    val linesBelowHeader = lines.filter { it.y > header.y }

    var linesForDay = emptyList<TextLine>()
    for (line in linesBelowHeader) {
      if (line.oneTextMatches(Regex(""".*Zusatzstoffe.*"""))) {
        // an Feiertagen gibt es keine Angebote ... und keine Zusatzstoffe
        if (line.texts.size > 1) {
          val weekday = findWeekdaySection(linesForDay)
          weekday?.let { result += it to linesForDay }
        }
        linesForDay = emptyList()
      } else if (line.oneTextMatches(Regex(""".*(Nährwerte|Kohlenhydrate).*"""))) {
        // ignore line
      } else {
        linesForDay = linesForDay + line
      }
    }

    return result
  }

  private fun parseDayOffer(
    lines: List<TextLine>,
    weekday: PdfSection,
    monday: LocalDate,
    priceColumn: PriceColumn
  ): LunchOffer? {
    val day = monday.plusDays(weekday.order)
    val name =
      lines
        .flatMap { line -> line.texts.filter { it.xIn(priceColumn.x) } }
        .joinToString(" ")
        .let { parseName(it) } ?: return null

    return LunchOffer(0, name, day, priceColumn.price, provider.id)
  }

  private fun findWeekdaySection(lines: List<TextLine>): PdfSection? {
    val weekdaysRegex =
      PdfSection.weekdayValues
        .joinToString(prefix = "^(", separator = "|", postfix = ")$") { it.label }
    val weekdayTextGroup =
      lines
        .mapNotNull { line -> line.texts.find { it.toString().matches(Regex(weekdaysRegex)) } }
        .firstOrNull()
    return weekdayTextGroup?.let { parseWeekdaySection(it.toString()) }
  }

  /**
   * Manchmal wird die fette Schrift durch Übereinanderlegen von Texten erreicht.
   * Zur Verarbeitung der Mittagsangebote brauchen wir jedoch lediglich "ein Layer".
   */
  private fun distinctPriceTexts(texts: List<TextGroup>): List<TextGroup> =
    texts.groupBy { it.toString() }
      .values.map { layeredTexts -> layeredTexts.minBy { it.xMin() }!! }
      .sortedBy { it.xMin() }

  private fun parseName(text: String): String? {
    val cleanedName =
      text.trim().replace("  ", " ")

    if (cleanedName.isEmpty() || cleanedName == "-")
      return null
    return cleanedName
  }

  private fun parseWeekdaySection(weekdayString: String): PdfSection? =
    PdfSection.weekdayValues.find { it.label == weekdayString }

  enum class PdfSection(
    val label: String,
    val order: Long
  ) {
    TABLE_HEADER("", -1),
    MONTAG("Montag", 0),
    DIENSTAG("Dienstag", 1),
    MITTWOCH("Mittwoch", 2),
    DONNERSTAG("Donnerstag", 3),
    FREITAG("Freitag", 4);

    companion object {
      val weekdayValues = listOf(MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    }
  }

  data class PriceColumn(
    val x: Float,
    val price: Money
  )
}
