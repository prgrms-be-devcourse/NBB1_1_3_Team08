package org.prgrms.devconnect.common.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


@RestControllerAdvice
class GlobalExceptionHandler {

  private val log = LoggerFactory.getLogger(this::class.java)

  @ExceptionHandler(DevConnectException::class)
  fun handlerBadRequestException(e: DevConnectException): ResponseEntity<ExceptionResponse> {
    log.warn("[${e.javaClass.name}] error: ${e.message}", e)

    return ResponseEntity
      .status(e.exceptionCode.code)
      .body(ExceptionResponse(e.exceptionCode.code, e.message!!))
  }

  @ExceptionHandler(BindException::class)
  fun handlerBindException(e: BindException): ResponseEntity<ExceptionResponse> {
    val message = e.bindingResult.allErrors[0].defaultMessage.toString()
    log.warn("DTO validation error: $message", e)

    return ResponseEntity.status(BAD_REQUEST).body(ExceptionResponse(BAD_REQUEST.value(), message))
  }
}