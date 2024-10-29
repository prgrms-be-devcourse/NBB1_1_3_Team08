package org.prgrms.devconnect.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.prgrms.devconnect.common.auth.CustomerMemberDetailsService
import org.prgrms.devconnect.common.auth.JwtService
import org.prgrms.devconnect.common.auth.filter.JwtExceptionFilter
import org.prgrms.devconnect.common.auth.filter.JwtTokenAuthenticationFilter
import org.prgrms.devconnect.common.auth.filter.LoginAuthenticationFilter
import org.prgrms.devconnect.common.auth.handler.CustomAuthenticationEntryPoint
import org.prgrms.devconnect.common.auth.handler.LoginFailureHandler
import org.prgrms.devconnect.common.auth.handler.LoginSuccessHandler
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtService: JwtService,
    private val memberRepository: MemberRepository,
    private val customerMemberDetailsService: CustomerMemberDetailsService,
    private val objectMapper: ObjectMapper
) {


  @Bean
  @Throws(Exception::class)
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http
        .cors { it.disable() }
        .csrf { it.disable() }
        .formLogin { it.disable() }
        .httpBasic { it.disable() }
        .headers { it.frameOptions { config -> config.sameOrigin() } }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests {
          it.requestMatchers("/", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
              .requestMatchers("/static/**", "/templates/**").permitAll() // 멤버
              .requestMatchers("/api/v1/members/login", "/api/v1/members/signup",
                  "/api/v1/members/reissue").permitAll()
              .requestMatchers("/api/v1/members/logout", "/api/v1/members").authenticated() // 알림
              .requestMatchers("/api/v1/alarms/**").authenticated() // 게시판
              .requestMatchers(HttpMethod.GET, "/api/v1/boards/**").permitAll()
              .requestMatchers("/api/v1/boards/**").authenticated() // 버그 리포트
              .requestMatchers("/api/v1/bug-report/**").authenticated() // 댓글
              .requestMatchers(HttpMethod.GET, "/api/v1/comments/**").permitAll()
              .requestMatchers("/api/v1/comments/**").authenticated() // 관심
              .requestMatchers("/api/v1/interests/**").authenticated() // 채용 공고
              .requestMatchers(HttpMethod.DELETE, "/api/v1/job-posts/**").authenticated()
              .requestMatchers("/api/v1/job-posts/**").permitAll()
              .anyRequest().permitAll()
        }

        .addFilterAfter(loginAuthenticationFilter(), LogoutFilter::class.java)
        .addFilterBefore(jwtAuthenticationFilter(), LoginAuthenticationFilter::class.java)
        .addFilterBefore(jwtExceptionFilter(), JwtTokenAuthenticationFilter::class.java)

    return http.build()
  }

  @Bean
  fun passwordEncoder(): PasswordEncoder {
    return BCryptPasswordEncoder()
  }

  @Bean
  fun authenticationManager(): AuthenticationManager {
    val provider = DaoAuthenticationProvider()
    provider.setPasswordEncoder(passwordEncoder())
    provider.setUserDetailsService(customerMemberDetailsService)
    return ProviderManager(provider)
  }

  @Bean
  fun jwtAuthenticationFilter(): JwtTokenAuthenticationFilter {
    return JwtTokenAuthenticationFilter(jwtService, memberRepository)
  }

  @Bean
  fun loginAuthenticationFilter(): LoginAuthenticationFilter {
    val loginAuthenticationFilter = LoginAuthenticationFilter(objectMapper)
    loginAuthenticationFilter.setAuthenticationManager(authenticationManager())
    loginAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler())
    loginAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler())
    loginAuthenticationFilter.setFilterProcessesUrl("/api/v1/members/login")

    return loginAuthenticationFilter
  }

  @Bean
  fun loginSuccessHandler(): LoginSuccessHandler {
    return LoginSuccessHandler(jwtService)
  }

  @Bean
  fun loginFailureHandler(): LoginFailureHandler {
    return LoginFailureHandler()
  }

  @Bean
  fun jwtExceptionFilter(): JwtExceptionFilter {
    return JwtExceptionFilter()
  }

  @Bean
  fun authenticationEntryPoint(): AuthenticationEntryPoint {
    return CustomAuthenticationEntryPoint()
  }
}