package org.prgrms.devconnect.common.auth.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.prgrms.devconnect.common.auth.JwtService
import org.prgrms.devconnect.common.exception.ExceptionCode.NOT_FOUND_MEMBER
import org.prgrms.devconnect.common.exception.member.MemberException
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException


class JwtTokenAuthenticationFilter(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository
) : OncePerRequestFilter() {

  private val log = LoggerFactory.getLogger(this::class.java)

  @Throws(IOException::class, ServletException::class)
  override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse,
                                filterChain: FilterChain) {

    if (isRequestAuthorized(request)) {
      filterChain.doFilter(request, response)
      return
    }

    authenticateAccessToken(request, response, filterChain)
  }

  private fun isRequestAuthorized(request: HttpServletRequest): Boolean {
    val accessToken: String? = jwtService.extractAccessToken(request)
    return accessToken.isNullOrBlank()
  }

  @Throws(ServletException::class, IOException::class)
  private fun authenticateAccessToken(request: HttpServletRequest, response: HttpServletResponse,
                                      filterChain: FilterChain) {
    log.info("인증 시도")
    val token: String? = jwtService.extractAccessToken(request)
    jwtService.isTokenValid(token!!)
    val username = jwtService.extractUsername(token)
    val member = memberRepository.findByEmail(username!!) ?: throw MemberException(NOT_FOUND_MEMBER)
    saveAuthentication(member)

    filterChain.doFilter(request, response)
  }

  private fun saveAuthentication(member: Member) {
    val authentication: Authentication = UsernamePasswordAuthenticationToken(member, null, null)
    SecurityContextHolder.getContext().authentication = authentication
    log.info("인증 완료: {}", member.email)
  }
}