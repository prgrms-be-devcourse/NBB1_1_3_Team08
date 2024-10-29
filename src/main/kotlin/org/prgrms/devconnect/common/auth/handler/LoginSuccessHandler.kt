package org.prgrms.devconnect.common.auth.handler

import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.prgrms.devconnect.common.auth.JwtService
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import java.io.IOException


class LoginSuccessHandler(
    private val jwtService: JwtService
) : SimpleUrlAuthenticationSuccessHandler() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(IOException::class, ServletException::class)
  override fun onAuthenticationSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                       authentication: Authentication) {

    val username = extractUsername(authentication)
    val accessToken: String = jwtService.createAccessToken(username)
    val refreshToken: String = jwtService.createRefreshToken(username)
    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken)
    log.info("로그인 성공: {}", username)
    log.info("Access & refresh 토큰 발급")
  }

  private fun extractUsername(authentication: Authentication): String {
    val userDetails = authentication.principal as UserDetails
    return userDetails.username
  }
}