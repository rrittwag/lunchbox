package info.rori.lunchbox.server.akka.scala.external

import java.net.URL

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
object OcrClient {

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

  private def downloadImage(imageUrl: URL): dispatch.Future[Response] = Http(url(imageUrl.toString))

  private def uploadImageToNewocr(imageGetResp: Res, imageFilename: String): Future[String] = {
    val request = url("http://api.newocr.com/v1/upload").POST
      .addBodyPart(new ByteArrayPart("file", imageGetResp.getResponseBodyAsBytes, imageGetResp.getContentType, null, imageFilename))
      .addQueryParameter("key", NewocrApiKey)

    Http(request OK as.String).flatMap { responseString =>
      parseFileIdOpt(responseString) match {
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

    Http(request OK as.String).map { responseString =>
      parseOcrTextOpt(responseString).getOrElse("")
    }
  }

  private def parseFileIdOpt(jsonString: String): Option[String] =
    (Json.parse(jsonString) \ "data" \ "file_id").asOpt[String]

  private[external] def parseOcrTextOpt(jsonString: String): Option[String] =
    (Json.parse(jsonString) \ "data" \ "text").asOpt[String]

  private lazy val NewocrApiKey = ConfigFactory.load().getString("external.newocr.apikey")
}
