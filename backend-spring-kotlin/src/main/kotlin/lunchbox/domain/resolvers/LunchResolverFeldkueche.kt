package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.FELDKUECHE
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

/**
 * Mittagsangebote von Feldküche Rühlow ermitteln.
 */
@Component
class LunchResolverFeldkueche(
  val dateValidator: DateValidator,
  val ocrClient: OcrClient,
  val htmlParser: HtmlParser,
) : LunchResolver {
  override val provider = FELDKUECHE

  override fun resolve(): List<LunchOffer> =
    // TODO: parallelize
    resolveFromImageLinks(resolveImageLinks(provider.menuUrl))

  fun resolveImageLinks(htmlUrl: URL): List<URL> {
    val site = htmlParser.parse(htmlUrl)

    val divsWithContent = site.select("div#content_area")
    val links = divsWithContent.select("a").map { it.attr("href") }
    return links.map { url(it) }
  }

  private fun resolveFromImageLinks(imageUrls: List<URL>): List<LunchOffer> =
    imageUrls.flatMap { resolveTextFromImageLink(it) }

  private fun resolveTextFromImageLink(imageUrl: URL): List<LunchOffer> =
    resolveOffersFromText(ocrClient.doOCR(imageUrl))

  fun resolveOffersFromText(rawText: String): List<LunchOffer> {
    val contentAsLines = rawText.split('\n').map { correctOcrErrors(it) }
    val monday = resolveMonday(contentAsLines) ?: return emptyList()

    val rawOffers =
      if (Weekday.entries.any { contentAsLines.contains(it.label) } &&
        contentAsLines.any { it.matches(Regex("""^\d+,(\d{2}) *€$""")) }
      ) {
        resolveOffersWith3rowSplit(contentAsLines)
      } else {
        resolveOffersWith2rowSplit(contentAsLines)
      }

    return rawOffers
      .map { createOffer(it, monday) }
      .filter { dateValidator.isValid(it.day, provider) }
  }

  private fun createOffer(
    raw: RawOffer,
    monday: LocalDate,
  ): LunchOffer {
    val (title, description) =
      StringParser.splitOfferName(
        raw.name,
        listOf(" mit ", "Kartoffeln", "Brot", "Nudeln", "Klöße"),
      )

    return LunchOffer(
      0,
      title,
      description,
      monday.plusDays(raw.weekday.order),
      raw.price,
      emptySet(),
      provider.id,
    )
  }

  private fun resolveMonday(contentAsLines: List<String>): LocalDate? {
    val wochenplanZeile = contentAsLines.find { it.contains("Wochenplan") } ?: return null
    val day = StringParser.parseLocalDate(wochenplanZeile) ?: return null
    val monday = day.with(DayOfWeek.MONDAY)
    return if (dateValidator.isValid(monday, provider)) monday else null
  }

  private fun resolveOffersWith2rowSplit(contentAsLines: List<String>): List<RawOffer> {
    val weekdaysRegex = Regex("(${Weekday.entries.joinToString("|"){ it.label }}) .*")
    val offerTexts =
      contentAsLines
        .filter { it.matches(weekdaysRegex) }
        .filterNot { it.startsWith("Montag bis") }

    fun splitWeekdayAndName(text: String): RawOfferName? {
      val weekdayName = text.takeWhile { it != ' ' }
      val lunchName = text.dropWhile { it != ' ' }
      val weekday = Weekday.entries.find { it.label == weekdayName } ?: return null

      return RawOfferName(weekday, lunchName.replace(Regex(""" \d+,\d{2} *€"""), "").trim())
    }

    val prices =
      contentAsLines
        .filter { it.matches(Regex(""".*\d+,(\d{2}) *€""")) }
        .mapNotNull { StringParser.parseMoney(it) }

    return offerTexts
      .mapNotNull { splitWeekdayAndName(it) }
      .zip(prices)
      .map { (rawName, price) -> RawOffer(rawName.weekday, rawName.name, price) }
  }

  private fun resolveOffersWith3rowSplit(contentAsRows: List<String>): List<RawOffer> {
    fun isWeekday(text: String) = Weekday.entries.map { it.label }.contains(text)
    val relevantRows =
      contentAsRows
        .filter { it.isNotEmpty() }
        .dropWhile { !isWeekday(it) }
    val weekdayTextRows = relevantRows.takeWhile { isWeekday(it) }
    val followingRows = relevantRows.dropWhile { isWeekday(it) }

    val weekdayRows =
      weekdayTextRows
        .mapNotNull { row -> Weekday.entries.find { it.label == row } }

    val prices =
      followingRows
        .filter { it.matches(Regex(""".*\d+,(\d{2}) *€""")) }
        .mapNotNull { StringParser.parseMoney(it) }

    return weekdayRows
      .zip(followingRows)
      .map { (weekday, name) -> RawOfferName(weekday, name) }
      .zip(prices)
      .map { (rawName, price) -> RawOffer(rawName.weekday, rawName.name, price) }
  }

  private fun correctOcrErrors(line: String) =
    line
      .trim()
      .replace("Wochenglan", "Wochenplan")
      .replace("‚", ",")
      .replace("""l)""", "0")
      .replace(Regex("""(\d+,\d{2}) [cCeEs]"""), "$1 €")
      //      .replace("4,00 €", "4,80 €") // besser falsch zu hohe 4,80 als zu niedrige 4,00
      .replace("Kanonen-", "Kartoffeln")
      .replace("ﬂ", "fl")
      .replace("ﬁ", "fi")
      .replace(Regex("Wochen.lan"), "Wochenplan")
      .replace(Regex("Wochen.Ian"), "Wochenplan")
      .replace("eintopl", "eintopf")
      .replace("eintop", "eintopf")
      .replace("eintof", "eintopf")
      .replace("eintopff", "eintopf")
      .replace("Erhsen", "Erbsen")
      .replace("Chili con Garne", "Chili con Carne")
      .replace("—", "-")
      .replace(" _", " ")

  data class RawOfferName(
    val weekday: Weekday,
    val name: String,
  )

  data class RawOffer(
    val weekday: Weekday,
    val name: String,
    val price: Money,
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
