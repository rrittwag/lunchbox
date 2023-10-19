package lunchbox.util.facebook

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.util.retry.Retry.backoff
import java.time.Duration

@ConfigurationProperties("external.facebook")
data class FacebookConfigProperties(
  val appId: String = "",
  val appSecret: String = "",
)

/**
 * Ruft eine Resource der Facebook GraphApi ab.
 */
@Component
class FacebookGraphApiImpl(
  val config: FacebookConfigProperties,
) : FacebookGraphApi {
  override fun <T : GraphApiResource> query(
    url: String,
    clazz: Class<T>,
  ): T? {
    val resource = url.replaceFirst(Regex("^/"), "")
    val accessToken = "access_token=${config.appId}|{${config.appSecret}}"

    return WebClient.create("https://graph.facebook.com/v2.10/$resource?$accessToken")
      .get()
      .retrieve()
      .bodyToMono(clazz)
      .retryWhen(backoff(5, Duration.ofSeconds(5)))
      .block()
  }
}
