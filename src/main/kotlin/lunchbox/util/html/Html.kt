package lunchbox.util.html

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.URL

class Html {
  companion object {
    fun load(url: URL): Document {
      return load(url, "utf-8")
    }

    fun load(url: URL, encoding: String): Document {
      return Jsoup.parse(url.openStream(), encoding, "${url.protocol}:${url.authority}")
    }
  }
}
