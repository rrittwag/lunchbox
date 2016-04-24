package util

import org.joda.time.{LocalDate, DateTime}
import org.joda.time.format.ISODateTimeFormat
import play.api.libs.json.{JsString, JsValue, Reads, Writes}

import scala.util.{Success, Try}

/**
  * Helper functions for converting Joda's DateTime in Play.
  * User: robbel
  * Date: 04.04.16
  */
object PlayDateTimeHelper {

  val dateFormat = ISODateTimeFormat.date()
  val dateTimeFormat = ISODateTimeFormat.dateTime().withZoneUTC() // milliseconds are mandatory - alternative: dateTimeNoMillis

  /**
    * Reads Joda's DateTime from Play JSON.
    */
  implicit val jodaDateTimeReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, dateTimeFormat)
    )
  )

  /**
    * Writes Joda's DateTime to Play JSON.
    */
  implicit val jodaDateTimeWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString(dateTimeFormat))
  }


  /**
    * Reads Joda's LocalDate from Play JSON.
    */
  implicit val jodaLocalDateReads = Reads[LocalDate](js =>
    js.validate[String].map[LocalDate](dateString =>
      LocalDate.parse(dateString, dateFormat)
    )
  )

  /**
    * Writes Joda's LocalDate to Play JSON.
    */
  implicit val jodaLocalDateWrites: Writes[LocalDate] = new Writes[LocalDate] {
    def writes(d: LocalDate): JsValue = JsString(d.toString(dateFormat))
  }

  /**
    * Parses a Joda LocalDate from optional string.
    *
    * @param optDateString string to parse
    * @return
    */
  def parseLocalDate(optDateString: Option[String]): Try[Option[LocalDate]] = optDateString match {
    case Some(dateString) => Try(Some(LocalDate.parse(dateString, dateFormat)))
    case None => Success(Option.empty[LocalDate])
  }

}
