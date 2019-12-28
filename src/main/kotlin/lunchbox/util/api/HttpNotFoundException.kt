package lunchbox.util.api

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception erzeugt HTTP-Antwort mit Fehlercode 404
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class HttpNotFoundException(message: String) : RuntimeException(message)
