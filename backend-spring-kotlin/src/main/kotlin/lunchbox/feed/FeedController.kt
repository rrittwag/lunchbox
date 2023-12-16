package lunchbox.feed

import com.rometools.rome.feed.atom.Content
import com.rometools.rome.feed.atom.Entry
import com.rometools.rome.feed.atom.Feed
import com.rometools.rome.feed.atom.Link
import com.rometools.rome.feed.atom.Person
import jakarta.servlet.http.HttpServletRequest
import lunchbox.domain.models.LunchLocation
import lunchbox.domain.models.LunchOffer
import lunchbox.domain.models.LunchProvider
import lunchbox.repository.LunchOfferRepository
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

const val URL_FEED = "/feed"

/**
 * Stellt einen Atom-Feed mit den Mittagsangeboten bereit.
 */
@RestController
class FeedController(
  val repo: LunchOfferRepository,
) {
  @GetMapping(URL_FEED)
  fun feed(
    @RequestParam location: LunchLocation,
    request: HttpServletRequest,
  ): Feed =
    // Spring automatically converts Rome feeds
    // -> https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/converter/feed/AtomFeedHttpMessageConverter.html
    createFeed(location, resolveRequestUrl(request))

  fun createFeed(
    location: LunchLocation,
    feedLink: String,
  ): Feed {
    val providerIDs = LunchProvider.entries.filter { it.location == location }.map { it.id }
    val offersForProviders = repo.findAll().filter { providerIDs.contains(it.provider) }
    val offersTilToday = offersForProviders.filter { it.day <= LocalDate.now() }

    return createFeed(location, feedLink, offersTilToday)
  }

  private fun createFeed(
    location: LunchLocation,
    feedLink: String,
    offers: List<LunchOffer>,
  ): Feed {
    val offersByDay = offers.groupBy { it.day }.toSortedMap(reverseOrder())

    val author = Person().apply { name = "Lunchbox" }
    val selfLink =
      Link().apply {
        rel = "self"
        href = feedLink
      }
    val feed =
      Feed("atom_1.0").apply {
        id = "urn:uuid:8bee5ffa-ca9b-44b4-979b-058e32d3a157"
        title = "Mittagstisch ${location.label}"
        updated = toDateAtNoon(LocalDate.now())
        authors = listOf(author)
        alternateLinks = listOf(selfLink)
      }

    for ((day, offersForDay) in offersByDay) {
      val entry =
        Entry().apply {
          id = "urn:date:$day"
          title = toStringWithWeekday(day)
          authors = feed.authors
          published = toDateAtNoon(day)
          updated = toDateAtNoon(day)
          contents =
            listOf(
              Content().apply {
                type = Content.HTML
                value = createHtmlLunchday(offersForDay)
              },
            )
        }
      feed.entries.add(entry)
    }

    return feed
  }

  private fun createHtmlLunchday(offersForDay: List<LunchOffer>): String {
    return "<div>${createHtmlForProviders(offersForDay)}</div>"
  }

  private fun createHtmlForProviders(offers: List<LunchOffer>): String {
    var result = ""
    for ((providerId, provOffers) in offers.groupBy { it.provider }) {
      val provider = LunchProvider.entries.find { it.id == providerId } ?: continue
      result +=
        """
        |<table style="border:0px;">
        |  <tr style="padding-bottom: 1.5em;">
        |    <th>${provider.label}</th>
        |    <th></th>
        |  </tr>
        |  ${createHtmlOffers(provOffers)}
        |</table>
        """
    }
    return result.trimMargin()
  }

  private fun createHtmlOffers(providerOffers: List<LunchOffer>): String {
    var result = ""
    for (offer in providerOffers) {
      val price =
        if (offer.price != null) {
          "%d,%02d €".format(offer.price.amountMajorInt, offer.price.minorPart)
        } else {
          ""
        }

      result +=
        """
        |<tr style="padding-bottom: 1.5em;">
        |  <td>${offer.name}</td>
        |  <td style="vertical-align: top;">
        |    <span style="white-space: nowrap;">$price</span>
        |  </td>
        |</tr>
        """
    }
    return result.trimMargin()
  }

  fun resolveRequestUrl(request: HttpServletRequest): String {
    val requestURL = request.requestURL.toString()

    // Statt HTTPS liefert requestURL HTTP ... WTF?
    val headers = request.getHeaders("X-Forwarded-Proto")
    if (headers != null && headers.hasMoreElements()) {
      val (_, postScheme) = requestURL.split("://")
      return "${headers.nextElement()}://$postScheme?${request.queryString}"
    }
    return "$requestURL?${request.queryString}"
  }

  private fun toDateAtNoon(date: LocalDate): Date {
    val timeZoneBerlin = ZoneId.of("Europe/Berlin")
    val dateTime = date.atStartOfDay(timeZoneBerlin).plusHours(12)
    return Date(dateTime.toInstant().toEpochMilli())
  }

  private fun toStringWithWeekday(date: LocalDate): String =
    DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.GERMAN).format(date)
}

@Component
class LunchLocationConverter : Converter<String, LunchLocation> {
  override fun convert(source: String): LunchLocation? = LunchLocation.entries.find { it.label == source }
}
