package lunchbox.util.url

import java.net.URI
import java.net.URL

object UrlUtil {
  /**
   * URL instanziieren.
   * @param urlStr String der URL.
   */
  fun url(urlStr: String): URL = URI(urlStr).toURL()
}
