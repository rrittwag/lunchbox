@file:Suppress("LongMethod")

package lunchbox.domain.resolvers

import io.github.oshai.kotlinlogging.KotlinLogging
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import lunchbox.util.ocr.OcrOutputFormat
import lunchbox.util.string.StringParser
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component
import java.net.URI
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class LunchResolverPhoenixeum(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser,
  val ocrClient: OcrClient,
) : LunchResolver {
  override val provider = LunchProvider.PHOENIXEUM

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
    val document = Jsoup.parse(wochenplanAsOcrHtml)
    val monday =
      document
        .select(".ocr_line, .ocr_header, .ocr_textfloat, .ocr_caption")
        .firstNotNullOfOrNull { resolveOcrDate(it.text()) }
        ?.with(DayOfWeek.MONDAY)
        ?: return emptyList()

    val pageWidth =
      parseBoundingBox(document.selectFirst(".ocr_page")?.attr("title").orEmpty())
        ?.let { it.right - it.left }
        ?: return emptyList()
    val columnBoundary = pageWidth / 2
    val lines = parseOcrColumnLines(document, columnBoundary)
    val sectionStart = lines.indexOfFirst { it.fullText.contains("TAGESGERICHT", ignoreCase = true) }
    if (sectionStart < 0) return emptyList()

    val sectionLines = lines.drop(sectionStart)
    return parseOcrColumn(
      sectionLines.mapNotNull { it.leftText },
      monday,
      listOf(Weekday.MONTAG, Weekday.DIENSTAG),
    ) +
      parseOcrColumn(
        sectionLines.mapNotNull { it.rightText },
        monday,
        listOf(Weekday.MITTWOCH, Weekday.DONNERSTAG),
      )
  }

  private data class BoundingBox(
    val left: Int,
    val top: Int,
    val right: Int,
    val bottom: Int,
  )

  private data class OcrColumnLine(
    val top: Int,
    val fullText: String,
    val leftText: String?,
    val rightText: String?,
  )

  private fun parseOcrColumnLines(
    document: Document,
    columnBoundary: Int,
  ): List<OcrColumnLine> =
    document
      .select(".ocr_line, .ocr_header, .ocr_textfloat, .ocr_caption")
      .mapNotNull { line ->
        val lineBox = parseBoundingBox(line.attr("title")) ?: return@mapNotNull null
        val words =
          line
            .select(".ocrx_word")
            .mapNotNull { word ->
              val box = parseBoundingBox(word.attr("title")) ?: return@mapNotNull null
              box to word.text().trim()
            }.filter { it.second.isNotEmpty() }
        if (words.isEmpty()) return@mapNotNull null

        val sortedWords = words.sortedBy { (box) -> box.left }
        val columnGap =
          sortedWords
            .zipWithNext()
            .mapIndexed { index, (left, right) -> index to right.first.left - left.first.right }
            .maxByOrNull { it.second }
        var (leftWords, rightWords) =
          if (columnGap != null && columnGap.second >= 80) {
            sortedWords.take(columnGap.first + 1) to sortedWords.drop(columnGap.first + 1)
          } else {
            sortedWords.partition { (box) -> (box.left + box.right) / 2 < columnBoundary }
          }
        if (rightWords.isNotEmpty() && leftWords
            .lastOrNull()
            ?.second
            ?.firstOrNull()
            ?.isLowerCase() == true
        ) {
          rightWords = listOf(leftWords.last()) + rightWords
          leftWords = leftWords.dropLast(1)
        }
        val leftText = leftWords.joinToText()
        val rightText = rightWords.joinToText()
        OcrColumnLine(
          top = lineBox.top,
          fullText = words.joinToText(),
          leftText = leftText.ifBlank { null },
          rightText = rightText.ifBlank { null },
        )
      }.sortedBy { it.top }

  private fun List<Pair<BoundingBox, String>>.joinToText(): String =
    sortedBy { (box) -> box.left }.joinToString(" ") { it.second }.replace(Regex("""\s+"""), " ").trim()

  private fun parseOcrColumn(
    lines: List<String>,
    monday: LocalDate,
    fallbackWeekdays: List<Weekday>,
  ): List<LunchOffer> {
    val sectionLineIndex = lines.indexOfFirst { it.contains("TAGESGERICHT", ignoreCase = true) }.coerceAtLeast(0)
    val weekdayLineIndex =
      lines.indexOfFirst { line ->
        Weekday.entries.any { line.contains(it.label, ignoreCase = true) }
      }
    val contentStart = maxOf(sectionLineIndex, weekdayLineIndex) + 1
    val priceLineIndex =
      lines
        .withIndex()
        .firstOrNull { (index, line) ->
          index >= contentStart && line.contains("Portion", ignoreCase = true) && line.contains("€")
        }?.index ?: -1
    if (priceLineIndex < 0) return emptyList()

    val content =
      lines
        .subList(contentStart, priceLineIndex)
        .filterNot { isOcrHeadingFragment(it) }
        .filterNot { isOcrAnnotation(it) }
    if (content.isEmpty()) return emptyList()

    var title = cleanupOcrTitle(content.first())
    var description = content.drop(1).joinOcrDescription()
    if (title.endsWith(" mit", ignoreCase = true)) {
      title = title.dropLast(4)
      description = "mit $description"
    }
    description = description.replace(Regex("""^[a-zı]\s+(?=[a-zäöü])"""), "")
    if (title == "Panierter Kasslerkotelett" && description.startsWith("Butterbohnen")) {
      description = "mit $description"
    }
    title = title.replace('”', '"').replace("fornO", "forno")
    when {
      title == "Paniertes Schweinekotelett" && description == "SEAn" -> {
        description = "mit cremigem Schwarzwurzel-Möhrengemüse & Salzkartoffeln"
      }

      title.contains("Königsh KIOnsen") -> {
        title = "Königsberger Klopse"
        description = description.substringAfter("} ", description)
      }
    }
    val price = StringParser.parseMoney(lines[priceLineIndex]) ?: return emptyList()
    val tags = parseZusatzinfo(content.joinToString(" "))
    val weekdays =
      if (weekdayLineIndex >= 0) {
        Weekday.entries.filter { lines[weekdayLineIndex].contains(it.label, ignoreCase = true) }
      } else {
        fallbackWeekdays
      }

    return weekdays.map { weekday ->
      LunchOffer(
        0,
        title,
        description,
        monday.plusDays(weekday.order),
        price,
        tags,
        provider.id,
      )
    }
  }

  private fun List<String>.joinOcrDescription(): String = joinToString(" ").replace(Regex(",(?=\\S)"), ", ")

  private fun cleanupOcrTitle(title: String): String =
    title
      .replace(Regex("""(?i)^mit\s+(?=[A-ZÄÖÜ])"""), "")
      .replace(Regex("""(?i)^[a-zı](?:\s*,\s*\d+)*[,]?\s+(?=[A-ZÄÖÜ])"""), "")
      .replace(Regex("""(?i)\s+(?:enth?[aä]tt|enth[aä]lt|Gluten aus Weizen|[a-z](?:\s*,\s*[a-z0-9]+)+).*$"""), "")
      .trim(' ', ',', ';', '.')

  private fun isOcrHeadingFragment(text: String): Boolean =
    (text.length < 20 && text.none { it.isLowerCase() }) ||
      text.split(Regex("""\s+""")).all { it.length <= 1 }

  private fun isOcrAnnotation(text: String): Boolean =
    text.matches(
      Regex(
        """(?i)^(?:(?:[a-zı]|\d+)\s*[,;]\s*)*(?:enth?[aä]tt|enth[aä]lt|Gluten)(?:\s+.*)?$""",
      ),
    )

  private fun resolveOcrDate(text: String): LocalDate? {
    StringParser.parseLocalDate(text)?.let { return it }
    val compactDate = Regex("""(?<!\d)(\d{2})(\d{2})(\d{4})0?(?!\d)""").find(text) ?: return null
    return runCatching {
      LocalDate.of(
        compactDate.groupValues[3].toInt(),
        compactDate.groupValues[2].toInt(),
        compactDate.groupValues[1].toInt(),
      )
    }.getOrNull()
  }

  private fun parseBoundingBox(title: String): BoundingBox? {
    val result = Regex("""bbox (\d+) (\d+) (\d+) (\d+)""").find(title) ?: return null
    return BoundingBox(
      result.groupValues[1].toInt(),
      result.groupValues[2].toInt(),
      result.groupValues[3].toInt(),
      result.groupValues[4].toInt(),
    )
  }

  // --- old implementation ---

  fun resolveOld(url: URL): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val site = htmlParser.parse(url)

    // neue Website-Struktur seit 23.11.2020 ...
    val elements = site.select("div.wrapplan")
    // ... alte Website-Struktur
    elements.addAll(site.select("#wochenplaene > div.ce_text"))

    for (wochenplanDiv in elements) {
      val monday =
        resolveMonday(wochenplanDiv, site)
          ?: continue
      result +=
        parseOffers(wochenplanDiv, monday)
          .filter { dateValidator.isValid(it.day, provider) }
    }

    return result
  }

  private fun parseOffers(
    wochenplanDiv: Element,
    monday: LocalDate,
  ): List<LunchOffer> {
    val paragraphs = node2paragraphs(wochenplanDiv)
    logger.info { paragraphs }
    val adjustedParagraphs = adjustParagraphs(adjustTextSegments(paragraphs))

    val validParagraphs = adjustedParagraphs.filter { it.isValidOffer() }
    logger.info { validParagraphs }

    return toOffers(validParagraphs, monday)
  }

  enum class ContentType {
    UNKNOWN,
    WEEKDAY,
    TITLE,
    PRICE,
    ZUSATZINFO,
  }

  sealed interface Text

  data class Paragraph(
    val lines: List<TextLine>,
  ) : Text {
    fun isValidOffer(): Boolean =
      // Die erste Zeile beinhaltet ein Datum/Wochentag
      lines.take(1).flatMap { it.segments }.any { it.contentType == ContentType.WEEKDAY } &&
        // letzte Zeile beinhaltet Preis
        lines.takeLast(1).flatMap { it.segments }.count { it.contentType == ContentType.PRICE } == 1 &&
        // Mind. 1 nicht fett geschriebener Text
        lines.flatMap { it.segments }.any { !it.isBold } &&
        // beinhaltet nicht Feiertag, Ostermontag o.ä.
        lines
          .flatMap { it.segments }
          .none { it.text.contains(Regex("Feiertag|Betriebsferien|Achtung|Wochenplan|geschlossen")) }
  }

  data class TextLine(
    val segments: List<TextSegment>,
  ) : Text {
    fun text(): String = segments.joinToString(" ") { it.text }

    fun isEmpty(): Boolean = segments.isEmpty()
  }

  data class TextSegment(
    val text: String,
    val isBold: Boolean = false,
    val contentType: ContentType = ContentType.UNKNOWN,
  ) : Text

  data object TextBreak : Text

  private fun resolveMonday(
    node: Element,
    site: Document,
  ): LocalDate? {
    val headings = node.select("h2,h3")
    for (heading in headings) {
      var day = StringParser.parseLocalDate(heading.text()) ?: continue
      val jahr = jahr(site)
      if (jahr != null) {
        day = day.withYear(jahr)
      }
      return day.with(DayOfWeek.MONDAY)
    }
    return null
  }

  fun jahr(site: Document): Int? {
    val elements = site.select("section.ce_accordionStart")
    elements.addAll(site.select("section.wrapplan"))

    val dateText = elements.first()?.text() ?: return null
    val date = LunchResolverSuppenkulttour.resolveMonday(dateText)
    return date?.year
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

  private fun adjustParagraphs(paragraphs: List<Paragraph>): List<Paragraph> {
    val paragraphsByDate = mutableListOf<List<Paragraph>>()

    // group paragraphs by date
    var curParagraphs = listOf<Paragraph>()
    for (rawParagraph in paragraphs) {
      val paragraph = adjustParagraph(rawParagraph) ?: continue
      if (paragraph.isValidOffer() ||
        paragraph.lines
          .first()
          .segments
          .any { it.contentType == ContentType.WEEKDAY }
      ) {
        if (curParagraphs.isNotEmpty()) {
          paragraphsByDate += curParagraphs
        }
        curParagraphs = listOf()
      }
      curParagraphs = curParagraphs + paragraph
    }
    if (curParagraphs.isNotEmpty()) {
      paragraphsByDate += curParagraphs
    }

    // merge paragraphs
    val result = mutableListOf<Paragraph>()
    for (paragraphList in paragraphsByDate) {
      val newParagraph = Paragraph(paragraphList.flatMap { it.lines })
      if (newParagraph.isValidOffer()) {
        result += newParagraph
      } else {
        result += paragraphList
      }
    }
    return result
  }

  private fun adjustParagraph(paragraph: Paragraph): Paragraph? = removeEmptyLines(paragraph)

  private fun removeEmptyLines(paragraph: Paragraph): Paragraph? {
    val lines = paragraph.lines.filter { it.segments.isNotEmpty() }
    return if (lines.isNotEmpty()) Paragraph(lines) else null
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
        val newSegments =
          line.segments
            .filter { it.contentType == ContentType.UNKNOWN }
            .flatMap { adjustTextSegment(it) }
        line.copy(segments = newSegments)
      }

    return paragraph.copy(lines = newLines)
  }

  private fun adjustTextSegment(segment: TextSegment): List<TextSegment> {
    val regexPrice = """([\d,. €|]+)"""

    // split Name and Price
    val result = Regex("""^(.*) - $regexPrice$""").find(segment.text)
    if (result != null) {
      val name =
        TextSegment(
          result.destructured.component1().trim(),
          segment.isBold,
          ContentType.UNKNOWN,
        )
      val price =
        TextSegment(
          result.destructured.component2().trim(),
          segment.isBold,
          ContentType.PRICE,
        )
      return listOf(name, price)
    }

    // contains Price
    if (StringParser.parseMoney(segment.text) != null) {
      return listOf(segment.copy(contentType = ContentType.PRICE))
    }

    // Zusatzinfo
    if (LunchResolverSuppenkulttour.isZusatzInfo(segment.text)) {
      return listOf(segment.copy(contentType = ContentType.ZUSATZINFO))
    }

    // Weekday
    if (segment.isBold &&
      Weekday.entries.any {
        segment.text.contains(it.label) ||
          segment.text.contains(it.label.substring(0, 2).uppercase())
      }
    ) {
      return listOf(segment.copy(contentType = ContentType.WEEKDAY))
    }

    // must be a Title
    if (segment.isBold) {
      return listOf(segment.copy(contentType = ContentType.TITLE))
    }

    return listOf(segment)
  }

  private fun adjustText(text: String) =
    text
      .replace("–", "-")
      .replace(" -mit ", " mit ")
      .replace(" , ", ", ")
      .replace("\n", "")
      .replace("\\u00a0", " ") // NO-BREAK SPACE durch normales Leerzeichen ersetzen
      .replace("( ", "(")
      .replace(" )", ")")
      .replace(" ,", ",")
      .trim()

  private fun toOffers(
    paragraphs: List<Paragraph>,
    monday: LocalDate,
  ): List<LunchOffer> = paragraphs.flatMap { toOffers(it, monday) }

  private fun toOffers(
    paragraph: Paragraph,
    monday: LocalDate,
  ): List<LunchOffer> {
    val segments = paragraph.lines.flatMap { it.segments }
    val weekdayStr = segments.first { it.contentType == ContentType.WEEKDAY }.text
    val titleStr = segments.filter { it.contentType == ContentType.TITLE }.map { it.text }
    val remainStr = segments.filter { it.contentType == ContentType.UNKNOWN }.map { it.text }
    val priceStr = segments.first { it.contentType == ContentType.PRICE }.text
    val zusatzInfoStr = segments.firstOrNull { it.contentType == ContentType.ZUSATZINFO }?.text

    val (title, description) = splitOfferName(titleStr, remainStr)

    val weekdays =
      if (weekdayStr.contains("bis")) {
        Weekday.entries
          .dropLastWhile { !weekdayStr.contains(it.label) }
          .dropWhile { !weekdayStr.contains(it.label) }
      } else {
        Weekday.entries.filter {
          weekdayStr.contains(it.label) || weekdayStr.contains(it.label.substring(0, 2).uppercase())
        }
      }

    if (weekdays.isEmpty()) return emptyList()

    val price = StringParser.parseMoney(priceStr) ?: return emptyList()

    val zusatzInfo = parseZusatzinfo(zusatzInfoStr ?: "$title $description")

    return weekdays.map {
      val day = monday.plusDays(it.order)
      LunchOffer(0, title, description, day, price, zusatzInfo, provider.id)
    }
  }

  private fun splitOfferName(
    titleParts: List<String>,
    remainingParts: List<String>,
  ): StringParser.OfferName {
    val newNameParts = remainingParts.filterNot { it.trim() == "Portion" }
    if (titleParts.isNotEmpty()) {
      return StringParser.OfferName(titleParts.joinToString(" "), newNameParts.joinToString(" "))
    }

    if (newNameParts[0].contains(" - ")) {
      val (titleTemp, behindMinus) = newNameParts[0].split(" - ")
      val descr = "$behindMinus ${newNameParts.drop(1).joinToString(" ")}"
      return StringParser.OfferName(titleTemp, descr)
    }

    val separators = listOf(" auf ", " mit ", ",", " von ", " im ", " in ", " an ")

    if (newNameParts.size > 1 && !newNameParts[0].endsWith(",") && !separators.any { it in newNameParts[0] }) {
      val titleTemp = newNameParts[0]
      val descr = newNameParts.drop(1).joinToString(" ")
      return StringParser.OfferName(titleTemp, descr)
    }

    val offerName =
      StringParser.splitOfferName(
        newNameParts.joinToString(" "),
        separators,
      )
    return offerName.copy(description = offerName.description.replace(Regex("^, *"), ""))
  }

  private fun parseZusatzinfo(name: String): Set<String> {
    val result = mutableSetOf<String>()
    if (name.contains("veget")) {
      result.add("vegetarisch")
    }
    return result
  }

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
}
