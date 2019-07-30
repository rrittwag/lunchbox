package lunchbox.domain.logic

import java.net.URL

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.util.html.Html
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

/**
 * Ermittelt Mittagsangebote für Schweinestall.
 */
@Component
class LunchResolverSchweinestall : LunchResolver {
  override val provider: LunchProvider = SCHWEINESTALL

  override fun resolve(): List<LunchOffer> =
    resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val site = Html.load(url, "iso-8859-1")

    // Die Tabelle 'cal_content' enthält die Wochenangebote
    val tdsInOffersTable = site.select("#cal_content td")

    return tdsInOffersTable
      .chunked(5) // je 5 td-Elemente sind ein Offer, aber ...
      .filter { it.size >= 3 }
      .mapNotNull {
        // ... nur die ersten 3 td sind nützlich
        (dayElem, priceElem, nameElem) -> resolveOffer(dayElem, priceElem, nameElem)
      }
  }

  private fun resolveOffer(dayElem: Element, priceElem: Element, nameElem: Element): LunchOffer? {
    val day = parseDay(dayElem) ?: return null
    val price = parsePrice(priceElem) ?: return null
    val name = parseName(nameElem)
    return LunchOffer(0, name, day, price, provider.id)
  }

  /**
   * Erzeugt ein LocalDate aus dem Format "*dd.mm.yyyy*"
   *
   * @param elem HTML-Node mit auszuwertendem Text
   * @return
   */
  private fun parseDay(elem: Element): LocalDate? {
    val regex = Regex("""\d{2}.\d{2}.\d{4}""")
    val matchResult = regex.find(elem.text())
      ?: return null
    return parseLocalDate(matchResult.value, "dd.MM.yyyy")
  }

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param elem HTML-Node mit auszuwertendem Text
   * @return
   */
  private fun parsePrice(elem: Element): Money? {
    val regex = Regex("""(\d+)[.,](\d{2})""")
    val matchResult = regex.find(elem.text())
      ?: return null
    val (major, minor) = matchResult.destructured
    return Money.ofMinor(CurrencyUnit.EUR, major.toLong() * 100 + minor.toLong())
  }

  private fun parseName(elem: Element): String =
    elem.text()
      .trim()
      .replace("\u0084", "") // merkwürdige Anführungszeichen rauswerfen
      .replace("\u0093", "")

  private fun parseLocalDate(dateString: String, dateFormat: String): LocalDate? =
    try {
      LocalDate.parse(dateString, DateTimeFormatter.ofPattern(dateFormat))
    } catch (exc: DateTimeParseException) {
      null
    }
}
