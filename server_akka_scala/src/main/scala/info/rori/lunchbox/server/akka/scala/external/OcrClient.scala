package info.rori.lunchbox.server.akka.scala.external

import java.net.URL

import dispatch.Defaults._
import dispatch._
import play.api.libs.json._

import scala.concurrent.Future
import scala.util.matching.Regex

/**
  * Service für das Auslesen von Text aus einem Bild (OCR).
  */
object OcrClient extends HttpClient {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def doOCR(imageUrl: URL): Future[String] = {
    /*
      Tipp: Die HTTP-Requests an newocr.com lassen sich via Wireshark mitsamt aller Header loggen:
      tshark -V -Y "http.request and http.host contains newocr" > tshark.log
     */
    val body = Json.obj(
      "img_url" -> imageUrl.toString,
      "engine" -> "tesseract",
      "engine_args" -> Json.obj("lang" -> "deu"))

    val req = url("http://openocr:20080/ocr").POST
      .setContentType("application/json", "utf-8")
      .setBody(body.toString)

    Http(req OK (response => response.getResponseBody))
  }

}
