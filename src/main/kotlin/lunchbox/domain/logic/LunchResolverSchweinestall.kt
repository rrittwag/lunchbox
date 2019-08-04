package lunchbox.domain.logic

import java.net.URL

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.domain.models.LunchProvider.SCHWEINESTALL
import lunchbox.util.html.Html
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

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
    val day = StringParser.parseLocalDate(dayElem.text()) ?: return null
    val price = StringParser.parseMoney(priceElem.text()) ?: return null
    val name = parseName(nameElem)
    return LunchOffer(0, name, day, price, provider.id)
  }

  private fun parseName(elem: Element): String =
    elem.text()
      .trim()
      .replace("\u0084", "") // merkwürdige Anführungszeichen rauswerfen
      .replace("\u0093", "")
}
