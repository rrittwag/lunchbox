package lunchbox.util.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

/**
 * Liest HTML-Dokumente ein.
 */
object Html {
  fun load(url: URL): Document {
    return load(url, "utf-8")
  }

  fun load(url: URL, encoding: String): Document {
    return Jsoup.parse(url.openStream(), encoding, "${url.protocol}:${url.authority}")
  }

  /**
   * Rendert JavaScript-lastige Webseiten/WebApps in statisches HTML, bevor es sie parst.
   */
  fun renderAndLoad(url: URL): Document =
    if (url.protocol == "file")
      // TODO: Unit-Tests brauchen die Daten nicht rendern. Bessere Lösung?
      load(url)
    else
      Jsoup.parse(
        URL("http://rendertron:3005/render/$url").openStream(),
        "utf-8",
        "${url.protocol}:${url.authority}"
      )
}
