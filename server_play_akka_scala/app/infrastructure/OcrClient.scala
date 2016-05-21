package infrastructure

import java.net.URL
import javax.inject.Singleton

import dispatch.Defaults._
import dispatch._
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Service für das Auslesen von Text aus einem Bild (OCR).
 */
trait OcrClient {
  def doOCR(imageUrl: URL): Future[String]
}

@Singleton
class DefaultOcrClient(implicit ec: ExecutionContext) extends OcrClient {

  def doOCR(imageUrl: URL): Future[String] = {
    /*
      Tipp: Die HTTP-Requests lassen sich via Wireshark mitsamt aller Header loggen:
      tshark -V -Y "http.request and http.host contains openocr" > tshark.log
     */
    val body = Json.obj(
      "img_url" -> imageUrl.toString,
      "engine" -> "tesseract",
      "engine_args" -> Json.obj("lang" -> "deu")
    )

    val req = url("http://openocr:20080/ocr").POST
      .setContentType("application/json", "utf-8")
      .setBody(body.toString)

    Http(req OK (response => response.getResponseBody))
    //    ws.url("http://openocr:20080/ocr")
    //      .post(body)
    //      .map(_.json.as[String])
  }

}
