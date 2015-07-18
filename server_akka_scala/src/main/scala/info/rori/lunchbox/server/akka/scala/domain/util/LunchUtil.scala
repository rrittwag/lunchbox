package info.rori.lunchbox.server.akka.scala.domain.util

import org.joda.time.LocalDate

object LunchUtil {

  /**
   * Es sind nur die Mittagsangebote ab der vergangenen Woche interessant. Diese Methode prüft, ein Tag innerhalb dieses Zeitrahmens liegt.
   * <p>
   * @param day der Tag
   * @return
   */
  def isDayRelevant(day: LocalDate): Boolean = {
    val mondayThisWeek = toMonday(LocalDate.now())
    val mondayLastWeek = mondayThisWeek.minusWeeks(1)
    day.compareTo(mondayLastWeek) >= 0
  }

  /**
   * Gibt den Montag der angegebenen der Woche zurück.
   * <p>
   * @param day gibt die Woche vor
   * @return
   */
  def toMonday(day: LocalDate) = day.withDayOfWeek(1)

}
