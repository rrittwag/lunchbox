package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.TORNEY
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import lunchbox.util.string.StringParser
import lunchbox.util.url.UrlUtil.url
import org.joda.money.Money
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import java.time.temporal.IsoFields

/**
 * Ermittelt Mittagsangebote für Torney.
 */
@Component
class LunchResolverTorney(
  val dateValidator: DateValidator,
  val ocrClient: OcrClient,
  val htmlParser: HtmlParser,
) : LunchResolver {
  override val provider = TORNEY

  override fun resolve(): List<LunchOffer> = resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val tagesgerichteLinks = resolveImageLinks(url)
    return resolveFromImageLinks(tagesgerichteLinks)
  }

  private fun resolveFromImageLinks(imageUrls: List<URL>): List<LunchOffer> =
    imageUrls.flatMap { resolveTextFromImageLink(it) }

  private fun resolveTextFromImageLink(imageUrl: URL): List<LunchOffer> {
    val monday = parseMonday(imageUrl.toString())
    return resolveOffersFromText(ocrClient.doOCR(imageUrl), monday)
      .filter { dateValidator.isValid(it.day, provider) }
  }

  fun parseMonday(url: String): LocalDate? {
    val matchOffer = Regex("Tagesgericht-([0-9]{1,2}).KW-([0-9]{4})").find(url) ?: return null
    val (week, year) = matchOffer.destructured
    val date = LocalDate.of(year.toInt(), Month.JANUARY, 10)
    val dayInWeek = date.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong())
    return dayInWeek.with(DayOfWeek.MONDAY)
  }

  fun parseMonday(
    lines: List<Text>,
    mondayByUrl: LocalDate?,
  ): LocalDate? {
    val line = lines.filterIsInstance<TextSegment>().find { it.contentType === ContentType.DATE } ?: return mondayByUrl
    return StringParser.parseLocalDate(line.text)?.with(DayOfWeek.MONDAY) ?: mondayByUrl
  }

  fun resolveImageLinks(htmlUrl: URL): List<URL> {
    val site = htmlParser.parse(htmlUrl)

    return site
      .select("a[aria-label=\"Tagesgerichte\"]")
      .map { it.attr("href") }
      .map { url(it) }
  }

  fun resolveOffersFromText(
    rawText: String,
    mondayByUrl: LocalDate?,
  ): List<LunchOffer> {
    val rawLines = rawText.split('\n').map { correctOcrErrors(it) }
    val lines = filterIrrelevantLines(rawLines)
    val segments = createSegments(lines)
    val monday = parseMonday(segments, mondayByUrl) ?: return emptyList()
    val rawOffers = removeInvalidOffers(createRawOffers(segments))

    return Weekday.entries.flatMap { weekday ->
      rawOffers.map {
        LunchOffer(
          0,
          it.name,
          "" + it.description,
          monday.plusDays(weekday.order),
          it.price,
          emptySet(),
          provider.id,
        )
      }
    }
  }

  private fun removeInvalidOffers(rawOffers: List<RawOffer>): List<RawOffer> =
    rawOffers.filterNot {
      it.name.contains("Betriebsklima") ||
        it.description === null ||
        it.description!!.contains("Betriebsklima")
    }

  private fun createSegments(lines: List<String>): List<Text> {
    val segments =
      lines
        .map { if (it.isEmpty()) TextBreak else TextSegment(it, ContentType.UNKNOWN) }
        .map { guessContentType(it) }
        .flatMap { adjustSegment(it) }

    return segments
  }

  fun guessContentType(segment: Text): Text =
    when (segment) {
      is TextBreak -> segment
      is TextSegment ->
        if (segment.text.startsWith("und ") || segment.text.startsWith("mit ")) {
          TextSegment(segment.text, ContentType.DESCRIPTION)
        } else if (segment.text.matches(Regex("[0-9,]+ *€"))) {
          TextSegment(segment.text, ContentType.PRICE)
        } else if (segment.text.matches(Regex(".*[0-9]{2}.[0-9]{2}.[0-9]{4}.*"))) {
          TextSegment(segment.text, ContentType.DATE)
        } else {
          segment
        }
    }

  private fun adjustSegment(segment: Text): List<Text> {
    if (segment is TextSegment) {
      var matchOffer = Regex("""^(.+) ([0-9,]+ *€)$""").find(segment.text)
      if (matchOffer != null) {
        val (pre, post) = matchOffer.destructured
        return listOf(TextSegment(pre, segment.contentType), TextSegment(post, ContentType.PRICE))
      }
      matchOffer = Regex("""^(.+) (mit|und)$""").find(segment.text)
      if (matchOffer != null) {
        val (pre, post) = matchOffer.destructured
        return listOf(TextSegment(pre, segment.contentType), TextSegment(post, ContentType.PREPOSITION))
      }
    }
    return listOf(segment)
  }

  private fun createRawOffers(segments: List<Text>): List<RawOffer> {
    val relevantSegments = skipDateSegments(segments)

    val result = mutableListOf<RawOffer>()
    var breakBefore = false
    var prepositionBefore = false

    var newOffer: RawOffer? = null

    for (segment in relevantSegments) {
      if (segment is TextBreak) {
        breakBefore = true
      } else if (segment is TextSegment) {
        if (newOffer == null) {
          newOffer = RawOffer(segment.text)
          breakBefore = false
        } else if (segment.contentType == ContentType.PRICE) {
          if (!breakBefore) newOffer.price = StringParser.parseMoney(segment.text)
          breakBefore = false
        } else if (segment.contentType in
          listOf(
            ContentType.DESCRIPTION,
            ContentType.PREPOSITION,
          ) ||
          prepositionBefore ||
          !breakBefore
        ) {
          newOffer.description =
            if (newOffer.description == null) {
              segment.text
            } else {
              "${newOffer.description} ${segment.text}"
            }
          prepositionBefore = segment.contentType === ContentType.PREPOSITION
          breakBefore = false
        } else {
          result += newOffer
          newOffer = RawOffer(segment.text)
          breakBefore = false
        }
      }
    }
    if (newOffer != null) {
      result += newOffer
    }

    if (result.all { it.price == null }) {
      relevantSegments
        .filterIsInstance<TextSegment>()
        .filter { it.contentType == ContentType.PRICE }
        .forEachIndexed { index, text ->
          if (result.size > index) {
            result[index].price = StringParser.parseMoney(text.text)
          }
        }
    }

    return result
  }

  private fun skipDateSegments(segments: List<Text>): List<Text> {
    if (segments.none { it is TextSegment && it.contentType == ContentType.DATE }) {
      return segments
    }
    return segments.takeLastWhile { !(it is TextSegment && it.contentType == ContentType.DATE) }
  }

  private fun filterIrrelevantLines(contentAsLines: List<String>) =
    contentAsLines
      .filterNot {
        it.contains("tagesgericht", true) ||
          it.contains("vorrat", true) ||
          it.contains("torney", true)
      }.dropWhile { it.isEmpty() }
      .dropLastWhile { it.isEmpty() }

  private fun correctOcrErrors(line: String) =
    line
      .trim()
      .replace("TACESGERICHTE", "TAGESGERICHTE")
      .replace("‚", ",")
      .replace(";", "")
      .replace(Regex("^[:\" ]+"), "")
      .replace("—", "-")
      .replace(Regex(" -$"), "")
      .replace(Regex("Buter"), "Butter")
      .replace(Regex("^(.+) TORNEY$"), "$1")
      .replace(Regex("[ .:;%@”©‘fi{}]+$"), "")
      .replace(Regex(" [a-zA-Z]$"), "")
      .replace(Regex("mit([A-Z])"), "mit $1")
      .replace(Regex("und([A-Z])"), "und $1")
      .replace(Regex("""([0-9,]+) *<"""), "$1 €")
      .replace(Regex("""([0-9,]+)9 *€"""), "$10 €")
      .replace(Regex("""\(([0-9,]+) *€"""), "$1 €")
      .replace(Regex("""([0-9])([0-9]{2}) *€"""), "$1,$2 €")
      .replace(Regex("^n+$"), "")
      .trim()

  enum class ContentType {
    UNKNOWN,
    DATE,
    TITLE,
    DESCRIPTION,
    PREPOSITION,
    PRICE,
  }

  sealed interface Text

  data class TextSegment(
    val text: String,
    val contentType: ContentType = ContentType.UNKNOWN,
  ) : Text

  data object TextBreak : Text

  data class RawOffer(
    var name: String,
    var description: String? = null,
    var price: Money? = null,
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
}
