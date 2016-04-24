package external

import play.api.Logger

/**
  * User: robbel
  * Date: 24.04.16
  */
trait PlayLogging {
  lazy val logger = Logger(getClass)

  lazy val defaultLogger = Logger
}
