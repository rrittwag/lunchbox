package lunchbox.util.ocr

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.net.URL
import java.time.Duration

/**
 * Spricht einen WebService an, der Texterkennung auf dem übergebenen Bild ausführt (OCR).
 */
@Component
class OcrClient {

  fun doOCR(
    imageUrl: URL,
    ocrServerUrl: String = "http://openocr:$OCR_SERVER_PORT"
  ): String {

    val requestBody = mapOf(
      "img_url" to imageUrl,
      "engine" to "tesseract",
      "engine_args" to mapOf("lang" to "deu")
    )

    return WebClient.create("$ocrServerUrl/ocr")
      .post()
      .body(BodyInserters.fromObject(requestBody))
      .retrieve()
      .bodyToMono<String>()
      .block(Duration.ofSeconds(60)) ?: ""
  }
}

const val OCR_SERVER_PORT = 9292
