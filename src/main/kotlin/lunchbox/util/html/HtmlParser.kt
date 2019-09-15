package lunchbox.util.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

/**
 * Liest HTML-Dokumente ein.
 */
object HtmlParser {
  fun parse(url: URL): Document {
    return parse(url, "utf-8")
  }

  fun parse(url: URL, encoding: String): Document {
    return Jsoup.parse(url.openStream(), encoding, "${url.protocol}:${url.authority}")
  }

  /**
   * Rendert JavaScript-lastige Webseiten/WebApps in statisches HTML, bevor es sie parst.
   */
  fun renderAndParse(url: URL): Document =
    if (url.protocol == "file")
      // TODO: Unit-Tests brauchen die Daten nicht rendern. Bessere Lösung?
      parse(url)
    else
      Jsoup.parse(
        URL("http://rendertron:3005/render/$url").openStream(),
        "utf-8",
        "${url.protocol}:${url.authority}"
      )
}
