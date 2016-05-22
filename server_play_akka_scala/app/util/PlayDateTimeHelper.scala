package util

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDate, OffsetDateTime, ZoneId}

import play.api.libs.json.{JsString, JsValue, Reads, Writes}
import play.api.mvc.QueryStringBindable

import scala.util.{Success, Try}

/**
 * Helper functions for converting Java 8's DateTime in Play.
 * User: robbel
 * Date: 04.04.16
 */
object PlayDateTimeHelper {

  val dateFormat = DateTimeFormatter.ISO_LOCAL_DATE
  val dateTimeFormat = DateTimeFormatter.ISO_OFFSET_DATE_TIME

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
    def writes(d: OffsetDateTime): JsValue = JsString(d.toString)
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

  /**
   * Converts a Java OffsetDateTime to milliseconds since epoch.
   * @param dateTime a Java OffsetDateTime
   * @return
   */
  def toEpochMillis(dateTime: OffsetDateTime): Long = dateTime.toInstant.toEpochMilli

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
   * Parses a LocalDate from optional string.
   *
   * @param optDateString string to parse
   * @return
   */
  def parseLocalDate(optDateString: Option[String]): Try[Option[LocalDate]] = optDateString match {
    case Some(dateString) => Try(Some(LocalDate.parse(dateString, dateFormat)))
    case None => Success(Option.empty[LocalDate])
  }

  /**
   * Enables sorting of LocalDate.
   */
  implicit object LocalDateOrdering extends Ordering[LocalDate] {
    override def compare(x: LocalDate, y: LocalDate): Int = x compareTo y
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
