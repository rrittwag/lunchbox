package lunchbox.util.facebook

import java.time.Duration
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

/**
 * Ruft eine Resource der Facebook GraphApi ab.
 */
@Component
class FacebookGraphApiImpl(
  @Value("\${external.facebook.appId:}") val appId: String,
  @Value("\${external.facebook.appSecret:}") val appSecret: String
) : FacebookGraphApi {

  override fun <T : GraphApiResource> query(url: String, clazz: Class<T>): T? {
    val resource = url.replaceFirst(Regex("^/"), "")
    val accessToken = "access_token=$appId|$appSecret"

    return WebClient.create("https://graph.facebook.com/v2.10/$resource?$accessToken")
      .get()
      .retrieve()
      .bodyToMono(clazz)
      .retryBackoff(5, Duration.ofSeconds(5), Duration.ofSeconds(60))
      .block()
  }
}
