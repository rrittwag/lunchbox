package infrastructure

import java.net.URL

import dispatch.Defaults._
import dispatch._
import play.api.libs.json._

import scala.concurrent.Future

/**
  * Service für das Auslesen von Text aus einem Bild (OCR).
  */
object OcrClient extends HttpClient {

  def doOCR(imageUrl: URL): Future[String] = {
    /*
      Tipp: Die HTTP-Requests lassen sich via Wireshark mitsamt aller Header loggen:
      tshark -V -Y "http.request and http.host contains openocr" > tshark.log
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
