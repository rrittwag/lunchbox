package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.util.date.DateValidator
import lunchbox.util.date.HolidayUtil
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.joda.money.Money
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class LunchResolverSuppenkulttour(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser,
) : LunchResolver {

  override val provider = SUPPENKULTTOUR

  override fun resolve(): List<LunchOffer> = resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val site = htmlParser.parse(url)

    // Die Wochenangebote sind im section-Element mit der class "ce_accordionStart" enthalten
    val elements = site.select("section.ce_accordionStart")
    elements.addAll(site.select("section.wrapplan"))

    for (wochenplanSection in elements) {
      val monday = resolveMonday(wochenplanSection) ?: continue
      if (!dateValidator.isValid(monday)) {
        continue
      }
      result +=
        parseOffers(wochenplanSection, monday)
          .filterNot { HolidayUtil.isHoliday(it.day, provider.location) }
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
    WEEKDAY,
    DATE,
    ZUSATZINFO,
    TITLE,
    DESCRIPTION,
    PRICES,
  }
  sealed interface Text
  data class Paragraph(val lines: List<TextLine>): Text {
    fun isValidOffer(): Boolean =
      // Die ersten 2 Zeilen beinhalten einen Titel
      lines.take(2).flatMap { it.segments }.any { it.contentType == ContentType.TITLE } &&
      // Exakt 1 Titel
      lines.flatMap { it.segments }.count { it.contentType == ContentType.TITLE } == 1 &&
      // Mind. 1 nicht fett geschriebener Text (Zusatzinfo, Beschreibung, Preis)
      lines.flatMap { it.segments }.any { !it.isTitle } &&
      // beinhaltet nicht Feiertag, Ostermontag o.ä.
      lines.flatMap { it.segments }.none { it.text.contains(Regex("Feiertag|Betriebsferien|Achtung|Wochenplan|geschlossen")) }

    fun hasTitleLike(title: String): Boolean = lines.any { it.hasTitleLike(title) }
    fun containsDateOrWeekday(): Boolean = lines.any { it.containsDateOrWeekday() }
    fun containsEmptyLines(): Boolean = lines.any { it.isEmpty() }
  }
  data class TextLine(val segments: List<TextSegment>): Text {

    fun hasTitleLike(title: String): Boolean =
      segments.any { it.isTitle && it.text.contains(title, true) }

    fun containsDateOrWeekday(): Boolean =
      segments.any { seg -> seg.contentType in setOf(ContentType.WEEKDAY, ContentType.DATE) }

    fun text(): String = segments.joinToString(" ") { it.text }
    fun isEmpty(): Boolean = segments.isEmpty()
  }
  data class TextSegment(
    val text: String,
    val isTitle: Boolean = false,
    val contentType: ContentType = ContentType.UNKNOWN,
  ): Text
  object TextBreak: Text

  data class GroupedParagraphs(val wochensuppen: List<Paragraph>, val tagessuppen: Map<LocalDate, Paragraph>)

  private fun parseOffers(wochenplanSection: Element, monday: LocalDate): List<LunchOffer> {
    val paragraphs = node2paragraphs(wochenplanSection)
    val adjustedParagraphs = adjustParagraphs(adjustTextSegments(paragraphs))
    val groupedParagraphs = groupParagraphs(adjustedParagraphs, monday)
    println(groupedParagraphs)

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
      result += when (child) {
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
    if (currentLine.isNotEmpty())
      lines += TextLine(currentLine)
    return lines
  }

  private fun readTexts(node: Node, isTitle: Boolean = false): List<Text> {
    val result = mutableListOf<Text>()
    for (child in node.childNodes()) {
      result += when {
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

  private fun adjustTextSegments(paragraphs: List<Paragraph>): List<Paragraph> =
    paragraphs.map { adjustTextSegments(it) }

  private fun adjustTextSegments(paragraph: Paragraph): Paragraph {
    // Segmente gruppieren nach isTitle
    var newLines = paragraph.lines.map { line ->
      val newSegments = mutableListOf<TextSegment>()
      var lastSegment: TextSegment? = null
      for (segment in line.segments) {
        if (lastSegment == null)
          lastSegment = segment
        else if (lastSegment.isTitle == segment.isTitle)
          lastSegment = lastSegment.copy(text = "${lastSegment.text} ${segment.text}")
        else {
          newSegments += lastSegment
          lastSegment = segment
        }
      }
      if (lastSegment != null)
        newSegments += lastSegment
      line.copy(segments = newSegments)
    }

    // falls der Titel falsch gesetzt wurde
    newLines = adjustTitle(newLines)

    // Segmente auf bekannte Pattern und Content untersuchen
    newLines = newLines.map { line ->
      val newSegments = line.segments
        .filter { it.contentType == ContentType.UNKNOWN }
        .flatMap { adjustTextSegment(it) }
      line.copy(segments = newSegments)
    }

    return paragraph.copy(lines = newLines)
  }

  private fun adjustTitle(lines: List<TextLine>): List<TextLine> {
    if (lines.isEmpty())
      return lines
    val firstLine = lines.first()
    if (firstLine.segments.any { it.isTitle })
      return lines
    if (lines.flatMap { it.segments }.none { it.text.contains("€") })
      return lines
    val newSegments = firstLine.segments.map { it.copy(isTitle = true) }
    return listOf(firstLine.copy(segments = newSegments)) + lines.drop(1)
  }

  private fun adjustTextSegment(segment: TextSegment): List<TextSegment> {
    val regexWeekdays = Weekday.values().joinToString("|", "(", ")") { it.label }
    val regexDate = """(\d{2}.\d{2}.\d{2,4})"""
    val regexPrices = """(klein[\d,. €|]+mittel[\d,. €|]+groß[\d,. €|]+)"""

    // split Weekday and Date
    var result = Regex("""^$regexWeekdays.*$regexDate$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isTitle, ContentType.WEEKDAY)
      val title = TextSegment(result.destructured.component2().trim(), segment.isTitle, ContentType.DATE)
      return listOf(weekday, title)
    }

    // split Weekday and Title
    result = Regex("""^$regexWeekdays[ -]+(.*)$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isTitle, ContentType.WEEKDAY)
      val title = TextSegment(result.destructured.component2().trim(), segment.isTitle, ContentType.TITLE)
      return listOf(weekday, title)
    }

    // split Description and Prices
    result = Regex("""^([^€]*) +$regexPrices$""").find(segment.text)
    if (result != null) {
      val weekday = TextSegment(result.destructured.component1().trim(), segment.isTitle, ContentType.DESCRIPTION)
      val title = TextSegment(result.destructured.component2().trim(), segment.isTitle, ContentType.PRICES)
      return listOf(weekday, title)
    }

    // contains Prices
    if (predictPrice(listOf(segment.text)) != null)
      return listOf(segment.copy(contentType = ContentType.PRICES))

    // Zusatzinfo
    if (isZusatzInfo(segment.text))
      return listOf(segment.copy(contentType = ContentType.ZUSATZINFO))

    // must be a Title
    if (segment.isTitle)
      return listOf(segment.copy(contentType = ContentType.TITLE))

    return listOf(segment)
  }

  private fun adjustParagraphs(paragraphs: List<Paragraph>): List<Paragraph> {
    val result = mutableListOf<Paragraph>()
    for (paragraph in paragraphs) {
      if (isMultipleParagraphs(paragraph))
        result += splitMultiParagraph(paragraph)
      else if (paragraph.containsEmptyLines()) {
        removeEmptyLines(paragraph)?.let { result += it }
      } else
        result += paragraph
    }
    return result
  }

  private fun isMultipleParagraphs(paragraph: Paragraph): Boolean = splitMultiParagraph(paragraph).size > 1

  private fun splitMultiParagraph(paragraph: Paragraph): List<Paragraph> = splitByEmptyLines(paragraph)

  private fun splitByEmptyLines(paragraph: Paragraph): List<Paragraph> {
    val result = mutableListOf<Paragraph>()
    var currentLines = mutableListOf<TextLine>()
    for (line in paragraph.lines) {
      if (line.isEmpty()) {
        if (currentLines.isNotEmpty()) result += Paragraph(currentLines)
        currentLines = mutableListOf()
      } else
        currentLines += line
    }
    if (currentLines.isNotEmpty()) result += Paragraph(currentLines)
    return result
  }

  private fun removeEmptyLines(paragraph: Paragraph): Paragraph? {
    val lines = paragraph.lines.filter { it.segments.isNotEmpty() }
    return if (lines.isNotEmpty()) Paragraph(lines) else null
  }

  enum class ParagraphGroupState {
    PRE, WOCHENSUPPEN, TAGESSUPPEN
  }

  private fun groupParagraphs(paragraphs: List<Paragraph>, monday: LocalDate): GroupedParagraphs {
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
            val date = if (tagessuppen.keys.isNotEmpty())
              tagessuppen.keys.max().plusDays(1)
            else
              monday
            tagessuppen += date to paragraph
          }
        }
      }
    }

    val validTagessuppen = tagessuppen.filterValues { it.isValidOffer() }

    return GroupedParagraphs(wochensuppen, validTagessuppen)
  }

  private fun parseWochensuppen(paragraphs: List<Paragraph>, monday: LocalDate): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()
    for (paragraph in paragraphs) {
      val raw = toRawOffer(paragraph) ?: continue
      result += LunchOffer(
        0,
        raw.name,
        raw.description,
        monday,
        raw.price,
        raw.tags,
        provider.id
      )
    }
    return result
  }

  private fun parseTagessuppen(paragraphs: Map<LocalDate, Paragraph>): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()
    for ((date, paragraph) in paragraphs) {
      val raw = toRawOffer(paragraph) ?: continue
      result += LunchOffer(
        0,
        raw.name,
        raw.description,
        date,
        raw.price,
        raw.tags + "Tagessuppe",
        provider.id
      )
    }
    return result
  }

  private fun toRawOffer(para: Paragraph): RawOffer? {
    val title = para.lines.flatMap { line -> line.segments.filter { it.contentType == ContentType.TITLE } }.firstOrNull()?.text ?: return null
    val zusatzInfos = para.lines.flatMap { line -> line.segments.filter { it.contentType == ContentType.ZUSATZINFO } }.firstOrNull()?.text ?: ""
    val descriptionSegments = para.lines.flatMap { it.segments }.filter { it.contentType in setOf(ContentType.UNKNOWN, ContentType.DESCRIPTION ) }
    val description = descriptionSegments.joinToString (" ") { it.text }
    val priceText = para.lines.flatMap { line -> line.segments.filter { it.contentType == ContentType.PRICES } }.firstOrNull()?.text ?: ""
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
      .mapNotNull { Regex("""\| *mittel([\d,. ]*)€.*""").find(it) }
      .mapNotNull { StringParser.parseMoney(it.destructured.component1()) }
      .toSet().firstOrNull()

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

  private fun isZusatzInfo(string: String): Boolean {
    val zusatzInfos = listOf(
      "vegan", "glutenfrei", "vegetarisch", "veg.", "veget.", "vegarisch",
      "laktosefrei", "veget.gf", "gf", "lf", "enthält", "enthälti", "vegt.",
    )
    return string.split(Regex("[|(), ]")).all { it.length < 3 || zusatzInfos.contains(it.trim()) }
  }

  private fun adjustText(text: String) =
    text.trim()
      .replace("–", "-")
      .replace(" , ", ", ")
      .replace("\n", "")
      .replace("\\u00a0", " ") // NO-BREAK SPACE durch normales Leerzeichen ersetzen
      .trim()

  private fun parseDateOrWeekday(paragraph: Paragraph, monday: LocalDate): LocalDate {
    val dateSegments = paragraph.lines.flatMap { line -> line.segments.filter { it.contentType == ContentType.DATE } }
    if (dateSegments.isNotEmpty()) {
      val localDate = StringParser.parseLocalDate(dateSegments.first().text)
      if (localDate != null)
        return localDate
    }

    val weekdaySegments = paragraph.lines.flatMap { line -> line.segments.filter { it.contentType == ContentType.WEEKDAY } }
    if (weekdaySegments.isNotEmpty()) {
      for (weekday in Weekday.values()) {
        if (weekdaySegments.first().text.startsWith(weekday.label))
          return monday.plusDays(weekday.order)
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
      val correctedFirstDay = when (firstDay.dayOfWeek) {
        DayOfWeek.SUNDAY -> firstDay.plusDays(1)
        DayOfWeek.SATURDAY -> firstDay.plusDays(2)
        else -> firstDay
      }
      return correctedFirstDay.with(DayOfWeek.MONDAY)
    }
  }
}
