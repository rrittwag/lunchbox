package lunchbox.util.ocr

import org.springframework.beans.factory.annotation.Value
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

/**
 * Führt Texterkennung (OCR) auf dem übergebenen Bild via OpenOCR aus.
 * <p>
 * OpenOCR wird in der Lunchbox via Docker bereitgestellt.
 */
@Component
class OcrClient(
  @Value("\${external.ocr.url:http://ocr:$OCR_PORT}")
  val ocrUrl: String,
) {
  @Retryable(maxRetries = 5, delay = 5000, multiplier = 2.0)
  fun doOCR(imageUrl: URL): String {
    val requestBody =
      mapOf(
        "img_url" to imageUrl,
        "engine" to "tesseract",
        "engine_args" to mapOf("lang" to "deu"),
      )

    return RestClient
      .create("$ocrUrl/url")
      .post()
      .body(requestBody)
      .retrieve()
      .body<String>() ?: ""
  }
}

const val OCR_PORT = 9292
