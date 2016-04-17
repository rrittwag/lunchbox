package info.rori.lunchbox.server.akka.scala.external

import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object OcrClientMain extends App {
  val dateString = "gesundheitszentrum_2016-04-04"

  // provide jpgs via "docker run -d -p 80:80 -v {...}/lunchbox/server_akka_scala/src/test/resources/mittagsplaene/gesundheitszentrum/:/usr/share/nginx/html nginx"
  val ocrTextFuture = OcrClient.doOCR(new URL(s"http://192.168.99.100/$dateString.jpg"))
  ocrTextFuture.onComplete {
    case Success(ocrText) =>
      Files.write(Paths.get(s"src/test/resources/mittagsplaene/gesundheitszentrum/${dateString}_ocr.txt"), ocrText.getBytes(StandardCharsets.UTF_8))
      println("done")
      System.exit(0)

    case Failure(e) =>
      e.printStackTrace()
      System.exit(1)
  }
}
