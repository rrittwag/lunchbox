package lunchbox.domain.resolvers

import java.net.URL
import java.time.DayOfWeek
import java.time.LocalDate
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.SUPPENKULTTOUR
import lunchbox.util.date.DateValidator
import lunchbox.util.date.HolidayUtil
import lunchbox.util.html.HtmlParser
import lunchbox.util.string.StringParser
import org.joda.money.Money
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.nodes.TextNode
import org.springframework.stereotype.Component

@Component
class LunchResolverSuppenkulttour(
  val dateValidator: DateValidator,
  val htmlParser: HtmlParser
) : LunchResolver {

  override val provider = SUPPENKULTTOUR

  override fun resolve(): List<LunchOffer> = resolve(provider.menuUrl)

  fun resolve(url: URL): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val site = htmlParser.parse(url)

    // Die Wochenangebote sind im section-Element mit der class "ce_accordionStart" enthalten
    for (wochenplanSection in site.select("section.ce_accordionStart")) {
      val monday = resolveMonday(wochenplanSection) ?: continue
      if (!dateValidator.isValid(monday))
        continue
      result +=
        parseOffers(wochenplanSection, monday)
          .filterNot { HolidayUtil.isHoliday(it.day, provider.location) }
    }
    return result
  }

  private fun resolveMonday(node: Element): LocalDate? {
    val togglers = node.select("div.toggler")
    for (toggler in togglers) {
      val togglerText = toggler.text().replace("\n", " ")
      val monday = resolveMonday(togglerText)
      if (monday != null)
        return monday // nicht ganz sauber: nur das erste Datum ist relevant
    }
    return null
  }

  private fun resolveMonday(text: String): LocalDate? {
    val matchOffer = Regex(""".*Suppen .*vom +([\d.]+).*""").find(text) ?: return null
    val (firstDayString) = matchOffer.destructured
    val firstDay = StringParser.parseLocalDate(firstDayString) ?: return null

    // manchmal verrutscht der Wochenbeginn auf den Samstag oder Sonntag davor
    val correctedFirstDay = when (firstDay.dayOfWeek) {
      DayOfWeek.SUNDAY -> firstDay.plusDays(1)
      DayOfWeek.SATURDAY -> firstDay.plusDays(2)
      else -> firstDay
    }
    return correctedFirstDay.with(DayOfWeek.MONDAY)
  }

  private fun parseOffers(wochenplanSection: Element, monday: LocalDate): List<LunchOffer> {
    // die Daten stecken in vielen, undefiniert angeordneten HTML-Elementen, daher lieber
    // als Reintext auswerten (mit Pipes als Zeilenumbrüchen)
    val wochenplanString = node2text(wochenplanSection)

    val wochensuppenStart = wochenplanString.indexOf("Die Wochensuppen")
    val tagessuppenStart = wochenplanString.indexOf("Die Tagessuppen")

    val wochensuppen =
      if (wochensuppenStart > -1 && wochensuppenStart < tagessuppenStart) {
        val wochensuppenString =
          wochenplanString.substring(wochensuppenStart + 16, tagessuppenStart)
        parseWochensuppen(removeLeadingPipes(wochensuppenString), monday)
      } else
        emptyList()

    val tagessuppen =
      if (tagessuppenStart > -1) {
        val tagessuppenString = wochenplanString.substring(tagessuppenStart + 15)
        parseTagessuppen(removeLeadingPipes(tagessuppenString), monday)
      } else
        emptyList()

    val multipliedWochensuppen = multiplyWochenangebote(wochensuppen, tagessuppen.map { it.day })
    return tagessuppen + multipliedWochensuppen
  }

  private fun parseWochensuppen(text: String, monday: LocalDate): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val wochensuppenStrings =
      cleanUnnecessaryInfo(highlightOfferBorders(text))
        .split("|||")
        .map { cleanUpPipes(it) }

    val predictedPrice = predictPrice(wochensuppenStrings)

    for (wochensuppeString in wochensuppenStrings) {
      val offerAsStrings = wochensuppeString.split("|").map { it.trim() }
      val rawOffer = parseOfferAttributes(offerAsStrings, predictedPrice) ?: continue
      result += LunchOffer(0, rawOffer.name, monday, rawOffer.price, provider.id)
    }
    return result
  }

  private fun parseTagessuppen(text: String, monday: LocalDate): List<LunchOffer> {
    val result = mutableListOf<LunchOffer>()

    val tagessuppenStrings =
      cleanUnnecessaryInfo(highlightOfferBorders(text))
        .split("|||")
        .map { cleanUpPipes(it) }

    val predictedPrice = predictPrice(tagessuppenStrings)

    for ((day, tagessuppeString) in groupTagessuppeByDay(tagessuppenStrings, monday)) {
      val offerAsStrings = tagessuppeString.split("|").map { it.trim() }
      val rawOffer = parseOfferAttributes(offerAsStrings, predictedPrice) ?: continue
      result += LunchOffer(0, rawOffer.name, day, rawOffer.price, provider.id)
    }
    return result
  }

  private fun highlightOfferBorders(text: String): String =
    text
      .replace(Regex("""\| *\|"""), "||")
      .replace(Regex("""groß *(\d+[.,]\d{2}) ?€? *\|"""), """groß $1 €||""")
      .replace(Regex("""([^|]) *\(enthält"""), "$1|(enthält")

  private fun cleanUnnecessaryInfo(text: String): String =
    text
      // unnütze Info "Standort nicht besetzt" entfernen
      .replace(Regex("""\|{2,3}[^|]*Standort[^|]*\|{2,3} *"""), "||")
      // unnütze Info "Achtung !!!!Plan gilt auch am" entfernen
      .replace(Regex("""\|{2,3}[^|]*Achtung[^|]*\|{2,3} *"""), "||")
      .replace(Regex("""\|{2,3}[^|]*Betriebsferien[^|]*\|{2,3} *"""), "||")
      // TODO: Notbehelfe für krumm formatierte Weihnachtswoche entfernen
      .replace(Regex("""\.(20\d\d)\|\|\|"""), ".$1||")
      .replace(Regex("""([a-z]) *klein 3,50 €"""), "$1||klein 3,50 €")

  private fun predictPrice(lines: List<String>): Money? =
    lines
      .mapNotNull { Regex("""\| *mittel([\d,. ]*)€.*""").find(it) }
      .mapNotNull { StringParser.parseMoney(it.destructured.component1()) }
      .toSet().firstOrNull()

  private fun parseOfferAttributes(
    offerAttributesAsStrings: List<String>,
    predictedPrice: Money?
  ): RawOffer? {
    val clearedParts = offerAttributesAsStrings.map { cleanUpString(it) }.filter { it.isNotEmpty() }
    if (clearedParts.isEmpty())
      return null

    val title = clearedParts.first().trim()
    val remainingParts = clearedParts.drop(1)

    val description = mutableListOf<String>()
    var price: Money? = predictedPrice

    for (part in remainingParts) {
      if (isZusatzInfo(part))
        continue // erstmal ignorieren

      val match = Regex("""(.+) (\d+[.,]\d{2}) ?€? *""").find(part)
      if (match != null) {
        val (portion, priceStr) = match.destructured
        if (portion.trim() == "mittel")
          price = StringParser.parseMoney(priceStr)
        continue
      }

      description += part.trim()
    }

    if (price == null)
      return null

    val name = formatName(title, description) ?: return null

    return RawOffer(name, price)
  }

  private fun formatName(title: String, description: List<String>): String? {
    val formattedDescription = description.filter { it.isNotEmpty() }

    return when {
      title.isEmpty() && formattedDescription.isEmpty() -> null
      title.split(" ").contains("Feiertag") -> null
      formattedDescription.isEmpty() -> title
      else -> "$title: ${formattedDescription.joinToString(" ")}"
    }
  }

  private fun cleanUpString(str: String): String {
    val replacedStr =
      str
        .replace(Regex("""€ *€"""), "€")
        .replace(Regex("""^[a-zA-Z]$"""), "")

    return replacedStr.split(" ").asSequence()
      .filter { s -> s != "(" && s != ")" }
      .filter { it.isNotEmpty() }
      .joinToString(" ").trim()
  }

  private fun multiplyWochenangebote(
    wochenOffers: List<LunchOffer>,
    dates: List<LocalDate>
  ): List<LunchOffer> {
    val sortedDates = dates.toSet().toList().sorted()
    return wochenOffers.flatMap { offer -> sortedDates.map { date -> offer.copy(day = date) } }
  }

  private fun node2text(node: Node): String {
    var result = ""
    // Zeilenumbrüche durch Pipe-Zeichen ausdrücken
    if (node is Element && node.tagName() in listOf("br", "p")) result += "|"

    for (child in node.childNodes()) {
      result += when (child) {
        is TextNode -> adjustText("${child.text()}|")
        else -> node2text(child)
      }
    }
    return result
  }

  private fun cleanUpPipes(text: String) =
    removeLeadingPipes(text)
      // add pipe after Zusatzinfos
      .replaceFirst(Regex("""\) """), ")|")

  private fun removeLeadingPipes(text: String) =
    text.trim()
      // remove leading pipes
      .replaceFirst(Regex("""^[ |]+"""), "")

  private fun adjustText(text: String) =
    text
      .replace("–", "-")
      .replace(" , ", ", ")
      .replace("\n", "")
      .replace("\\u00a0", " ") // NO-BREAK SPACE durch normales Leerzeichen ersetzen

  private fun isZusatzInfo(string: String): Boolean {
    val zusatzInfos = listOf(
      "vegan", "glutenfrei", "vegetarisch", "veg.", "veget.",
      "laktosefrei", "veget.gf", "gf", "lf", "enthält", "enthälti"
    )
    return string.split(Regex("[(), ]")).all { it.length < 3 || zusatzInfos.contains(it.trim()) }
  }

  private fun groupTagessuppeByDay(
    tagessuppenStrings: List<String>,
    monday: LocalDate
  ): Map<LocalDate, String> {
    val result = mutableMapOf<LocalDate, String>()

    // Wochen mit vielen Feiertagen enthalten ggf. Datumse
    if (tagessuppenStrings.any { StringParser.parseLocalDate(it) != null }) {
      for (tagessuppeString in tagessuppenStrings) {
        val date2name = extractDate(tagessuppeString)
        if (date2name != null)
          result += date2name.date to date2name.name
      }

    // Standardfall: "Montag" bis "Freitag" (ohne Datum)
    } else {
      var currentWeekday = Weekday.MONTAG
      for (tagessuppeString in tagessuppenStrings) {
        val weekday2name = extractWeekday(tagessuppeString, currentWeekday)
        val weekday = weekday2name.weekday
        result += monday.plusDays(weekday.order) to weekday2name.name
        currentWeekday = Weekday.values().find { it.order == weekday.order + 1 } ?: break
      }
    }
    return result
  }

  private fun extractDate(text: String): Date2Name? {
    val texts = text.split(Regex("""\|\|"""), 2)
    if (texts.size < 2) return null

    val (dateString, remainingText) = texts
    val date = StringParser.parseLocalDate(dateString) ?: return null
    return Date2Name(date, remainingText)
  }

  private fun extractWeekday(text: String, predictedWeekday: Weekday): Weekday2Name {
    val weekdayNames = Weekday.values().joinToString("|") { it.label }
    val match = Regex("""^ *($weekdayNames) *[-|]*(.*)""").find(text)
    if (match != null) {
      val (weekdayString, remainingText) = match.destructured
      val weekday = Weekday.values().find { it.label == weekdayString }
      if (weekday != null)
        return Weekday2Name(weekday, remainingText)
    }
    return Weekday2Name(predictedWeekday, text)
  }

  data class RawOffer(
    val name: String,
    val price: Money
  )

  data class Weekday2Name(
    val weekday: Weekday,
    val name: String
  )

  data class Date2Name(
    val date: LocalDate,
    val name: String
  )

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
