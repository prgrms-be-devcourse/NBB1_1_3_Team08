package org.prgrms.devconnect.api.controller.member.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import org.prgrms.devconnect.domain.member.entity.constant.Interest

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MemberUpdateRequestDto(
  @NotBlank(message = "닉네임은 필수입니다.")
  @Schema(description = "회원의 닉네임", example = "김철수")
  val nickname: String,

  @field:NotBlank(message = "직업은 필수입니다.")
  @Schema(description = "회원의 직업", example = "백엔드 엔지니어")
  val job: String,

  @field:NotBlank(message = "소속은 필수입니다.")
  @Schema(description = "회원의 소속", example = "Grep")
  val affiliation: String,

  @field:PositiveOrZero(message = "경력은 0 이상이어야 합니다.")
  @Schema(description = "회원의 경력(년)", example = "5")
  val career: Int,

  @field:NotBlank(message = "자기소개는 필수입니다.")
  @Schema(description = "회원의 자기소개", example = "자기 소개 예시 ....")
  val selfIntroduction: String,

  @field:NotBlank(message = "유효한 GitHub 링크를 입력하세요.")
  @Schema(description = "GitHub 링크", example = "https://github.com/")
  val githubLink: String,

  @field:NotBlank(message = "유효한 블로그 링크를 입력하세요.")
  @Schema(description = "블로그 링크", example = "https://blog.johndoe.com")
  val blogLink: String,

  @field:NotNull(message = "관심 분야는 필수입니다.")
  @Schema(description = "관심 분야", example = "TEAM_PROJECT")
  val interest: Interest,

  @Schema(description = "추가할 기술 스택 ID 목록", example = "[4, 5, 6]")
  val addTechStacks: List<Long>,

  @Schema(description = "삭제할 기술 스택 ID 목록", example = "[1, 2, 3]")
  val deleteTechStacks: List<Long>,
) {
}