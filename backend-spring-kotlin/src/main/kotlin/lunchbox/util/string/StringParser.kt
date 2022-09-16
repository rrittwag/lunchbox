package lunchbox.util.string

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Erzeugt Daten aus Zeichenketten.
 */
object StringParser {

  /**
   * Erzeugt ein Money-Objekt (in EURO) aus dem Format "*0,00*"
   *
   * @param moneyString String im Format "*0,00*"
   * @return
   */
  fun parseMoney(moneyString: String): Money? {
    val matchResult = Regex(""".*(\d+)[.,](\d{2}).*""").find(moneyString)
      ?: return null
    val (major, minor) = matchResult.destructured
    return Money.ofMinor(CurrencyUnit.EUR, major.toLong() * 100 + minor.toLong())
  }

  /**
   * Erzeugt ein LocalDate-Objekt aus den Formaten "*dd.mm.yyyy*", "*dd.mm.yy*" und "*dd.mm*".
   * <p>
   * Aus dem Format "*dd.mm*" entsteht ein Datum des aktuellen Jahres, außer das Datum lappt über
   * ins nächste Jahr.
   * <p>
   *
   * @param dateString String im Format "*dd.mm.yyyy*", "*dd.mm.yy*" oder "*dd.mm*"
   * @return
   */
  fun parseLocalDate(dateString: String): LocalDate? {
    Regex(""".*(\d{2}.\d{2}.\d{4}).*""").find(dateString)?.let {
      val (trimmedDateString) = it.destructured
      return parseLocalDate(trimmedDateString, "dd.MM.yyyy")
    }

    Regex(""".*(\d{2}.\d{2}.\d{2}).*""").find(dateString)?.let {
      val (trimmedDateString) = it.destructured
      return parseLocalDate(trimmedDateString, "dd.MM.yy")
    }

    Regex(""".*(\d{2}.\d{2}).*""").find(dateString)?.let {
      val (trimmedDateString) = it.destructured
      val yearToday = LocalDate.now().year
      val year =
        if (LocalDate.now().monthValue == 12 && trimmedDateString.endsWith("01")) {
          yearToday + 1
        } else {
          yearToday
        }
      return parseLocalDate("$trimmedDateString.$year", "dd.MM.yyyy")
    }

    return null
  }

  /**
   * Parst alle Datumsangaben und gibt für die am häufigsten verwendete Woche den Montag zurück.
   */
  fun parseMondayOfMostUsedWeek(lines: List<String>): LocalDate? {
    // alle Datumse aus PDF ermitteln
    val days = lines.mapNotNull { parseLocalDate(it) }
    val mondays = days.map { it.with(DayOfWeek.MONDAY) }

    // den Montag der am häufigsten verwendeten Woche zurückgeben
    return mondays
      .groupBy { it }
      .maxByOrNull { it.value.size }
      ?.key
  }

  private fun parseLocalDate(dateString: String, dateFormat: String): LocalDate? =
    try {
      LocalDate.from(DateTimeFormatter.ofPattern(dateFormat).parse(dateString))
    } catch (exc: Throwable) {
      null
    }

  /**
   * Zerlegt die Bezeichnung eines Mittagsangebots in Titel und Beschreibung.
   */
  fun splitOfferName(
    name: String,
    splitBefore: List<String> = listOf(" auf ", " mit ", " von ", " im ", " in ", " an ")
  ): OfferName {
    val splitIndex = name.indexOfAny(splitBefore)

    if (splitIndex < 0 && name.contains(",")) {
      val (title, description) = name.split(Regex(","), 2)
      return OfferName(title.trim(), description.trim())
    }

    return when {
      splitIndex <= 0 -> OfferName(name.trim(), "")
      else -> OfferName(name.substring(0, splitIndex).trim(), name.substring(splitIndex).trim())
    }
  }

  data class OfferName(
    val title: String,
    val description: String
  )
}
