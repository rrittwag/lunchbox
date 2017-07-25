package infrastructure

import javax.inject.Singleton

import com.typesafe.config.ConfigFactory
import dispatch._

import scala.concurrent.{ExecutionContext, Future}

/**
 * Daten von Facebook abrufen.
 * <p>
 * Dokumentation: Daten abrufen via <a href="https://developers.facebook.com/docs/graph-api/using-graph-api/v2.3">Graph API</a>
 * <p>
 * Dokumentation: Authentisierung & Authorisierung via <a href="https://developers.facebook.com/docs/apps/register">App-ID</a>
 * & <a href="https://developers.facebook.com/docs/facebook-login/access-tokens#apptokens">Access Tokens</a>.
 * <p>
 */
trait FacebookClient {
  def query(graphApiUrl: String): Future[String]
}

@Singleton
class DefaultFacebookClient(implicit ec: ExecutionContext) extends FacebookClient with HttpClient {

  def query(graphApiUrl: String): Future[String] = {
    val config = ConfigFactory.load() // TODO: inject via "configuration: Configuration"
    val appId = config.getString("infrastructure.facebook.appId")
    val appSecret = config.getString("infrastructure.facebook.appSecret")

    val request = url(s"https://graph.facebook.com/v2.10/${graphApiUrl.replaceFirst("^/", "")}").secure
      .addQueryParameter("access_token", s"$appId|$appSecret")

    val requestFunc = () => Http.default(request OK as.String)

    runWithRetry(requestFunc)
    //    ws.url(s"https://graph.facebook.com/v2.3/${graphApiUrl.replaceFirst("^/", "")}")
    //      .withQueryString("access_token" -> s"$appId|$appSecret")
    //      .get()
    //      .map(_.json.as[String])
  }
}
