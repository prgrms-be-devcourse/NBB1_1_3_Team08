package org.prgrms.devconnect.common.auth.handler

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import java.io.IOException


class LoginFailureHandler : SimpleUrlAuthenticationFailureHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(IOException::class)
  override fun onAuthenticationFailure(request: HttpServletRequest?, response: HttpServletResponse,
                                       exception: AuthenticationException) {
    log.info("로그인 실패")
    sendResponse(response)
  }

  @Throws(IOException::class)
  private fun sendResponse(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_BAD_REQUEST
    response.characterEncoding = "UTF-8"
    response.contentType = "text/plain;charset=UTF-8"
    response.writer.write("로그인 실패")
  }
}