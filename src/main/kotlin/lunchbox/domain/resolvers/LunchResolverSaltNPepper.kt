package lunchbox.domain.resolvers

import java.net.URL

import org.springframework.stereotype.Component

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SALT_N_PEPPER
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class LunchResolverSaltNPepper(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser
) : LunchResolver {

  override val provider = SALT_N_PEPPER

  override fun resolve(): List<LunchOffer> =
    resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val site = htmlParser.parse(url)

    val divs = site.select("div.wpb_content_element")
    val monday = resolveMonday(divs) ?: return emptyList()

    return resolveOffers(divs, monday)
  }

  private fun resolveMonday(divs: Elements): LocalDate? =
    divs
      .map { parseName(it.text()).replace("\n", "") }
      .find { it.contains(" Uhr") }
      ?.let { StringParser.parseLocalDate(it) }
      ?.let { toMonday(it) }

  private fun resolveOffers(divs: Elements, monday: LocalDate): List<LunchOffer> {
    val section2node = mutableMapOf<OfferSection, Element>()

    for (div in divs) {
      val h4 = div.select("h4") ?: continue
      val title = parseName(h4.text())

      for (section in OfferSection.values())
        if (section.label == title)
          section2node += section to div
    }

    val dayNodes = section2node.filterKeys { OfferSection.weekdayValues.contains(it) }
    val dayOffers = resolveDayOffers(dayNodes, monday)

    val weekNodes = section2node.filterKeys { !OfferSection.weekdayValues.contains(it) }
    val weekOffers = resolveWeekOffers(weekNodes, dayOffers.map { it.day }.toSet())

    val allOffers = dayOffers + weekOffers
    return allOffers.filter { dateValidator.isValid(it.day) }
  }

  private fun resolveDayOffers(
    section2node: Map<OfferSection, Element>,
    monday: LocalDate
  ): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()
    for ((section, node) in section2node) {
      val pureOffers = resolveSectionOffers(node)
      result.addAll(pureOffers.map { it.copy(day = monday.plusDays(section.order)) })
    }
    return result
  }

  private fun resolveWeekOffers(
    section2node: Map<OfferSection, Element>,
    days: Set<LocalDate>
  ): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()
    for ((_, node) in section2node)
      for (pureOffer in resolveSectionOffers(node))
        for (weekday in days)
          result += pureOffer.copy(name = "Wochenangebot: ${pureOffer.name}", day = weekday)
    return result
  }

  private fun resolveSectionOffers(node: Element): List<LunchOffer> {
    val tds = node.select("td")
    return tds
      .chunked(2)
      .filter { it.size == 2 }
      .mapNotNull { (nameElem, priceElem) -> resolveOffer(nameElem, priceElem) }
  }

  private fun resolveOffer(nameElem: Element, priceElem: Element): LunchOffer? {
    val price = StringParser.parseMoney(priceElem.text()) ?: return null

    // Zusatzstoffe entfernen (hochgestellt/sup)
    nameElem.children().filter { it.tagName() == "sup" }.forEach { it.remove() }

    val name = parseName(nameElem.text())
        .replace(Regex("^Topp-Preis:"), "")
        .replace(Regex("^Tipp:"), "")
        .replace(Regex("[0-9]{1,2}(, [0-9]{1,2})*$"), "")
        .trim()
    return LunchOffer(0, name, LocalDate.now(), price, provider.id)
  }

  private fun parseName(name: String): String =
    name.trim().replace("\n", " ").replace("  ", " ")

  private fun toMonday(day: LocalDate): LocalDate = day.with(DayOfWeek.MONDAY)

  enum class OfferSection(
    val label: String,
    val order: Long
  ) {
    MONTAG("Montag", 0),
    DIENSTAG("Dienstag", 1),
    MITTWOCH("Mittwoch", 2),
    DONNERSTAG("Donnerstag", 3),
    FREITAG("Freitag", 4),
    WOCHENANGEBOT("Unser Wochenangebot", -1);

    companion object {
      val weekdayValues = listOf(MONTAG, DIENSTAG, MITTWOCH, DONNERSTAG, FREITAG)
    }
  }
}
