package lunchbox.domain.logic

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.util.html.Html
import lunchbox.util.pdf.PdfExtractor
import lunchbox.util.pdf.TextGroup
import lunchbox.util.pdf.TextLine
import mu.KotlinLogging
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Component
class LunchResolverAokCafeteria(
  val dateValidator: DateValidator
) : LunchResolver {

  private val logger = KotlinLogging.logger {}

  override val provider: LunchProvider = AOK_CAFETERIA

  override fun resolve(): List<LunchOffer> {
    val pdfLinks = resolvePdfLinks(provider.menuUrl)
    return resolveFromPdfs(pdfLinks)
  }

  fun resolvePdfLinks(htmlUrl: URL): List<String> {
    val site = Html.load(htmlUrl)

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

      for (priceColumn in priceColumns)
        offers += parseDayOffer(weekdayLines, weekday, monday, priceColumn)
    }
    return offers
  }

  fun parsePriceColumns(priceHeader: TextLine): List<PriceColumn> {
    val normalizedPriceTexts = normalizePriceTexts(priceHeader.texts)
    val xCoords = normalizedPriceTexts.map { it.xMid() }
    val prices = normalizedPriceTexts.mapNotNull { parsePrice(it.toString()) }
    return xCoords.zip(prices).map { PriceColumn(it.first, it.second) }
  }

  fun parseMonday(lines: List<TextLine>): LocalDate? =
    parseMondayFromStrings(lines.map { it.toString() })

  fun parseMondayFromStrings(lines: List<String>): LocalDate? {
    // alle Datumse aus PDF ermitteln
    val days = lines.mapNotNull { parseDay(it) }
    val mondays = days.map { it.with(DayOfWeek.MONDAY) }

    // den Montag der am häufigsten verwendeten Woche zurückgeben
    return mondays
      .groupBy { it }
      .maxBy { it.value.size }
      ?.key
  }

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
  ): LunchOffer {
    val day = monday.plusDays(weekday.order)
    val name =
      lines
        .flatMap { line -> line.texts.filter { it.xIn(priceColumn.x) } }
        .joinToString(" ")
    return LunchOffer(0, parseName(name), day, priceColumn.price, LunchProvider.AOK_CAFETERIA.id)
  }

  private fun findWeekdaySection(lines: List<TextLine>): PdfSection? {
    val weekdaysRegex = PdfSection.weekdayValues.map { it.label }.joinToString("|", "^(", ")$")
    val weekdayTextGroupOpt =
      lines
        .mapNotNull { line -> line.texts.find { it.toString().matches(Regex(weekdaysRegex)) } }
        .firstOrNull()
    return weekdayTextGroupOpt?.let { parseWeekdaySection(it.toString()) }
  }

  /**
   * Manchmal wird die fette Schrift durch Übereinanderlegen von Texten erreicht.
   * Zur Verarbeitung der Mittagsangebote brauchen wir jedoch lediglich "ein Layer".
   */
  private fun normalizePriceTexts(texts: List<TextGroup>): List<TextGroup> =
    texts.groupBy { it.toString() }
      .values.map { text -> text.minBy { it.xMin() }!! }
      .toList().sortedBy { it.xMin() }

  private fun parseDay(dayString: String): LocalDate? {
    Regex(""".*(\d{2}.\d{2}.\d{4}).*""").find(dayString)?.let {
      val (dateString) = it.destructured
      return parseLocalDate(dateString, "dd.MM.yyyy")
    }

    Regex(""".*(\d{2}.\d{2}.\d{2}).*""").find(dayString)?.let {
      val (dateString) = it.destructured
      return parseLocalDate(dateString, "dd.MM.yy")
    }

    Regex(""".*(\d{2}.\d{2}).*""").find(dayString)?.let {
      val (dateString) = it.destructured
      val yearToday = LocalDate.now().year
      val year =
        if (LocalDate.now().monthValue == 12 && dateString.endsWith("01"))
          yearToday + 1
        else
          yearToday
      return parseLocalDate("$dateString.$year", "dd.MM.yyyy")
    }

    return null
  }

  private fun parseLocalDate(dateString: String, dateFormat: String): LocalDate? =
    try {
      LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString))
    } catch (exc: Throwable) {
      null
    }

  private fun parseName(text: String): String = text.trim().replace("  ", " ")

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param priceString String im Format "*0,00*"
   * @return
   */
  private fun parsePrice(priceString: String): Money? {
    val matchResult = Regex(""".*(\d+)[.,](\d{2}).*""").find(priceString)
      ?: return null
    val (major, minor) = matchResult.destructured
    return Money.ofMinor(CurrencyUnit.EUR, major.toLong() * 100 + minor.toLong())
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
