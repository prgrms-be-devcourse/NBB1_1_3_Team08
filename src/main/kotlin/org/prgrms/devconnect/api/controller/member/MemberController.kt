package org.prgrms.devconnect.api.controller.member

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.prgrms.devconnect.api.controller.member.dto.request.MemberCreateRequestDto
import org.prgrms.devconnect.api.controller.member.dto.request.MemberLoginRequestDto
import org.prgrms.devconnect.api.controller.member.dto.request.MemberUpdateRequestDto
import org.prgrms.devconnect.api.controller.member.dto.response.MemberResponseDto
import org.prgrms.devconnect.api.service.member.MemberCommandService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.common.auth.JwtService
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/members")
@Tag(name = "멤버 API", description = "회원 관리 관련 API")
class MemberController(
  private val memberCommandService: MemberCommandService,
  private val memberQueryService: MemberQueryService,
  private val jwtService: JwtService
) {

  @GetMapping
  @Operation(summary = "회원 정보 조회", description = "인증된 사용자의 회원 정보를 조회합니다.")
  fun getMember(@AuthenticationPrincipal member: Member): ResponseEntity<MemberResponseDto> {
    val responseDto: MemberResponseDto = memberQueryService.getMember(member.memberId!!)
    return ResponseEntity.ok(responseDto)
  }

  @PostMapping("/signup")
  @Operation(summary = "회원 가입", description = "새로운 사용자를 등록합니다.")
  fun createMember(@RequestBody dto: @Valid MemberCreateRequestDto): ResponseEntity<Void> {
    memberCommandService.createMember(dto)
    return ResponseEntity.status(HttpStatus.CREATED).build()
  }

  @PostMapping("/login")
  @Operation(summary = "로그인", description = "사용자 로그인 처리.")
  fun login(@RequestBody dto: @Valid MemberLoginRequestDto) {
    // 로그인 처리 로직
  }

  @PostMapping("/logout")
  @Operation(summary = "로그아웃", description = "현재 로그인된 사용자를 로그아웃 처리.")
  fun logout(@AuthenticationPrincipal member: Member): ResponseEntity<Void> {
    jwtService.deleteRefreshTokenByEmail(member.email)
    return ResponseEntity.ok().build()
  }

  @PutMapping
  @Operation(summary = "회원 정보 수정", description = "현재 회원의 정보를 수정합니다.")
  fun updateMember(
    @AuthenticationPrincipal member: Member,
    @RequestBody dto: @Valid MemberUpdateRequestDto
  ): ResponseEntity<Void> {
    memberCommandService.updateMember(member.memberId!!, dto)
    return ResponseEntity.ok().build()
  }

  @GetMapping("/reissue")
  @Operation(
    summary = "Access Token 재발급",
    description = "Refresh Token을 사용하여 새로운 Access Token을 발급받습니다."
  )
  fun reissueAccessToken(
    @CookieValue(value = "Authorization-refresh", defaultValue = "") refreshToken: String,
    response: HttpServletResponse
  ): ResponseEntity<Void> {
    jwtService.reIssueAccessToken(response, refreshToken)
    return ResponseEntity.ok().build()
  }
}