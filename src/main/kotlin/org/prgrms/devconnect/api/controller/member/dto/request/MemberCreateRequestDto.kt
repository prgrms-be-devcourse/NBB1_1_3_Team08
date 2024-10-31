package org.prgrms.devconnect.api.controller.member.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.prgrms.devconnect.domain.member.entity.constant.Interest


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MemberCreateRequestDto(
  @field:NotBlank(message = "유효한 이메일 형식을 입력하세요.")
  @Schema(description = "회원 이메일", required = true, example = "user@example.com")
  val email: String,

  @field:NotBlank(message = "비밀번호는 필수입니다.")
  @Schema(description = "비밀번호", required = true, example = "password123!")
  val password: String,

  @field:NotBlank(message = "닉네임은 필수입니다.")
  @Schema(description = "닉네임", required = true, example = "DevUser")
  val nickname: String,

  @field:NotBlank(message = "직업은 필수입니다.")
  @Schema(description = "회원 직업", required = true, example = "Software Engineer")
  val job: String,

  @field:NotBlank(message = "소속은 필수입니다.")
  @Schema(description = "소속", required = true, example = "Company Name")
  val affiliation: String,

  @field:PositiveOrZero(message = "경력은 0 이상이어야 합니다.")
  @Schema(description = "경력", required = true, example = "5")
  val career: Int,

  @field:NotBlank(message = "자기소개는 필수입니다.")
  @Schema(description = "자기소개", required = true, example = "안녕하세요! 저는 소프트웨어 엔지니어입니다.")
  val selfIntroduction: String,

  @field:NotBlank(message = "유효한 GitHub 링크를 입력하세요.")
  @Schema(description = "GitHub 링크", required = true, example = "https://github.com/user")
  val githubLink: String,

  @field:NotBlank(message = "유효한 블로그 링크를 입력하세요.")
  @Schema(description = "블로그 링크", required = true, example = "https://userblog.com")
  val blogLink: String,

  @field:NotNull(message = "관심 분야는 필수입니다.")
  @Schema(description = "회원의 관심 분야", required = true, example = "TEAM_PROJECT")
  val interest: Interest,

  @field:NotEmpty(message = "기술 스택 목록은 비어있을 수 없습니다.")
  @field:Size(min = 1, message = "기술 스택 ID는 최소 하나 이상이어야 합니다.")
  @Schema(description = "기술 스택 ID 목록", required = true, example = "[1, 2, 3]")
  val techStackIds: List<@Positive(message = "기술 스택 ID는 0보다 커야 합니다.") Long>,

  ) {
  fun toEntity(memberTechStacks: List<MemberTechStackMapping>): Member {
    return Member(
      email = email,
      password = password,
      nickname = nickname,
      job = job,
      affiliation = affiliation,
      career = career,
      selfIntroduction = selfIntroduction,
      githubLink = githubLink,
      blogLink = blogLink,
      interest = interest,
      memberTechStacks = memberTechStacks
    )
  }
}