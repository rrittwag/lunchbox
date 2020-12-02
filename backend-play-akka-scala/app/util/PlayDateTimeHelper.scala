package util

import java.time.format.{DateTimeFormatter, DateTimeFormatterBuilder, ResolverStyle}
import java.time.temporal.ChronoField._
import java.time.{Instant, LocalDate, OffsetDateTime, ZoneId}

import play.api.libs.json.{JsString, JsValue, Reads, Writes}
import play.api.mvc.QueryStringBindable

import scala.util.Try

/**
 * Helper functions for converting Java 8's DateTime in Play.
 * User: robbel
 * Date: 04.04.16
 */
object PlayDateTimeHelper {

  // ---- OffsetDateTime ----

  private val dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME

  // RFC3339 is stricter than ISO-8601, seconds and milliseconds are mandatory
  private val rfc3339Format = new DateTimeFormatterBuilder()
    .append(DateTimeFormatter.ISO_LOCAL_DATE)
    .appendLiteral('T')
    .appendValue(HOUR_OF_DAY, 2)
    .appendLiteral(':')
    .appendValue(MINUTE_OF_HOUR, 2)
    .appendLiteral(':')
    .appendValue(SECOND_OF_MINUTE, 2)
    .appendFraction(MILLI_OF_SECOND, 3, 3, true)
    .appendOffsetId
    .toFormatter
    .withResolverStyle(ResolverStyle.STRICT)

  /**
   * Reads DateTime from Play JSON.
   */
  implicit val dateTimeReads = Reads[OffsetDateTime](js =>
    js.validate[String].map[OffsetDateTime](dtString =>
      OffsetDateTime.parse(dtString, dateTimeFormat)))

  /**
   * Writes DateTime to Play JSON.
   */
  implicit val dateTimeWrites: Writes[OffsetDateTime] = new Writes[OffsetDateTime] {
    def writes(d: OffsetDateTime): JsValue = JsString(d.toStringRFC)
  }

  /**
   * Enables sorting of OffsetDateTime.
   */
  implicit def dateTimeOrdering: Ordering[OffsetDateTime] = Ordering.ordered[OffsetDateTime]

  /**
   * Converts milliseconds since epoch to a Java OffsetDateTime. Offset is Z.
   *
   * @param epochMilliseconds milliseconds since epoch.
   * @return
   */
  def toOffsetDateTime(epochMilliseconds: Long): OffsetDateTime =
    OffsetDateTime.ofInstant(Instant.ofEpochMilli(epochMilliseconds), ZoneId.of("Z"))

  implicit class RichOffsetDateTime(val underlying: OffsetDateTime) {
    /**
     * toString for the stricter RFC3339 (Seconds and milliseconds will be output, although zero).
     *
     * @return
     */
    def toStringRFC: String = underlying.format(rfc3339Format)

    /**
     * Converts a Java OffsetDateTime to milliseconds since epoch.
     *
     * @return
     */
    def toEpochMilli: Long = underlying.toInstant.toEpochMilli
  }

  // ---- LocalDate ----

  private val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE

  /**
   * Reads LocalDate from Play JSON.
   */
  implicit val localDateReads = Reads[LocalDate](js =>
    js.validate[String].map[LocalDate](dateString =>
      LocalDate.parse(dateString, dateFormat)))

  /**
   * Writes LocalDate to Play JSON.
   */
  implicit val localDateWrites: Writes[LocalDate] = new Writes[LocalDate] {
    def writes(d: LocalDate): JsValue = JsString(d.toString)
  }

  /**
   * Enables sorting of LocalDate.
   */
  implicit object LocalDateOrdering extends Ordering[LocalDate] {
    override def compare(x: LocalDate, y: LocalDate): Int = x compareTo y
  }

  /**
   * Enable compare methods for LocalDate.
   *
   * @param underlying the LocalDate.
   */
  implicit class RichLocalDate(val underlying: LocalDate) extends Ordered[LocalDate] {
    override def compare(that: LocalDate): Int = underlying compareTo that
  }

  /**
   * Enables usage of LocalDate in routes file.
   * <p>
   * The QueryStringBindable converts the query parameters to a LocalDate. If conversion fails, returns 400 Bad Request.
   * <p>
   *
   * @return
   */
  implicit def bindableLocalDate = new QueryStringBindable[LocalDate] {
    override def bind(key: String, params: Map[String, Seq[String]]): Option[Either[String, LocalDate]] =
      params.get(key).map {
        case Seq(value) => bind(key, value)
        case _ => Left(s"query parameter $key may be used only once")
      }

    private def bind(key: String, value: String): Either[String, LocalDate] =
      Try(LocalDate.parse(value, dateFormat)).toOption.toRight(s"$value is no valid date")

    override def unbind(key: String, date: LocalDate): String = s"$key=${date.toString}"
  }

}
