package lunchbox.domain.resolvers

import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.BOULEVARD_IMBISS
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component
import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class LunchResolverBoulevardImbiss(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser,
) : LunchResolver {

  override val provider = BOULEVARD_IMBISS

  override fun resolve(): List<LunchOffer> =
    resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val site = htmlParser.parse(url)

    val columns = site.select("div.sppb-column:has(h2)")
    val columnTagesangebote = columns.find { node -> node.text().contains("Tagesangebote") }
    val columnWochenangebote = columns.find { node -> node.text().contains("Wochenangebote") }

    val rawTagesangebote = resolveOffers(columnTagesangebote)
    val rawWochenangebote = resolveOffers(columnWochenangebote)
    val today = LocalDate.now()
    val monday = today.with(DayOfWeek.MONDAY)
    val dates = Weekday.values().map { monday.plusDays(it.order) }
    val tagesangebote = rawTagesangebote.map { it.copy(day = today, tags = setOf("Tagesangebot")) }
    val wochenangebote = multiplyWochenangebote(rawWochenangebote, dates)
    return tagesangebote + wochenangebote
  }

  private fun resolveOffers(elem: Element?): List<LunchOffer> {
    if (elem == null) return emptyList()
    val offersAsText = elem.select(".sppb-addon-content").map { it.text() }
    return offersAsText.mapNotNull { resolveOffer(it) }
  }

  private fun resolveOffer(offerAsText: String): LunchOffer? {
    val matchOffer = Regex("""(.+) (\d+[.,]\d{2}) *€(.*)""").find(offerAsText) ?: return null
    val (text1, priceStr, text2) = matchOffer.destructured
    val name = text1 + if (text2.isBlank()) "" else " " + text2.trim()
    val price = StringParser.parseMoney(priceStr) ?: return null
    val (title, description) = StringParser.splitOfferName(name, listOf(" auf ", " mit "))
    return LunchOffer(0, title, description, LocalDate.now(), price, emptySet(), provider.id)
  }

  private fun multiplyWochenangebote(
    wochenOffers: List<LunchOffer>,
    dates: List<LocalDate>,
  ): List<LunchOffer> {
    val sortedDates = dates.toSet().toList().sorted()
    return wochenOffers.flatMap { offer -> sortedDates.map { date -> offer.copy(day = date) } }
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
