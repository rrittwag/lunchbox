package lunchbox.util.facebook

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import java.time.Duration

@Component
class FacebookGraphApiImpl : FacebookGraphApi {

  @Value("\${credentials.facebook.appId:}")
  lateinit var appId: String

  @Value("\${credentials.facebook.appSecret:}")
  lateinit var appSecret: String

  override fun <T : GraphApiResource> query(url: String, clazz: Class<T>): T? {
    val resource = url.replaceFirst(Regex("^/"), "")
    val accessToken = "access_token=$appId|$appSecret"

    return WebClient.create("https://graph.facebook.com/v2.10/$resource?$accessToken")
      .get()
      .retrieve()
      .bodyToMono(clazz)
      .block(Duration.ofSeconds(60)) ?: return null
  }
}
