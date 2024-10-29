package org.prgrms.devconnect.common.auth

import io.jsonwebtoken.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.prgrms.devconnect.common.auth.redis.RefreshToken
import org.prgrms.devconnect.common.auth.redis.RefreshTokenRepository
import org.prgrms.devconnect.common.exception.ExceptionCode.*
import org.prgrms.devconnect.common.exception.jwt.JwtException
import org.prgrms.devconnect.common.exception.member.MemberException
import org.prgrms.devconnect.common.exception.refresh.RefreshTokenException
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec


@Service
class JwtService(
    @Value("\${jwt.secretKey}")
    private val secretKey: String,

    @Value("\${jwt.access.expiration}")
    private val accessTokenExpirationPeriod: Long,

    @Value("\${jwt.refresh.expiration}")
    private val refreshTokenExpirationPeriod: Long,

    @Value("\${jwt.access.header}")
    private val accessHeader: String,

    @Value("\${jwt.refresh.header}")
    private val refreshHeader: String,

    private val refreshTokenRepository: RefreshTokenRepository,
    private val memberRepository: MemberRepository
) {

  private val log = LoggerFactory.getLogger(this::class.java)

  private fun getSecretKey(): SecretKey {
    return SecretKeySpec(secretKey.toByteArray(StandardCharsets.UTF_8), "HmacSHA256")
  }

  fun createAccessToken(username: String): String {
    return Jwts.builder()
        .claim("username", username)
        .claim("category", "AccessToken")
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + accessTokenExpirationPeriod!!))
        .signWith(getSecretKey())
        .compact()
  }

  fun createRefreshToken(username: String): String {
    val token: String = Jwts.builder()
        .claim("username", username)
        .claim("category", "RefreshToken")
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(Date(System.currentTimeMillis() + refreshTokenExpirationPeriod!!))
        .signWith(getSecretKey())
        .compact()

    val refreshToken: RefreshToken = RefreshToken(
        refreshToken = token,
        userEmail = username
    )

    refreshTokenRepository.save(refreshToken)
    return token
  }

  fun sendAccessAndRefreshToken(response: HttpServletResponse, accessToken: String,
                                refreshToken: String) {
    response.status = HttpServletResponse.SC_OK
    setAccessTokenHeader(response, accessToken)
    setRefreshTokenCookie(response, refreshToken)
  }

  fun extractAccessToken(request: HttpServletRequest): String? {
    val accessToken = request.getHeader(accessHeader)
    return if (accessToken != null && accessToken.startsWith(BEARER)) {
      accessToken.replace(BEARER, "")
    } else {
      null
    }
  }

  fun extractUsername(accessToken: String): String? {
    return getClaimsJws(accessToken).payload
        .get("username", String::class.java)
  }


  private fun setAccessTokenHeader(response: HttpServletResponse, accessToken: String) {
    response.setHeader(accessHeader, BEARER + accessToken)
  }

  private fun setRefreshTokenCookie(response: HttpServletResponse, refreshToken: String) {
    val cookie: Cookie = Cookie(refreshHeader, refreshToken)
    cookie.isHttpOnly = true
    cookie.secure = true
    cookie.path = "/"
    cookie.maxAge = (refreshTokenExpirationPeriod / 1000).toInt()
    response.addCookie(cookie)
  }

  fun isTokenValid(token: String): Boolean {
    try {
      val claimsJws: Jws<Claims> = getClaimsJws(token)
      val expiration: Date = claimsJws.payload.expiration
      return expiration.after(Date())
    } catch (e: ExpiredJwtException) {
      throw JwtException(JWT_TOKEN_EXPIRED)
    } catch (e: MalformedJwtException) {
      throw JwtException(JWT_TOKEN_MALFORMED)
    } catch (e: Exception) {
      throw JwtException(JWT_SIGNATURE_INVALID)
    }
  }

  private fun getClaimsJws(token: String): Jws<Claims> {
    return Jwts.parser()
        .verifyWith(getSecretKey())
        .build()
        .parseSignedClaims(token)
  }

  fun reIssueAccessToken(response: HttpServletResponse, refreshToken: String) {
    log.info("Access Token 재발급 시도: {}", refreshToken)
    val token: RefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
        ?: throw RefreshTokenException(NOT_FOUND_REFRESH_TOKEN)

    isTokenValid(refreshToken)
    val username: String = token.userEmail
    if (!memberRepository.existsByEmail(username)) {
      throw MemberException(NOT_FOUND_MEMBER)
    }

    val accessToken = createAccessToken(username)
    this.setAccessTokenHeader(response, accessToken)
    log.info("Access Token 재발급 성공")
  }

  companion object {
    private const val BEARER = "Bearer "
  }
}