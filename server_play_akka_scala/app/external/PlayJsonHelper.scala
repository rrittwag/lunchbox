package external

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.{JsString, JsValue, Reads, Writes}

/**
  * Helper functions for Play JSON.
  * User: robbel
  * Date: 04.04.16
  */
object PlayJsonHelper {

  val dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

  /**
    * Reads DateTime from Play JSON.
    */
  implicit val jodaDateTimeReads = Reads[DateTime](js =>
    js.validate[String].map[DateTime](dtString =>
      DateTime.parse(dtString, DateTimeFormat.forPattern(dateFormat))
    )
  )

  /**
    * Writes DateTime to Play JSON.
    */
  implicit val jodaDateTimeWrites: Writes[DateTime] = new Writes[DateTime] {
    def writes(d: DateTime): JsValue = JsString(d.toString())
  }

}
