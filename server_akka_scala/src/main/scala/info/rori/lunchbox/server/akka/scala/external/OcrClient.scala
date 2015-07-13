package info.rori.lunchbox.server.akka.scala.external

import java.net.URL
import java.nio.charset.StandardCharsets

import com.ning.http.client.Response
import com.ning.http.client.multipart.ByteArrayPart
import com.typesafe.config.ConfigFactory
import dispatch.Defaults._
import dispatch._

import scala.concurrent.Future
import scala.util.matching.Regex

import play.api.libs.json._

/**
 * Service für das Auslesen von Text aus einem Bild (OCR).
 */
object OcrClient extends HttpClient {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def doOCR(imageUrl: URL): Future[String] = {
    val imageFilename = imageUrl.getPath.substring(imageUrl.getPath.lastIndexOf('/') + 1, imageUrl.getPath.length)

    /*
      Tipp: Die HTTP-Requests an newocr.com lassen sich via Wireshark mitsamt aller Header loggen:
      tshark -V -Y "http.request and http.host contains newocr" > tshark.log
     */
    downloadImage(imageUrl)
      .flatMap(image => uploadImageToNewocr(image, imageFilename))
      .flatMap(fileId => startOcrOnNewocr(fileId))
  }

  private def downloadImage(imageUrl: URL): dispatch.Future[Response] = {
    val request = url(imageUrl.toString)

    def runRequest() = Http(request).either

    runWithBackoff(runRequest) { response =>
      Future(response)
    }
  }

  private def uploadImageToNewocr(imageGetResp: Res, imageFilename: String): Future[String] = {
    val request = url("http://api.newocr.com/v1/upload").POST
      .addBodyPart(new ByteArrayPart("file", imageGetResp.getResponseBodyAsBytes, imageGetResp.getContentType, null, imageFilename))
      .addQueryParameter("key", NewocrApiKey)

    def runRequest() = Http(request).either

    runWithBackoff(runRequest) { response =>
      parseFileIdOpt(response.getResponseBody) match {
        case Some(fileId) => Future(fileId)
        case None => Future.failed(new Exception) // TODO: FileNotUploadedException ???
      }
    }
  }

  private def startOcrOnNewocr(fileId: String): Future[String] = {
    val request = url("http://api.newocr.com/v1/ocr")
      .addQueryParameter("key", NewocrApiKey)
      .addQueryParameter("file_id", fileId)
      .addQueryParameter("lang", "deu")

    def runRequest() = Http(request).either.map {
      case Left(error) => Left(error)
      case Right(response) =>
        // Der Upload ist nicht sofort sichtbar, der OCR-Aufruf liefert Status-Code 400. Daher: via Exception Backoff erzwingen
        if (response.getStatusCode == 400) Left(new Exception(s"status code ${response.getStatusCode}: ${response.getResponseBody}"))
        else Right(response)
    }

    runWithBackoff(runRequest) { response =>
      // Dispatch erkennt das Charset nicht, daher manuell in UTF-8 umwandeln
      val responseString = new String(response.getResponseBodyAsBytes, StandardCharsets.UTF_8)
      Future(parseOcrTextOpt(responseString).getOrElse(""))
    }
  }

  private def parseFileIdOpt(jsonString: String): Option[String] =
    (Json.parse(jsonString) \ "data" \ "file_id").asOpt[String]

  private[external] def parseOcrTextOpt(jsonString: String): Option[String] =
    (Json.parse(jsonString) \ "data" \ "text").asOpt[String]

  private lazy val NewocrApiKey = ConfigFactory.load().getString("external.newocr.apikey")
}
