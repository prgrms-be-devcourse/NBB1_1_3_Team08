package org.prgrms.devconnect.common.auth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED
import org.prgrms.devconnect.common.exception.jwt.JwtException
import org.slf4j.LoggerFactory
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


class JwtExceptionFilter : OncePerRequestFilter() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(ServletException::class, IOException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
                                filterChain: FilterChain) {
    try {
      filterChain.doFilter(request, response)
    } catch (e: JwtException) {
      log.warn("JWT 예외 발생: {}", e.message)
      sendResponse(response, e)
    }
  }

  @Throws(IOException::class)
  private fun sendResponse(response: HttpServletResponse, e: JwtException) {
    response.status = SC_UNAUTHORIZED
    response.characterEncoding = "UTF-8"
    response.contentType = "text/plain;charset=UTF-8"
    response.writer.write(e.message.toString())
  }
}