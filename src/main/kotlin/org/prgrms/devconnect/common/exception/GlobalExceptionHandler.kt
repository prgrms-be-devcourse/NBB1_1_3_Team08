package org.prgrms.devconnect.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

  private val log = LoggerFactory.getLogger(this::class.java)

  @ExceptionHandler(DevConnectException::class)
  fun handlerBadRequestException(e: DevConnectException): ResponseEntity<ExceptionResponse> {
    log.warn(e.message, e)

    return ResponseEntity
        .status(e.exceptionCode.code)
        .body<ExceptionResponse>(ExceptionResponse(e.exceptionCode.code, e.message!!))
  }
}