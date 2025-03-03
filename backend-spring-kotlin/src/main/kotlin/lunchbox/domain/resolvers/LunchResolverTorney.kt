package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.TORNEY
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.ocr.OcrClient
import lunchbox.util.string.StringParser
import lunchbox.util.url.UrlUtil.url
import org.joda.money.Money
import org.jsoup.nodes.Element
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
    val monday = parseMonday(imageUrl.toString()) ?: return emptyList()
    return resolveOffersFromText(ocrClient.doOCR(imageUrl), monday)
  }

  fun parseMonday(url: String): LocalDate? {
    val matchOffer = Regex("Tagesgericht-([0-9]{1,2}).KW-([0-9]{4})").find(url) ?: return null
    val (week, year) = matchOffer.destructured
    val date = LocalDate.of(year.toInt(), Month.JANUARY, 10)
    val dayInWeek = date.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week.toLong())
    return dayInWeek.with(DayOfWeek.MONDAY)
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
    monday: LocalDate,
  ): List<LunchOffer> {
    val rawLines = rawText.split('\n').map { correctOcrErrors(it) }
    val lines = filterIrrelevantLines(rawLines)
    val segments = createSegments(lines)
    val rawOffers = createRawOffers(segments)

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

  private fun createSegments(lines: List<String>): List<Text> {
    val segments =
      lines
        .map { if (it.isEmpty()) TextBreak else TextSegment(it, ContentType.UNKNOWN) }
        .map { guessContentType(it) }
        .flatMap { adjustPreposition(it) }

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
        } else {
          segment
        }
    }

  private fun adjustPreposition(segment: Text): List<Text> =
    when (segment) {
      is TextBreak -> listOf(segment)
      is TextSegment -> {
        val matchOffer = Regex("""^(.+) (von|mit)$""").find(segment.text)
        if (matchOffer == null) {
          listOf(segment)
        } else {
          val (pre, post) = matchOffer.destructured
          listOf(TextSegment(pre, segment.contentType), TextSegment(post, ContentType.PREPOSITION))
        }
      }
    }

  private fun createRawOffers(segments: List<Text>): List<RawOffer> {
    val result = mutableListOf<RawOffer>()
    var breakBefore = false
    var prepositionBefore = false

    var newOffer: RawOffer? = null

    for (segment in segments) {
      if (segment is TextBreak) {
        breakBefore = true
      } else if (segment is TextSegment && segment.contentType != ContentType.PRICE) {
        if (newOffer == null) {
          newOffer = RawOffer(segment.text)
        } else if (segment.contentType in
          listOf(
            ContentType.DESCRIPTION,
            ContentType.PREPOSITION,
          ) ||
          prepositionBefore ||
          !breakBefore
        ) {
          newOffer =
            newOffer.copy(
              description =
                if (newOffer.description ==
                  null
                ) {
                  segment.text
                } else {
                  "${newOffer.description} ${segment.text}"
                },
            )
          prepositionBefore = segment.contentType === ContentType.PREPOSITION
        } else {
          result += newOffer
          newOffer = RawOffer(segment.text)
        }
      }
    }
    if (newOffer != null) {
      result += newOffer
    }

    segments
      .filterIsInstance<TextSegment>()
      .filter { it.contentType == ContentType.PRICE }
      .forEachIndexed { index, text ->
        if (result.size >
          index
        ) {
          result[index] = result[index].copy(price = StringParser.parseMoney(text.text))
        }
      }

    return result
  }

  private fun filterIrrelevantLines(contentAsLines: List<String>) =
    contentAsLines
      .filterNot {
        it.matches(Regex("^[0-9.-]+$")) ||
          it.contains("tagesgericht", true) ||
          it.contains("vorrat", true) ||
          it.contains("torney", true)
      }.dropWhile { it.isEmpty() }
      .dropLastWhile { it.isEmpty() }

  private fun resolveOffers(
    dateElem: Element,
    offersElem: Element,
  ): List<LunchOffer> {
    val monday: LocalDate = resolveMonday(dateElem) ?: return emptyList()
    if (!dateValidator.isValid(monday, provider)) return emptyList()

    val tdsAsText =
      offersElem
        .select("td")
        .map { it.text() }

    return tdsAsText
      .chunked(6) // 6 td sind ein Offer ...
      .filter { it.size >= 4 } // ... nur die erste 4 enthalten Daten
      .mapNotNull { (weekday, _, offerName, price) ->
        resolveOffer(monday, weekday, offerName, price)
      }
  }

  private fun resolveMonday(dateElem: Element): LocalDate? {
    val dateString = dateElem.text().replace(" ", "")
    val day = StringParser.parseLocalDate(dateString) ?: return null
    return day.with(DayOfWeek.MONDAY)
  }

  private fun resolveOffer(
    monday: LocalDate,
    weekdayString: String,
    nameString: String,
    priceString: String,
  ): LunchOffer? {
    val weekday = Weekday.entries.find { it.label == weekdayString } ?: return null
    val day = monday.plusDays(weekday.order)
    val price = StringParser.parseMoney(priceString) ?: return null
    val name = nameString
    val (title, description) = StringParser.splitOfferName(name, listOf(" auf ", " mit "))
    return LunchOffer(0, title, description, day, price, emptySet(), provider.id)
  }

  private fun correctOcrErrors(line: String) =
    line
      .trim()
      .replace("TACESGERICHTE", "TAGESGERICHTE")
      .replace("‚", ",")
      .replace(";", "")
      .replace(Regex("^[: ]+"), "")
      .replace("—", "-")
      .replace(Regex(" -$"), "")
      .replace(Regex("[ .:;%@”©‘fi{}]+$"), "")
      .replace(Regex(" [a-zA-Z]$"), "")
      .trim()

  enum class ContentType {
    UNKNOWN,
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
    val name: String,
    val description: String? = null,
    val price: Money? = null,
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
