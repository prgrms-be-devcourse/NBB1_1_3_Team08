package org.prgrms.devconnect.common.auth.handler

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import java.io.IOException


class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(IOException::class, ServletException::class)
  override fun commence(request: HttpServletRequest, response: HttpServletResponse,
                        authException: AuthenticationException) {

    log.warn("인증 실패: {}", authException.message)
    sendResponse(response)
  }

  @Throws(IOException::class)
  private fun sendResponse(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_UNAUTHORIZED
    response.characterEncoding = "UTF-8"
    response.contentType = "text/plain;charset=UTF-8"
    response.writer.write("인증 실패")
  }
}