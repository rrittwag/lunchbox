package lunchbox.util.facebook

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.resilience.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

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
  @Retryable(maxRetries = 5, delay = 5000, multiplier = 2.0)
  override fun <T : GraphApiResource> query(
    url: String,
    clazz: Class<T>,
  ): T? {
    val resource = url.replaceFirst(Regex("^/"), "")
    val accessToken = "access_token=${config.appId}|{${config.appSecret}}"

    return RestClient
      .create("https://graph.facebook.com/v2.10/$resource?$accessToken")
      .get()
      .retrieve()
      .body(clazz)
  }
}
