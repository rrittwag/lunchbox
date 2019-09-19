package lunchbox.util.html

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
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
  @Value("\${external.rendertron.server-url:http://rendertron:$RENDERTRON_SERVER_PORT}")
  val rendertronServerUrl: String
) : HtmlRenderer {

  override fun render(url: URL): String =
    WebClient.create("$rendertronServerUrl/render/$url")
      .get()
      .retrieve()
      .bodyToMono<String>()
      .retryBackoff(5, Duration.ofSeconds(5), Duration.ofSeconds(60))
      .block() ?: ""
}

const val RENDERTRON_SERVER_PORT = 3005
