package info.rori.lunchbox.server.akka.scala.external

import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.concurrent.ExecutionContext.Implicits.global

object OcrClientMain extends App {
  val ocrTextFuture = OcrClient.doOCR(new URL("https://scontent-fra3-1.xx.fbcdn.net/hphotos-xft1/t31.0-8/11251272_736557099788477_3687609882283739871_o.jpg"))
  for (ocrText <- ocrTextFuture) {
    Files.write(Paths.get("src/test/resources/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-08-10_ocr.txt"), ocrText.getBytes(StandardCharsets.UTF_8))
    println("done")
  }
}
