package lunchbox.util.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class HttpNotFoundException(message: String) : RuntimeException(message)
