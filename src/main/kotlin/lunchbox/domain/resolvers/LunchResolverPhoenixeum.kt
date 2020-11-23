package lunchbox.domain.resolvers

import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.util.date.DateValidator
import lunchbox.util.date.HolidayUtil
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component

@Component
class LunchResolverPhoenixeum(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser
) : LunchResolver {

  override val provider = LunchProvider.PHOENIXEUM

  override fun resolve(): List<LunchOffer> = resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val site = htmlParser.parse(url)

    val elements = site.select("div.wrapplan")
    elements.addAll(site.select("#wochenplaene > div.ce_text"))

    for (wochenplanDiv in elements) {
      val monday = resolveMonday(wochenplanDiv)
        ?: continue
      if (!dateValidator.isValid(monday))
        continue

      result += parseOffers(wochenplanDiv, monday)
        .filterNot { HolidayUtil.isHoliday(it.day, provider.location) }
    }

    return result
  }

  private fun parseOffers(wochenplanDiv: Element, monday: LocalDate): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    for (offerNode in wochenplanDiv.select("p")) {
      val pipedOffer = node2text(offerNode)
      val offerAsStrings = pipedOffer.split("|").map { it.trim() }

      val offers = parseOfferAttributes(offerAsStrings, monday)
      result += offers
    }

    return result
  }

  private fun resolveMonday(node: Element): LocalDate? {
    val h3 = node.selectFirst("h3")
    val day = StringParser.parseLocalDate(h3.text()) ?: return null
    return day.with(DayOfWeek.MONDAY)
  }

  private fun node2text(node: Node): String {
    var result = ""
    // Zeilenumbrüche durch Pipe-Zeichen ausdrücken
    if (node is Element && node.tagName() in listOf("br")) result += "|"

    for (child in node.childNodes()) {
      result += when (child) {
        is TextNode -> adjustText(child.text())
        else -> node2text(child)
      }
    }
    return result
  }

  private fun adjustText(text: String) =
    text
      .replace("–", "-")
      .replace(" , ", ", ")
      .replace("\n", "")
      .replace("\\u00a0", " ") // NO-BREAK SPACE durch normales Leerzeichen ersetzen

  private fun parseOfferAttributes(
    offerAttributesAsStrings: List<String>,
    monday: LocalDate
  ): List<LunchOffer> {
    val clearedParts = offerAttributesAsStrings.map { cleanUpString(it) }.filter { it.isNotEmpty() }
    if (clearedParts.size < 3)
      return emptyList()

    val weekdayStr = clearedParts.first()
    val priceStr = clearedParts.last()

    val nameParts = clearedParts.drop(1).dropLast(1)
    val (title, description) = splitOfferName(nameParts)
    if (title.contains("Feiertag")) return emptyList()

    val weekdays =
      if (weekdayStr.contains("bis"))
        Weekday.values()
          .dropLastWhile { !weekdayStr.contains(it.label) }
          .dropWhile { !weekdayStr.contains(it.label) }
      else
        Weekday.values().filter { weekdayStr.contains(it.label) }

    if (weekdays.isEmpty()) return emptyList()

    val price = StringParser.parseMoney(priceStr) ?: return emptyList()

    return weekdays.map {
      val day = monday.plusDays(it.order)
      val zusatzInfo = parseZusatzinfo("$title $description")
      LunchOffer(0, title, description, day, price, zusatzInfo, provider.id)
    }
  }

  private fun splitOfferName(nameParts: List<String>): StringParser.OfferName {
    if (nameParts[0].contains(" - ")) {
      val (titleTemp, behindMinus) = nameParts[0].split(" - ")
      val descr = "$behindMinus ${nameParts.drop(1).joinToString(" ")}"
      return StringParser.OfferName(titleTemp, descr)
    }

    if (nameParts.size > 1) {
      val titleTemp = nameParts[0]
      val descr = nameParts.drop(1).joinToString(" ")
      return StringParser.OfferName(titleTemp, descr)
    }

    return StringParser.splitOfferName(nameParts[0])
  }

  private fun parseZusatzinfo(name: String): Set<String> {
    val result = mutableSetOf<String>()
    if (name.contains("veget"))
      result.add("vegetarisch")
    return result
  }

  private fun cleanUpString(str: String): String {
    return str
      .replace("( ", "(")
      .replace(" )", ")")
      .replace(" ,", ",")
      .trim()
  }

  enum class Weekday(
    val label: String,
    val order: Long
  ) {
    MONTAG("Montag", 0),
    DIENSTAG("Dienstag", 1),
    MITTWOCH("Mittwoch", 2),
    DONNERSTAG("Donnerstag", 3),
    FREITAG("Freitag", 4);
  }
}
