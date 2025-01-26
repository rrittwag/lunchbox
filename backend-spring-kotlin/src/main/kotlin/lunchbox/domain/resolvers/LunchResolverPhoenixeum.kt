package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class LunchResolverPhoenixeum(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser,
) : LunchResolver {
  override val provider = LunchProvider.PHOENIXEUM

  override fun resolve(): List<LunchOffer> = resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
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
    val adjustedParagraphs = adjustParagraphs(adjustTextSegments(paragraphs))

    val validParagraphs = adjustedParagraphs.filter { it.isValidOffer() }

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
        is TextSegment -> currentLine.add(text)
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
          child is Element && child.tagName() == "br" -> listOf(TextBreak)
          child is Element && child.tagName() == "strong" -> readTexts(child, true)
          else -> readTexts(child, isTitle)
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
      return StringParser.OfferName(titleParts.joinToString { " " }, newNameParts.joinToString { " " })
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
