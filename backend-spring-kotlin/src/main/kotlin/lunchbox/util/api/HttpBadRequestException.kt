package lunchbox.util.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception erzeugt HTTP-Antwort mit Fehlercode 400
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
class HttpBadRequestException(
  message: String,
) : RuntimeException(message)
