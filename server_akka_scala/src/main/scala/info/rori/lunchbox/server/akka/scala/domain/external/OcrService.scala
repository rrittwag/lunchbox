package info.rori.lunchbox.server.akka.scala.domain.external

import java.net.URL

import com.ning.http.client.Response
import com.ning.http.client.multipart.ByteArrayPart
import com.typesafe.config.ConfigFactory
import dispatch.Defaults._
import dispatch._

import scala.concurrent.Future
import scala.util.matching.Regex

/**
 * Service für das Auslesen von Text aus einem Bild (OCR).
 */
object OcrService {

  implicit class RegexContext(sc: StringContext) {
    def r = new Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def doOCR(imageUrl: URL): Future[String] = {
    val imageFilename = imageUrl.getPath.substring(imageUrl.getPath.lastIndexOf('/') + 1, imageUrl.getPath.length)

    downloadImage(imageUrl)
      .flatMap(downloadImageResponse => uploadImageToNewocr(downloadImageResponse, imageFilename))
      .flatMap(fileId => startOcrOnNewocr(fileId))
  }

  private def downloadImage(imageUrl: URL): dispatch.Future[Response] = Http(url(imageUrl.toString))

  private def uploadImageToNewocr(imageGetResp: Res, imageFilename: String): Future[String] = {
    val newocrUploadReq = url("http://api.newocr.com/v1/upload").POST
      .addBodyPart(new ByteArrayPart("file", imageGetResp.getResponseBodyAsBytes, imageGetResp.getContentType, null, imageFilename))
      .addQueryParameter("key", NewocrApiKey)

    Http(newocrUploadReq OK as.String).flatMap { newocrUploadRespString =>
      parseFileIdOpt(newocrUploadRespString) match {
        case Some(fileId) => Future(fileId)
        case None => Future.failed(new Exception) // TODO: FileNotUploadedException ???
      }
    }
  }

  private def startOcrOnNewocr(fileId: String): Future[String] = {
    val newocrOcrReq = url("http://api.newocr.com/v1/ocr")
      .addQueryParameter("key", NewocrApiKey)
      .addQueryParameter("file_id", fileId)
      .addQueryParameter("lang", "deu")

    Http(newocrOcrReq OK as.String).map { newocrOcrRespString =>
      parseOcrTextOpt(newocrOcrRespString).getOrElse("")
    }
  }

  private def parseFileIdOpt(jsonString: String): Option[String] = jsonString match {
    case r""".*\"file_id\":\"([a-f\d]+)$fileId\".*""" => Option(fileId)
    case _ => None
  }

  private def parseOcrTextOpt(jsonString: String): Option[String] = jsonString match {
    case r""".*\"text\":\"(.*)$ocrText\",\"progress\".*""" => Option(ocrText)
    case _ => None
  }

  private lazy val NewocrApiKey = ConfigFactory.load().getString("external.newocr.apikey")
}
