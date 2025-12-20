package lunchbox.util.html

import org.springframework.beans.factory.annotation.Value
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.net.URL

/**
 * Rendert JavaScript-lastige Webseiten/WebApps in statisches HTML.
 */
interface HtmlRenderer {
  fun render(url: URL): String
}

@Component
class HtmlRendererImpl(
  @Value("\${external.rendertron.url:http://rendertron:$RENDERTRON_PORT}")
  val rendertronUrl: String,
) : HtmlRenderer {
  @Retryable(maxRetries = 5, delay = 5000, multiplier = 2.0)
  override fun render(url: URL): String =
    RestClient
      .create("$rendertronUrl/render/$url")
      .get()
      .retrieve()
      .body<String>() ?: ""
}

const val RENDERTRON_PORT = 3005
