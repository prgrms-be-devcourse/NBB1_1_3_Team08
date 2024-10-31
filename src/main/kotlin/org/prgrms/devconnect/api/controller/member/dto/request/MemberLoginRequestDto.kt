package org.prgrms.devconnect.api.controller.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

class MemberLoginRequestDto(
  @field:Email
  @Schema(description = "회원 이메일", required = true, example = "user@example.com")
  val email: String,

  @field:NotBlank
  @Schema(description = "비밀번호", required = true, example = "password123!")
  val password: String
) {
}