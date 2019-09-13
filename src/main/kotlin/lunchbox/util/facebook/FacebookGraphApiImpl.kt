package lunchbox.util.facebook

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import java.time.Duration

@Component
class FacebookGraphApiImpl(
  val mapper: ObjectMapper
) : FacebookGraphApi {

  @Value("\${credentials.facebook.appId:}")
  lateinit var appId: String

  @Value("\${credentials.facebook.appSecret:}")
  lateinit var appSecret: String

  override fun <T : GraphApiResource> query(url: String, clazz: Class<T>): T? {
    val resource = url.replace(Regex("^/"), "")
    val accessToken = "access_token=$appId|$appSecret"

    val json = WebClient.create("https://graph.facebook.com/v2.10/$resource?$accessToken")
      .get()
      .retrieve()
      .bodyToMono<String>()
      .block(Duration.ofSeconds(60)) ?: return null

    return fromJson(json, clazz)
  }

  fun <T : GraphApiResource> fromJson(json: String, clazz: Class<T>): T? {
    return mapper.readValue(json, clazz)
  }
}
