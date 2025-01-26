package lunchbox.util.html

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.util.retry.Retry.backoff
import java.net.URL
import java.time.Duration

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
  override fun render(url: URL): String =
    WebClient
      .create("$rendertronUrl/render/$url")
      .get()
      .retrieve()
      .bodyToMono<String>()
      .retryWhen(backoff(5, Duration.ofSeconds(5)))
      .block() ?: ""
}

const val RENDERTRON_PORT = 3005
