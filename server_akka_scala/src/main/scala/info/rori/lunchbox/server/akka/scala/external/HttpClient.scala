package info.rori.lunchbox.server.akka.scala.external

import dispatch.retry

import dispatch._, Defaults._
import grizzled.slf4j.Logging
import scala.concurrent.duration._

trait HttpClient extends Logging {

  /**
   * Führt einen HTTP-Request aus. Tritt eine Exception auf, z.B. durch eine gestörte Netzverbindung oder da z.B.
   * beim Aufruf <code>Http(request OK as.String)</code> ein Status-Code!=200 oder binäre Daten zurückgegeben werden,
   * so wird der Aufruf via Backoff-Strategie wiederholt.
   * <p>
   * @param runRequest Führt den Request aus.
   * @tparam T Ergebnis-Typ der HTTP-Anfrage.
   * @return Ergebnis, als Future.
   */
  def runWithRetry[T](runRequest: () => Future[T]): Future[T] =
    runWithUserDefinedRetry(() => runRequest().either)

  /**
   * Führt einen HTTP-Request aus. Bei Fehlschlag wird der Request via Backoff-Strategie wiederholt.
   * Der Nutzer legt über den Parameter <code>runRequest</code> die Bedingungen für das Wiederholen fest.
   * <p>
   * @param runRequest Führt den Request aus. Der Rückgabewerte bedeutet: bei Left(Throwable) ist der Request
   *                   fehlgeschlagen und wird wiederholt, bei Right(T) ist der Request erfolgreich.
   *                   So kann der Aufrufer z.B. bestimmte Status-Codes als Erfolg oder Misserfolg deuten.
   *                   Beispiel siehe <a href="http://www.bimeanalytics.com/engineering-blog/retrying-http-request-in-scala/">hier</href>.
   * @tparam T Ergebnis-Typ der HTTP-Anfrage.
   * @return Ergebnis, als Future.
   */
  def runWithUserDefinedRetry[T](runRequest: () => Future[Either[Throwable, T]]): Future[T] =
    retry.Backoff(max = 4, delay = 5.seconds, base = 2)(runRequest).flatMap(handleResponse)

  private def handleResponse[O](result: Either[Throwable, O]): Future[O] = result match {
    case Left(exc) =>
      logger.error("HTTP request failed after 4 retries", exc)
      Future.failed(exc)
    case Right(response) => Future(response)
  }
}
