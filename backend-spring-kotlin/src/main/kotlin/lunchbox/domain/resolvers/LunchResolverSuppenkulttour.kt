package lunchbox.domain.resolvers

import io.github.oshai.kotlinlogging.KotlinLogging
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import lunchbox.util.ocr.OcrOutputFormat
import lunchbox.util.string.StringParser
import org.joda.money.Money
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class LunchResolverSuppenkulttour(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser,
  val ocrClient: OcrClient,
) : LunchResolver {
  override val provider = SUPPENKULTTOUR

  override fun resolve(): List<LunchOffer> = resolve(provider.menuUrl)

  private val logger = KotlinLogging.logger {}

  fun resolve(url: URL): List<LunchOffer> {
    val wochenplanImages = resolveWochenplanImages(url)

    val result = mutableListOf<LunchOffer>()

    for (wochenplanImage in wochenplanImages) {
      val wochenplanAsOcrHtml = ocrClient.doOCR(wochenplanImage, OcrOutputFormat.HTML_WITH_TEXT_AND_BOUNDING_BOXES)
      result +=
        parseOffers(wochenplanAsOcrHtml)
          .filter { dateValidator.isValid(it.day, provider) }
    }
    return result
  }

  fun resolveWochenplanImages(url: URL): List<URL> {
    val site = htmlParser.parse(url)
    val referenceDate =
      Regex("""(\d{4}-\d{2}-\d{2})""")
        .find(url.path)
        ?.groupValues
        ?.get(1)
        ?.let(LocalDate::parse)
        ?: LocalDate.now()

    return site
      .select("#wochenkarte .elementor-widget-image")
      .mapNotNull { widget ->
        val image = widget.selectFirst("img") ?: return@mapNotNull null
        val imageUrl = image.attr("data-src").ifBlank { image.attr("src") }
        if (imageUrl.isBlank() || imageUrl.startsWith("data:")) return@mapNotNull null

        // Das unbefüllte Widget für die nächste Woche enthält auf der Website noch ein altes Bild.
        val embeddedDate =
          Regex("""(\d{4}-\d{2}-\d{2})""")
            .find(imageUrl)
            ?.groupValues
            ?.get(1)
            ?.let(LocalDate::parse)
        if (widget.hasClass("sk-week-next") && embeddedDate != null && embeddedDate.isBefore(referenceDate)) {
          return@mapNotNull null
        }
        URI(imageUrl).toURL()
      }.distinct()
  }

  fun parseOffers(wochenplanAsOcrHtml: String): List<LunchOffer> {
    val lines = parseHocrLines(wochenplanAsOcrHtml)
    val monday =
      lines
        .firstNotNullOfOrNull { resolveOcrDate(it.text) }
        ?.with(DayOfWeek.MONDAY)
        ?: return emptyList()

    val offerBlocks = mutableListOf<Pair<List<String>, String>>()
    var currentBlock = mutableListOf<String>()
    for (line in lines) {
      if (line.text.contains("TAGESGERICHT", ignoreCase = true)) break
      if (isOfferPrice(line.text)) {
        if (currentBlock.isNotEmpty()) offerBlocks += currentBlock to line.text
        currentBlock = mutableListOf()
      } else if (!isHocrHeading(line.text)) {
        currentBlock += line.text
      }
    }

    val parsedOffers = offerBlocks.mapNotNull { (offerLines, priceLine) -> parseOffer(offerLines, priceLine) }
    val dates = Weekday.entries.map { monday.plusDays(it.order) }
    return parsedOffers.flatMap { parsed ->
      val offerDates = parsed.weekday?.let { listOf(monday.plusDays(it.order)) } ?: dates
      offerDates.map { day ->
        LunchOffer(
          0,
          parsed.name,
          parsed.description,
          day,
          parsed.price,
          parsed.tags + if (parsed.weekday == null) emptySet() else setOf("Tagessuppe"),
          provider.id,
        )
      }
    }
  }

  private data class HocrLine(
    val text: String,
    val top: Int,
    val left: Int,
  )

  private data class ParsedOcrOffer(
    val name: String,
    val description: String,
    val price: Money?,
    val tags: Set<String>,
    val weekday: Weekday?,
  )

  private fun parseHocrLines(html: String): List<HocrLine> =
    Jsoup
      .parse(html)
      .select(".ocr_line, .ocr_header, .ocr_textfloat, .ocr_caption")
      .mapNotNull { line ->
        val bbox = Regex("""bbox (\d+) (\d+) (\d+) (\d+)""").find(line.attr("title")) ?: return@mapNotNull null
        val text = line.text().replace(Regex("""\s+"""), " ").trim()
        if (text.isEmpty()) return@mapNotNull null
        HocrLine(text, bbox.groupValues[2].toInt(), bbox.groupValues[1].toInt())
      }.sortedWith(compareBy(HocrLine::top, HocrLine::left))

  private fun isOfferPrice(text: String): Boolean =
    text.contains("€") &&
      text.contains("klein", ignoreCase = true) &&
      text.contains("mittel", ignoreCase = true) &&
      text.contains("groß", ignoreCase = true)

  private fun isHocrHeading(text: String): Boolean =
    text.contains("WOCHENPLAN", ignoreCase = true) ||
      text.contains("WOCHENSUPPEN", ignoreCase = true) ||
      text.contains("GANZE WOCHE", ignoreCase = true) ||
      Regex("""\d{2}\.\d{2}.*-.*\d{2}\.\d{2}""").containsMatchIn(text) ||
      Regex("""\b\d{8}\b""").containsMatchIn(text) ||
      Regex("""(?i)^klein\s+\d+\s*ml""").containsMatchIn(text)

  private fun resolveOcrDate(text: String): LocalDate? {
    StringParser.parseLocalDate(text)?.let { return it }
    val compactDate = Regex("""\b(\d{2})(\d{2})(\d{4})\b""").find(text) ?: return null
    return runCatching {
      LocalDate.of(
        compactDate.groupValues[3].toInt(),
        compactDate.groupValues[2].toInt(),
        compactDate.groupValues[1].toInt(),
      )
    }.getOrNull()
  }

  private fun parseOffer(
    lines: List<String>,
    priceLine: String,
  ): ParsedOcrOffer? {
    val relevantLines = lines.filterNot { it.matches(Regex("""^[\dö]+$""", RegexOption.IGNORE_CASE)) }
    if (relevantLines.isEmpty()) return null

    val rawTitle = relevantLines.first()
    val weekday = relevantLines.firstNotNullOfOrNull { resolveOcrWeekday(it) }
    val description =
      relevantLines
        .drop(1)
        .map { removeOcrWeekday(it) }
        .filter { it.isNotBlank() }
        .fold("") { result, line ->
          when {
            result.isEmpty() -> line
            result.last() in setOf(',', '.', ';', ':') -> "$result $line"
            line.startsWith("und ", ignoreCase = true) -> "$result $line"
            line.first().isLowerCase() -> "$result, $line"
            else -> "$result $line"
          }
        }.replace(Regex(",(?=\\S)"), ", ")

    val name = cleanupOcrTitle(rawTitle)
    if (name.isBlank() || description.isBlank()) return null

    return ParsedOcrOffer(
      name = name,
      description = description,
      price = parseOcrPrice(priceLine),
      tags = parseVeggie(rawTitle),
      weekday = weekday,
    )
  }

  private fun parseOcrPrice(priceLine: String): Money? {
    val priceAfterLabel = Regex("""(?i)mittel\s*(\d+[,.]\d+)""").find(priceLine)?.groupValues?.get(1)
    val priceBeforeLabel = Regex("""(?i)(\d+[,.]\d+)\s*€?\s*mittel""").find(priceLine)?.groupValues?.get(1)
    return StringParser.parseMoney(priceBeforeLabel ?: priceAfterLabel ?: return null)
  }

  private fun resolveOcrWeekday(text: String): Weekday? {
    val normalized =
      text
        .trim()
        .substringBefore(' ')
        .lowercase()
        .replace('0', 'o')
        .replace('1', 'i')
        .replace('l', 'i')
    return when {
      normalized.startsWith("montag") -> Weekday.MONTAG
      normalized.startsWith("dienstag") -> Weekday.DIENSTAG
      normalized.startsWith("mittwoch") -> Weekday.MITTWOCH
      normalized.startsWith("donnerstag") -> Weekday.DONNERSTAG
      normalized.startsWith("freitag") -> Weekday.FREITAG
      else -> null
    }
  }

  private fun removeOcrWeekday(text: String): String =
    if (resolveOcrWeekday(text) == null) text else text.trim().substringAfter(' ', "").trim()

  private fun cleanupOcrTitle(title: String): String {
    var result =
      title
        .trim()
        .replace(Regex("""^[‘'.]+\s*"""), "")
        .replace(Regex("""^[EMS]\s+(?=[A-ZÄÖÜ])"""), "")
        .replace(Regex("""^M\s*-\s*"""), "")

    result =
      result.replace(
        Regex(
          """(?i)\s+(?:,?\s*(?:enth[aä]lt|enmnätt|enätt)|,\s*(?:vegan|vegetarisch)|[a-z](?:\s*,\s*[a-z0-9;]*)+\s*(?:,?\s*(?:vegan|vegetarisch|enth[aä]lt.*))?|[-—]?\s*(?:vegan|vegetarisch)|;|[iı6](?=[.,\d]|\s*$)|[.]\d).*$""",
        ),
        "",
      )
    result = result.replace(Regex("""\s*-\s+"""), "-").trim(' ', ';', '.', '-', '—')
    if (result.count { it == '„' } > result.count { it == '“' }) result += "“"
    return result
  }

  // --- old implementation ---

  fun resolveOld(url: URL): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val site = htmlParser.parse(url)

    // Die Wochenangebote sind im section-Element mit der class "ce_accordionStart" enthalten
    val elements = site.select("section.ce_accordionStart")
    elements.addAll(site.select("section.wrapplan"))

    for (wochenplanSection in elements) {
      val monday = resolveMonday(wochenplanSection) ?: continue
      result +=
        parseOffers(wochenplanSection, monday)
          .filter { dateValidator.isValid(it.day, provider) }
    }
    return result
  }

  private fun resolveMonday(node: Element): LocalDate? {
    val togglers = node.select("div.toggler")
    for (toggler in togglers) {
      val togglerText = toggler.text().replace("\n", " ")
      val monday = resolveMonday(togglerText)
      if (monday != null) {
        return monday // nicht ganz sauber: nur das erste Datum ist relevant
      }
    }
    return null
  }

  enum class ContentType {
    UNKNOWN,
    SECTION_TITLE,
    WEEKDAY,
    DATE,
    ZUSATZINFO,
    TITLE,
    DESCRIPTION,
    PRICES,
  }

  sealed interface Text

  data class Paragraph(
    val lines: List<TextLine>,
  ) : Text {
    fun isValidOffer(): Boolean =
      // Die ersten 2 Zeilen beinhalten einen Titel
      lines.take(2).flatMap { it.segments }.any { it.contentType == ContentType.TITLE } &&
        // Exakt 1 Titel
        lines.flatMap { it.segments }.count { it.contentType == ContentType.TITLE } == 1 &&
        // Mind. 1 nicht fett geschriebener Text (Zusatzinfo, Beschreibung, Preis)
        lines.flatMap { it.segments }.any { !it.isBold } &&
        // beinhaltet nicht Feiertag, Ostermontag o.ä.
        lines
          .flatMap { it.segments }
          .none { it.text.contains(Regex("Feiertag|Betriebsferien|Achtung|Wochenplan|geschlossen")) }

    fun hasTitleLike(title: String): Boolean = lines.any { it.hasTitleLike(title) }

    fun hasBoldText(): Boolean = lines.any { it.hasBoldText() }

    fun containsDateOrWeekday(): Boolean = lines.any { it.containsDateOrWeekday() }

    fun containsEmptyLines(): Boolean = lines.any { it.isEmpty() }
  }

  data class TextLine(
    val segments: List<TextSegment>,
  ) : Text {
    fun hasTitleLike(title: String): Boolean = segments.any { it.isTitleLike(title) }

    fun hasBoldText(): Boolean = segments.any { it.isBold }

    fun containsDateOrWeekday(): Boolean =
      segments.any { seg -> seg.contentType in setOf(ContentType.WEEKDAY, ContentType.DATE) }

    fun text(): String = segments.joinToString(" ") { it.text }

    fun isEmpty(): Boolean = segments.isEmpty()
  }

  data class TextSegment(
    val text: String,
    val isBold: Boolean = false,
    val contentType: ContentType = ContentType.UNKNOWN,
  ) : Text {
    fun isTitleLike(title: String): Boolean = isBold && text.contains(title, true)
  }

  data object TextBreak : Text

  data class GroupedParagraphs(
    val wochensuppen: List<Paragraph>,
    val tagessuppen: Map<LocalDate, Paragraph>,
  )

  private fun parseOffers(
    wochenplanSection: Element,
    monday: LocalDate,
  ): List<LunchOffer> {
    val paragraphs = node2paragraphs(wochenplanSection)
    logger.info { paragraphs }
    val adjustedParagraphs = adjustParagraphs(adjustTextSegments(paragraphs))
    val groupedParagraphs = groupParagraphs(adjustedParagraphs, monday)
    logger.info { groupedParagraphs }

    val wochensuppen = parseWochensuppen(groupedParagraphs.wochensuppen, monday)
    val tagessuppen = parseTagessuppen(groupedParagraphs.tagessuppen)

    val multipliedWochensuppen = multiplyWochenangebote(wochensuppen, tagessuppen.map { it.day })
    return tagessuppen + multipliedWochensuppen
  }

  private fun node2paragraphs(node: Node): List<Paragraph> {
    if (node is Element && node.tagName() == "p") {
      val lines = node2textlines(node)
      return listOf(Paragraph(lines))
    }

    val result = mutableListOf<Paragraph>()
    for (child in node.childNodes()) {
      result +=
        when (child) {
          is TextNode -> emptyList()
          else -> node2paragraphs(child)
        }
    }
    return result
  }

  private fun node2textlines(node: Node): List<TextLine> {
    val texts = readTexts(node)
    val lines = mutableListOf<TextLine>()
    var currentLine = mutableListOf<TextSegment>()
    for (text in texts) {
      when (text) {
        is TextSegment -> {
          currentLine.add(text)
        }

        is TextBreak -> {
          lines += TextLine(currentLine)
          currentLine = mutableListOf()
        }

        else -> {}
      }
    }
    if (currentLine.isNotEmpty()) {
      lines += TextLine(currentLine)
    }
    return lines
  }

  private fun readTexts(
    node: Node,
    isTitle: Boolean = false,
  ): List<Text> {
    val result = mutableListOf<Text>()
    for (child in node.childNodes()) {
      result +=
        when {
          child is TextNode -> {
            val text = adjustText(child.text())
            if (text.isEmpty()) emptyList() else listOf(TextSegment(text, isTitle))
          }

          child is Element && child.tagName() == "br" -> {
            listOf(TextBreak)
          }

          child is Element && child.tagName() == "strong" -> {
            readTexts(child, true)
          }

          else -> {
            readTexts(child, isTitle)
          }
        }
    }
    return result
  }

  private fun adjustTextSegments(paragraphs: List<Paragraph>): List<Paragraph> =
    paragraphs.map { adjustTextSegments(it) }

  private fun adjustTextSegments(paragraph: Paragraph): Paragraph {
    // Segmente gruppieren nach isBold
    var newLines =
      paragraph.lines.map { line ->
        val newSegments = mutableListOf<TextSegment>()
        var lastSegment: TextSegment? = null
        for (segment in line.segments) {
          if (lastSegment == null) {
            lastSegment = segment
          } else if (lastSegment.isBold == segment.isBold) {
            lastSegment = lastSegment.copy(text = "${lastSegment.text} ${segment.text}")
          } else {
            newSegments += lastSegment
            lastSegment = segment
          }
        }
        if (lastSegment != null) {
          newSegments += lastSegment
        }
        line.copy(segments = newSegments)
      }

    // Segmente auf bekannte Pattern und Content untersuchen
    newLines =
      newLines.map { line ->
        val newSegments = line.segments.flatMap { adjustTextSegment(it) }
        line.copy(segments = newSegments)
      }

    return paragraph.copy(lines = newLines)
  }

  private fun adjustTextSegment(segment: TextSegment): List<TextSegment> {
    if (segment.contentType != ContentType.UNKNOWN) {
      return listOf(segment)
    }

    if (segment.isBold && (segment.isTitleLike("Tagessuppe") || segment.isTitleLike("Wochensuppe"))) {
      return listOf(segment.copy(contentType = ContentType.SECTION_TITLE))
    }

    val regexWeekdays = Weekday.entries.joinToString("|", "(", ")") { it.label }
    val regexDate = """(\d{2}.\d{2}.\d{2,4})"""
    val regexPrices = """(klein[\d,. €|]+mittel[\d,. €|]+groß[\d,. €|]+)"""

    // split Weekday and Date
    var result = Regex("""^$regexWeekdays.*$regexDate$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isBold, ContentType.WEEKDAY)
      val title = TextSegment(result.destructured.component2().trim(), segment.isBold, ContentType.DATE)
      return listOf(weekday, title)
    }

    // contains Weekday
    result = Regex("""^$regexWeekdays[ -]+$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isBold, ContentType.WEEKDAY)
      return listOf(weekday)
    }

    // split Weekday and Title
    result = Regex("""^$regexWeekdays[ -]+(.*)$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isBold, ContentType.WEEKDAY)
      val title = TextSegment(result.destructured.component2().trim(), segment.isBold, ContentType.TITLE)
      return listOf(weekday, title)
    }

    // split Description and Prices
    result = Regex("""^([^€]*) +$regexPrices$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isBold, ContentType.DESCRIPTION)
      val title = TextSegment(result.destructured.component2().trim(), segment.isBold, ContentType.PRICES)
      return listOf(weekday, title)
    }

    // contains Prices
    if (predictPrice(listOf(segment.text)) != null) {
      return listOf(segment.copy(contentType = ContentType.PRICES))
    }

    // Zusatzinfo
    if (isZusatzInfo(segment.text)) {
      return listOf(segment.copy(contentType = ContentType.ZUSATZINFO))
    }

    // must be a Title
    if (segment.isBold) {
      return listOf(segment.copy(contentType = ContentType.TITLE))
    }

    // is empty line
    if (segment.text.matches(Regex("^[ _-]+$"))) {
      return emptyList()
    }

    return listOf(segment)
  }

  private fun adjustParagraphs(paragraphs: List<Paragraph>): List<Paragraph> {
    val result = mutableListOf<Paragraph>()

    val mergedParagraphs = mergeParagraphs(paragraphs)

    for (paragraph in mergedParagraphs) {
      if (isMultipleParagraphs(paragraph)) {
        result += splitMultiParagraph(paragraph)
      } else if (paragraph.containsEmptyLines()) {
        removeEmptyLines(paragraph)?.let { result += it }
      } else {
        result += paragraph
      }
    }

    return result.map { adjustTitle(it) }
  }

  private fun mergeParagraphs(paragraphs: List<Paragraph>): List<Paragraph> {
    val result = mutableListOf<Paragraph>()
    var previous: Paragraph? = null
    for (paragraph in paragraphs) {
      if (hasNoTitle(paragraph) && previous != null && hasTitleOnly(previous)) {
        result += Paragraph(previous.lines + paragraph.lines)
        previous = null
        continue
      }

      if (previous != null) result += previous
      previous = paragraph
    }
    if (previous != null) {
      result += previous
    }
    return result
  }

  private fun adjustTitle(paragraph: Paragraph): Paragraph {
    if (paragraph.lines.isEmpty()) {
      return paragraph
    }
    val firstLine = paragraph.lines.first()
    if (firstLine.segments.any { it.isBold || it.contentType === ContentType.TITLE }) {
      return paragraph
    }
    if (paragraph.lines.flatMap { it.segments }.none { it.text.contains("€") }) {
      return paragraph
    }
    // Titel ergänzen, sofern nicht vorhanden und es sich vermutlich um ein Offer handelt
    val newSegments = firstLine.segments.map { it.copy(isBold = true, contentType = ContentType.TITLE) }
    return Paragraph(listOf(firstLine.copy(segments = newSegments)) + paragraph.lines.drop(1))
  }

  private fun hasTitleOnly(paragraph: Paragraph): Boolean =
    paragraph.lines.size == 1 &&
      paragraph.lines[0].segments.any { it.isBold && it.contentType != ContentType.SECTION_TITLE }

  private fun hasNoTitle(paragraph: Paragraph): Boolean =
    !paragraph.hasBoldText() && paragraph.lines.any { l -> l.segments.any { it.contentType == ContentType.PRICES } }

  private fun isMultipleParagraphs(paragraph: Paragraph): Boolean = splitMultiParagraph(paragraph).size > 1

  private fun splitMultiParagraph(paragraph: Paragraph): List<Paragraph> = splitByEmptyLines(paragraph)

  private fun splitByEmptyLines(paragraph: Paragraph): List<Paragraph> {
    val result = mutableListOf<Paragraph>()
    var currentLines = mutableListOf<TextLine>()
    for (line in paragraph.lines) {
      if (line.isEmpty()) {
        if (currentLines.isNotEmpty()) result += Paragraph(currentLines)
        currentLines = mutableListOf()
      } else {
        currentLines += line
      }
    }
    if (currentLines.isNotEmpty()) result += Paragraph(currentLines)
    return result
  }

  private fun removeEmptyLines(paragraph: Paragraph): Paragraph? {
    val lines = paragraph.lines.filter { it.segments.isNotEmpty() }
    return if (lines.isNotEmpty()) Paragraph(lines) else null
  }

  enum class ParagraphGroupState {
    PRE,
    WOCHENSUPPEN,
    TAGESSUPPEN,
  }

  private fun groupParagraphs(
    paragraphs: List<Paragraph>,
    monday: LocalDate,
  ): GroupedParagraphs {
    val wochensuppen = mutableListOf<Paragraph>()
    val tagessuppen = mutableMapOf<LocalDate, Paragraph>()
    var state = ParagraphGroupState.PRE

    for (paragraph in paragraphs) {
      when (state) {
        ParagraphGroupState.PRE -> {
          if (paragraph.hasTitleLike("Wochensuppen")) {
            state = ParagraphGroupState.WOCHENSUPPEN
          } else if (paragraph.containsDateOrWeekday()) {
            state = ParagraphGroupState.TAGESSUPPEN
            tagessuppen += parseDateOrWeekday(paragraph, monday) to paragraph
          } else if (paragraph.isValidOffer()) {
            state = ParagraphGroupState.WOCHENSUPPEN
            wochensuppen += paragraph
          }
        }

        ParagraphGroupState.WOCHENSUPPEN -> {
          if (paragraph.hasTitleLike("Tagessuppen")) {
            state = ParagraphGroupState.TAGESSUPPEN
          } else if (paragraph.containsDateOrWeekday()) {
            state = ParagraphGroupState.TAGESSUPPEN
            tagessuppen += parseDateOrWeekday(paragraph, monday) to paragraph
          } else if (paragraph.isValidOffer()) {
            wochensuppen += paragraph
          }
        }

        ParagraphGroupState.TAGESSUPPEN -> {
          if (paragraph.containsDateOrWeekday()) {
            tagessuppen += parseDateOrWeekday(paragraph, monday) to paragraph
          } else if (paragraph.isValidOffer()) {
            val date =
              if (tagessuppen.keys.isNotEmpty()) {
                tagessuppen.keys.max().plusDays(1)
              } else {
                monday
              }
            tagessuppen += date to paragraph
          }
        }
      }
    }

    val validTagessuppen = tagessuppen.filterValues { it.isValidOffer() }

    return GroupedParagraphs(wochensuppen, validTagessuppen)
  }

  private fun parseWochensuppen(
    paragraphs: List<Paragraph>,
    monday: LocalDate,
  ): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()
    for (paragraph in paragraphs) {
      val raw = toRawOffer(paragraph) ?: continue
      result +=
        LunchOffer(
          0,
          raw.name,
          raw.description,
          monday,
          raw.price,
          raw.tags,
          provider.id,
        )
    }
    return result
  }

  private fun parseTagessuppen(paragraphs: Map<LocalDate, Paragraph>): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()
    for ((date, paragraph) in paragraphs) {
      val raw = toRawOffer(paragraph) ?: continue
      result +=
        LunchOffer(
          0,
          raw.name,
          raw.description,
          date,
          raw.price,
          raw.tags + "Tagessuppe",
          provider.id,
        )
    }
    return result
  }

  private fun toRawOffer(para: Paragraph): RawOffer? {
    val allSegments = para.lines.flatMap { it.segments }
    val title = allSegments.firstOrNull { it.contentType == ContentType.TITLE }?.text ?: return null
    val zusatzInfos = allSegments.firstOrNull { it.contentType == ContentType.ZUSATZINFO }?.text ?: ""
    val description =
      allSegments
        .filter { it.contentType in setOf(ContentType.UNKNOWN, ContentType.DESCRIPTION) }
        .joinToString(" ") { it.text }
    val priceText = allSegments.firstOrNull { it.contentType == ContentType.PRICES }?.text ?: ""
    val price = predictPrice(listOf(priceText))
    val tags = parseVeggie(zusatzInfos, title)

    return RawOffer(
      title,
      description,
      price,
      tags,
    )
  }

  private fun predictPrice(lines: List<String>): Money? =
    lines
      .mapNotNull { Regex("""[\|\/] *mittel([\d,. ]*)€.*""").find(it) }
      .mapNotNull { StringParser.parseMoney(it.destructured.component1()) }
      .toSet()
      .firstOrNull()

  private fun parseVeggie(vararg sources: String): Set<String> {
    val result = mutableSetOf<String>()
    for (source in sources) {
      if (source.contains("vegan", true)) result += "vegan"
      if (source.contains(Regex("[Vv]ege?t")) ||
        source.contains(Regex("[Vv]egarisch"))
      ) {
        result += "vegetarisch"
      }
    }
    return result
  }

  private fun multiplyWochenangebote(
    wochenOffers: List<LunchOffer>,
    dates: List<LocalDate>,
  ): List<LunchOffer> {
    val sortedDates = dates.toSet().toList().sorted()
    return wochenOffers.flatMap { offer -> sortedDates.map { date -> offer.copy(day = date) } }
  }

  private fun adjustText(text: String) =
    text
      .trim()
      .replace("–", "-")
      .replace(" , ", ", ")
      .replace("\n", "")
      .replace("\\u00a0", " ") // NO-BREAK SPACE durch normales Leerzeichen ersetzen
      .replace("\u2028", ", ")
      .trim()

  private fun parseDateOrWeekday(
    paragraph: Paragraph,
    monday: LocalDate,
  ): LocalDate {
    val dateSegments = paragraph.lines.flatMap { line -> line.segments.filter { it.contentType == ContentType.DATE } }
    if (dateSegments.isNotEmpty()) {
      val localDate = StringParser.parseLocalDate(dateSegments.first().text)
      if (localDate != null) {
        return localDate
      }
    }

    val weekdaySegments = paragraph.lines.flatMap { it.segments }.filter { it.contentType == ContentType.WEEKDAY }
    if (weekdaySegments.isNotEmpty()) {
      for (weekday in Weekday.entries) {
        if (weekdaySegments.first().text.startsWith(weekday.label)) {
          return monday.plusDays(weekday.order)
        }
      }
    }

    error("no weekday or date")
  }

  data class RawOffer(
    val name: String,
    val description: String,
    val price: Money?,
    val tags: Set<String>,
  )

  enum class Weekday(
    val label: String,
    val order: Long,
  ) {
    MONTAG("Montag", 0),
    DIENSTAG("Dienstag", 1),
    MITTWOCH("Mittwoch", 2),
    DONNERSTAG("Donnerstag", 3),
    FREITAG("Freitag", 4),
  }

  companion object {
    fun resolveMonday(text: String): LocalDate? {
      var matchOffer = Regex(""".*Suppen .*vom +([\d.]+).*""").find(text)
      if (matchOffer == null) {
        matchOffer = Regex("""([\d.]+) bis .*""").find(text)
      }
      if (matchOffer == null) {
        matchOffer = Regex("""([\d.]+) [-–] .*""").find(text)
      }
      if (matchOffer == null) {
        return null
      }

      val (firstDayString) = matchOffer.destructured
      val firstDay = StringParser.parseLocalDate(firstDayString) ?: return null

      // manchmal verrutscht der Wochenbeginn auf den Samstag oder Sonntag davor
      val correctedFirstDay =
        when (firstDay.dayOfWeek) {
          DayOfWeek.SUNDAY -> firstDay.plusDays(1)
          DayOfWeek.SATURDAY -> firstDay.plusDays(2)
          else -> firstDay
        }
      return correctedFirstDay.with(DayOfWeek.MONDAY)
    }

    fun isZusatzInfo(string: String): Boolean {
      val zusatzInfos =
        listOf(
          "vegan",
          "glutenfrei",
          "vegetarisch",
          "veg.",
          "veget.",
          "vegarisch",
          "laktosefrei",
          "veget.gf",
          "gf",
          "lf",
          "enthält",
          "enthälti",
          "vegt.",
          "Gluten",
          "aus",
          "Weizen",
          "Dinkel",
        )
      return string.split(Regex("[|(), ]")).all { it.length < 3 || zusatzInfos.contains(it.trim()) }
    }
  }
}
