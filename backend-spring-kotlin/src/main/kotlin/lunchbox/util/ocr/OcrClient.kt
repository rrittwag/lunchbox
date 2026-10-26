package lunchbox.util.ocr

import org.springframework.beans.factory.annotation.Value
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

enum class OcrOutputFormat(val format: String) {
  PLAIN_TEXT("text"), HTML_WITH_TEXT_AND_BOUNDING_BOXES("html")
}

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
  fun doOCR(imageUrl: URL, outputFormat: OcrOutputFormat = OcrOutputFormat.PLAIN_TEXT): String {
    val requestBody =
      mapOf(
        "img_url" to imageUrl,
        "format" to outputFormat.format,
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
