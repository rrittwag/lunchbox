package lunchbox.util.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component
import java.net.URL

/**
 * Liest HTML-Dokumente ein.
 */
@Component
class HtmlParser(
  val renderer: HtmlRenderer,
) {
  fun parse(url: URL): Document {
    return parse(url, "utf-8")
  }

  fun parse(
    url: URL,
    encoding: String,
  ): Document {
    return Jsoup.parse(url.openStream(), encoding, "${url.protocol}:${url.authority}")
  }

  fun renderAndParse(url: URL): Document {
    return Jsoup.parse(renderer.render(url), "${url.protocol}:${url.authority}")
  }
}
