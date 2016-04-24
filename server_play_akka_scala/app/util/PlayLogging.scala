package util

import play.api.Logger

/**
  * Helper trait for Play logging.
  * User: robbel
  * Date: 24.04.16
  */
trait PlayLogging {
  lazy val logger = Logger(getClass)

  lazy val defaultLogger = Logger
}
