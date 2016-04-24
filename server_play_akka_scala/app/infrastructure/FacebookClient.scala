package infrastructure

import com.typesafe.config.ConfigFactory
import dispatch.Defaults._
import dispatch._

import scala.concurrent.Future

/**
 * Daten von Facebook abrufen.
 * <p>
 * Dokumentation: Daten abrufen via <a href="https://developers.facebook.com/docs/graph-api/using-graph-api/v2.3">Graph API</a>
 * <p>
 * Dokumentation: Authentisierung & Authorisierung via <a href="https://developers.facebook.com/docs/apps/register">App-ID</a>
 * & <a href="https://developers.facebook.com/docs/facebook-login/access-tokens#apptokens">Access Tokens</a>.
 * <p>
 */
object FacebookClient extends HttpClient {

  def query(graphApiUrl: String): Future[String] = {
    val config = ConfigFactory.load()
    val appId = config.getString("external.facebook.appId")
    val appSecret = config.getString("external.facebook.appSecret")

    val request = url(s"https://graph.facebook.com/v2.3/${graphApiUrl.replaceFirst("^/", "")}").secure
      .addQueryParameter("access_token", s"$appId|$appSecret")

    val requestFunc = () => Http(request OK as.String)

    runWithRetry(requestFunc)
  }
}
