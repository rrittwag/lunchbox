package info.rori.lunchbox.server.akka.scala.external

import dispatch.retry

import dispatch._
import dispatch.Defaults._
import scala.concurrent.duration._
import scala.concurrent.Future

trait HttpClient {

  def runWithBackoff[T](runRequest: () => Future[Either[Throwable, Res]])(processResponse: Res => Future[T]): Future[T] =
    retry.Backoff(max = 4, delay = 5.seconds, base = 2)(runRequest).flatMap {
    case Left(error) =>
      error.printStackTrace() // TODO: Logging
      Future.failed(new Exception) // TODO: FileNotUploadedException ???
    case Right(response) => processResponse(response)
  }

}
