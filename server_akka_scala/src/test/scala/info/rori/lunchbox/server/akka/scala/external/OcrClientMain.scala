package info.rori.lunchbox.server.akka.scala.external

import java.net.URL
import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

import scala.concurrent.ExecutionContext.Implicits.global

object OcrClientMain extends App {
  val ocrTextFuture = OcrClient.doOCR(new URL("https://scontent-ams2-1.xx.fbcdn.net/hphotos-xtp1/t31.0-8/11754460_731479140296273_8817556174192653336_o.jpg"))
  for (ocrText <- ocrTextFuture) {
    Files.write(Paths.get("src/test/resources/mittagsplaene/gesundheitszentrum/gesundheitszentrum_2015-07-27_ocr.txt"), ocrText.getBytes(StandardCharsets.UTF_8))
    println("done")
  }
}
