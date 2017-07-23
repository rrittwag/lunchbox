package controllers

import javax.inject.Inject

import akka.stream.Materializer
import play.api.Logger
import play.api.http.HttpFilters
import play.api.mvc.{Filter, RequestHeader, Result}
import play.filters.gzip.GzipFilter

import scala.concurrent.{ExecutionContext, Future}

/**
 * Log requests and GZIP responses.
 *
 * @param gzip GZIP filter
 * @param log  log filter
 */
class Filters @Inject() (
    gzip: GzipFilter,
    log: RequestLoggingFilter) extends HttpFilters {

  val filters = Seq(gzip, log)
}

/**
 * Log every request and its execution time.
 *
 * @param mat Materializer!?
 * @param ec  ExecutionContext!?
 */
class RequestLoggingFilter @Inject() (
    implicit
    val mat: Materializer,
    ec: ExecutionContext) extends Filter {

  val requestLogger = Logger("request")
  val UserAgentHeader = "User-Agent"

  def apply(nextFilter: RequestHeader => Future[Result])(requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>
      val endTime = System.currentTimeMillis
      val requestTime = endTime - startTime

      requestLogger.info(s"${requestHeader.remoteAddress} - ${if (requestHeader.secure) "SSL - " else ""}${requestHeader.headers.get(UserAgentHeader).getOrElse("")} - ${requestHeader.method} ${requestHeader.uri} - took ${requestTime}ms and returned ${result.header.status}")

      result.withHeaders("Request-Time" -> requestTime.toString)
    }
  }
}