package infrastructure

import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object OcrClientMain extends App {
  val dateString = "feldkueche/feldkueche_2016-10-31"

  val ocrClient = new DefaultOcrClient

  // provide jpgs via "docker run -d -p 80:80 -v {...}/lunchbox/server_play_akka_scala/test/resources/mittagsplaene/:/usr/share/nginx/html --name "docker_nginx" nginx"
  val ocrTextFuture = ocrClient.doOCR(new URL(s"http://192.168.178.22/$dateString.jpg"))
  ocrTextFuture.onComplete {
    case Success(ocrText) =>
      Files.write(Paths.get(s"test/resources/mittagsplaene/${dateString}_ocr.txt"), ocrText.getBytes(StandardCharsets.UTF_8))
      println("done")
      System.exit(0)

    case Failure(e) =>
      e.printStackTrace()
      System.exit(1)
  }
}
