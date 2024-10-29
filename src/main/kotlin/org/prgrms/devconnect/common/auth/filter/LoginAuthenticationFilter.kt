package org.prgrms.devconnect.common.auth.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.prgrms.devconnect.api.controller.member.dto.request.MemberLoginRequestDto
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


class LoginAuthenticationFilter(
    private val objectMapper: ObjectMapper
) : UsernamePasswordAuthenticationFilter() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(AuthenticationException::class)
  override fun attemptAuthentication(request: HttpServletRequest,
                                     response: HttpServletResponse): Authentication {
    val dto: MemberLoginRequestDto = objectMapper.readValue(request.inputStream,
        MemberLoginRequestDto::class.java)

    log.info("로그인 시도: {}", dto.email)
    val authenticationToken = UsernamePasswordAuthenticationToken(
        dto.email, dto.password)

    return authenticationManager.authenticate(authenticationToken)
  }
}