package lunchbox.domain.resolvers

import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

/**
 * Ermittelt Mittagsangebote für Schweinestall.
 */
@Component
class LunchResolverSchweinestall(
  val htmlParser: HtmlParser
) : LunchResolver {

  override val provider = SCHWEINESTALL

  override fun resolve(): List<LunchOffer> =
    resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val site = htmlParser.parse(url)

    val sections = site.select("section")

    val sectionsByWeek =
      sections // Datum-Section und Offers-Section filtern und je Woche gruppieren
        .filter { it.text().matches(Regex("""[0-9 .-]+""")) || it.text().contains("€") }
        .chunked(2)
        .filter { it.size == 2 }

    return sectionsByWeek.flatMap { (dateElem, offersElem) ->
      resolveOffers(dateElem, offersElem)
    }
  }

  private fun resolveOffers(dateElem: Element, offersElem: Element): List<LunchOffer> {
    val monday: LocalDate = resolveMonday(dateElem) ?: return emptyList()

    val tdsAsText = offersElem.select("td")
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
    priceString: String
  ): LunchOffer? {
    val weekday = Weekday.values().find { it.label == weekdayString } ?: return null
    val day = monday.plusDays(weekday.order)
    val price = StringParser.parseMoney(priceString) ?: return null
    val name = cleanName(nameString)
    return LunchOffer(0, name, day, price, provider.id)
  }

  private fun cleanName(nameString: String): String =
    nameString
      .trim()
      .replace("\u0084", "") // merkwürdige Anführungszeichen rauswerfen
      .replace("\u0093", "")

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
