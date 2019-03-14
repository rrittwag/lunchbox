package lunchbox.util.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class HttpBadRequestException(message: String) : RuntimeException(message)
