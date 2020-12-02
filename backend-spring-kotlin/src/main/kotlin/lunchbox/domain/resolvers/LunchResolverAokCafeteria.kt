package lunchbox.domain.resolvers

import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Month
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.AOK_CAFETERIA
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.jsoup.nodes.Element
import org.springframework.stereotype.Component

@Component
class LunchResolverAokCafeteria(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser
) : LunchResolver {

  override val provider = AOK_CAFETERIA

  override fun resolve(): List<LunchOffer> =
    resolve(URL("${provider.menuUrl}/speiseplan/3/ajax/"))

  fun resolve(htmlUrl: URL): List<LunchOffer> {
    val site = htmlParser.parse(htmlUrl)

    val weekDates =
      site.select(".child")
        .mapNotNull { StringParser.parseLocalDate(it.text()) }
        .map { it.with(DayOfWeek.MONDAY) }

    val weekDivs = site.select("div[class*=child_menu]")

    val offers = mutableListOf<LunchOffer>()
    for ((date, weekDiv) in weekDates.zip(weekDivs))
      offers += resolveByWeek(date, weekDiv)

    return offers
  }

  private fun resolveByWeek(date: LocalDate, weekDiv: Element): List<LunchOffer> {
    val day2node = weekDiv.children().chunked(2).filter { it.size > 1 }

    val offers = mutableListOf<LunchOffer>()
    for ((dayElem, offersDiv) in day2node) {
      val day = calcDay(dayElem, date) ?: return emptyList()
      offers += resolveByDay(day, offersDiv)
    }
    return offers
  }

  private fun calcDay(dateElem: Element, date: LocalDate): LocalDate? {
    val weekdayString = dateElem.select(".day").text()
    val weekday = Weekday.values().find { weekdayString.startsWith(it.label) }
    if (weekday != null)
      return date.plusDays(weekday.order)
    val shortDate = dateElem.select(".short-date").text()
    val year =
      if (shortDate.trim().endsWith("01.") && date.month == Month.DECEMBER) date.year + 1
      else date.year
    return StringParser.parseLocalDate("$shortDate$year")
  }

  private fun resolveByDay(day: LocalDate, offersDiv: Element): List<LunchOffer> {
    val offerDivs = offersDiv.select(".day-usual")

    val offers = mutableListOf<LunchOffer>()
    for (offerElem in offerDivs) {
      val typ = offerElem.selectFirst("span").text()
      val name = offerElem.select("span:nth-of-type(2)").text()
      if (name.isEmpty())
        continue
      val zusatzstoffe = offerElem.select("small").text()
      var (title, description) = StringParser.splitOfferName(
        name, listOf(" auf ", " mit ", " von ", " im ", " in ", " an ", ", ", " (", " und ")
      )
      description = clearDescription(description)
      val tags = parseTags(name, typ, zusatzstoffe)
      offers += LunchOffer(0, title, description, day, null, tags, provider.id)
    }

    return offers
  }

  private fun clearDescription(description: String): String =
    description
      .replace(Regex("^, "), "")
      .replace("(", "")
      .replace(")", ",")
      .replace(Regex(",([^ ])"), ", $1")
      .replace(", , ", ", ")
      .trim()

  private fun parseTags(name: String, typ: String, zusatzstoffe: String): Set<String> {
    val result = mutableSetOf<String>()

    if (name.contains("vegan", ignoreCase = true))
      result += "vegan"
    else if (name.contains("vegetarisch", ignoreCase = true) || zusatzstoffe.contains("V"))
      result += "vegetarisch"

    if (typ.contains("vorbestellen", ignoreCase = true))
      result += "auf Vorbestellung"

    return result
  }

  enum class Weekday(
    val label: String,
    val order: Long
  ) {
    MONTAG("Mo", 0),
    DIENSTAG("Di", 1),
    MITTWOCH("Mi", 2),
    DONNERSTAG("Do", 3),
    FREITAG("Fr", 4);
  }
}
