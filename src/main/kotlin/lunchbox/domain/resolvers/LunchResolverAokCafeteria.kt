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
    return links.filter { it.matches(Regex(""".*/AOK.*.pdf""")) }.distinct()
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
    val hasLineWithPrices = lines.any { it.allTextsMatch(Regex("""^\d+[.,]\d{2} *€$""")) }

    return if (hasLineWithPrices)
      resolveFromPdfContentWithHorizontalPrice(pdfUrl, lines, monday)
    else
      resolveFromPdfContentWithVerticalPrice(lines, monday)
  }

  private fun resolveFromPdfContentWithHorizontalPrice(
    pdfUrl: URL,
    lines: List<TextLine>,
    monday: LocalDate
  ): List<LunchOffer> {
    val section2lines = groupBySectionWithHorizontalPrice(lines)

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

  private fun groupBySectionWithHorizontalPrice(
    lines: List<TextLine>
  ): Map<PdfSection, List<TextLine>> {
    val header = lines.find { it.allTextsMatch(Regex("""^\d+[.,]\d{2} *€$""")) }
      ?: return emptyMap()

    val result = mutableMapOf<PdfSection, List<TextLine>>()
    result += PdfSection.TABLE_HEADER to listOf(header)

    val linesBelowHeader =
      lines
        .filter { it.y > header.y }
        .filterNot { it.oneTextMatches(Regex(""".*Zusatzstoffe.*""")) }
        .filterNot { it.oneTextMatches(Regex(""".*(Nährwerte|Kohlenhydrate).*""")) }

    val sortedLines =
      linesBelowHeader
        .sortedBy { it.y }
        .takeWhile { !it.toString().contains("Salat nach Art des Hauses") }

    val averageY = sortedLines.windowed(2, 1).map { it[1].y - it[0].y }.average()

    var previousLine: TextLine? = null

    var linesForDay = emptyList<TextLine>()
    for (line in sortedLines) {
      if (previousLine != null && line.y - previousLine.y > averageY) {
        val weekday = findWeekdaySection(linesForDay)
        weekday?.let { result += it to linesForDay }
        linesForDay = arrayListOf(line)
      } else {
        linesForDay = linesForDay + line
      }
      previousLine = line
    }
    val weekday = findWeekdaySection(linesForDay)
    weekday?.let { result += it to linesForDay }
    linesForDay = emptyList()

    return result
  }

  private fun resolveFromPdfContentWithVerticalPrice(
    lines: List<TextLine>,
    monday: LocalDate
  ): List<LunchOffer> {
    val section2lines = groupBySectionWithVerticalPrice(lines)

    val offers = mutableListOf<LunchOffer>()

    for ((weekday, weekdayLines) in section2lines) {
      val mainLine = weekdayLines.find { it.oneTextMatches(Regex(".*€.*")) }
        ?: continue
      val price = StringParser.parseMoney(mainLine.toString())
        ?: continue
      if (mainLine.texts.size < 3)
        continue

      val priceColumn = PriceColumn(mainLine.texts[1].xMid(), price)
      val offer = parseDayOffer(weekdayLines, weekday, monday, priceColumn)
        ?: continue
      offers += offer
    }
    return offers
  }

  private fun groupBySectionWithVerticalPrice(
    lines: List<TextLine>
  ): Map<PdfSection, List<TextLine>> {
    val sortedLines = lines.sortedBy { it.y }
    val averageY = sortedLines.windowed(2, 1).map { it[1].y - it[0].y }.average()

    val result = mutableMapOf<PdfSection, List<TextLine>>()
    val currentLines = mutableListOf<TextLine>()
    var previousLine: TextLine? = null

    for (line in sortedLines) {
      if (previousLine != null && line.y - previousLine.y > averageY) {
        val mainLine = currentLines.find { it.oneTextMatches(Regex(".*€.*")) }
        if (mainLine != null) {
          val section = PdfSection.weekdayValues.find { mainLine.toString().startsWith(it.label) }
          if (section != null)
            result += section to currentLines.toList()
        }
        currentLines.clear()
      }
      currentLines += line
      previousLine = line
    }

    return result
  }

  private fun parseDayOffer(
    lines: List<TextLine>,
    weekday: PdfSection,
    monday: LocalDate,
    priceColumn: PriceColumn
  ): LunchOffer? {
    if (lines.any { it.oneTextMatches(Regex(".*(Feiertag|Ostermontag|Osterfreitag).*")) })
      return null

    val day = monday.plusDays(weekday.order)
    val name =
      lines
        .flatMap { line -> line.texts.filter { it.xIn(priceColumn.x) } }
        .joinToString(" ")
        .let { parseName(it) } ?: return null

    val (title, description) =
      StringParser.splitOfferName(name, listOf(" auf ", " mit ", " von ", " im ", " an "))

    return LunchOffer(0, title, description, day, priceColumn.price, parseTags(name), provider.id)
  }

  private fun parseTags(name: String): Set<String> {
    val tags = mutableSetOf<String>()
    if (name.contains("vegetarisch", true))
      tags += "vegetarisch"
    if (name.contains("vegan", true))
      tags += "vegan"
    return tags
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
      text
        .trim()
        .replace("  ", " ")
        .replace("süß- sauer", "süß-sauer")

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
