@file:Suppress("ktlint:standard:max-line-length")

package lunchbox.domain.resolvers

import io.mockk.mockk
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider.BOULEVARD_IMBISS
import lunchbox.util.date.DateValidator
import lunchbox.util.html.HtmlParser
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldHaveSize
import org.junit.jupiter.api.Test
import java.time.LocalDate

class LunchResolverBoulevardImbissTest {

  private val htmlParser = HtmlParser(mockk())
  private fun resolver() = LunchResolverBoulevardImbiss(DateValidator.alwaysValid(), htmlParser)
  private val providerId = BOULEVARD_IMBISS.id

  @Test
  fun `resolve offers for week of 2023-02-13`() {
    val url = javaClass.getResource("/menus/boulevard_imbiss/2023-02-13.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 26

    var week = weekOfToday()
    offers.filter { it.day == week.monday && it.tags.isEmpty() } shouldHaveSize 5
    offers.filter { it.day == week.tuesday && it.tags.isEmpty() } shouldHaveSize 5
    offers.filter { it.day == week.wednesday && it.tags.isEmpty() } shouldHaveSize 5
    offers.filter { it.day == week.thursday && it.tags.isEmpty() } shouldHaveSize 5
    offers.filter { it.day == week.friday && it.tags.isEmpty() } shouldHaveSize 5

    offers shouldContain LunchOffer(
      0,
      "Bratwurst, Stampfkartoffeln",
      "mit Letscho oder Sauerkraut",
      LocalDate.now(),
      euro("5.00"),
      setOf("Tagesangebot"),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Nudeln",
      "mit Gulasch",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Nudeln",
      "mit Jägerschnitzel",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Erbsensuppe",
      "mit Bockwurst",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Schnitzel",
      "mit Kartoffeln",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gulasch",
      "mit Rotkohl, Kartoffeln",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
  }

  @Test
  fun `resolve offers for week of 2023-02-13 ohne Tagesangebot`() {
    val url = javaClass.getResource("/menus/boulevard_imbiss/2023-02-18.html")

    val offers = resolver().resolve(url)

    offers shouldHaveSize 25

    var week = weekOfToday()
    offers.filter { it.day == week.monday } shouldHaveSize 5
    offers.filter { it.day == week.tuesday } shouldHaveSize 5
    offers.filter { it.day == week.wednesday } shouldHaveSize 5
    offers.filter { it.day == week.thursday } shouldHaveSize 5
    offers.filter { it.day == week.friday } shouldHaveSize 5
    offers shouldContain LunchOffer(
      0,
      "Nudeln",
      "mit Gulasch",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Nudeln",
      "mit Jägerschnitzel",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Erbsensuppe",
      "mit Bockwurst",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Schnitzel",
      "mit Kartoffeln",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
    offers shouldContain LunchOffer(
      0,
      "Gulasch",
      "mit Rotkohl, Kartoffeln",
      week.monday,
      euro("5.00"),
      emptySet(),
      providerId,
    )
  }
}
