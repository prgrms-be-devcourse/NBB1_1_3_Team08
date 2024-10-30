package org.prgrms.devconnect.api.controller.member.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.constant.Interest

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MemberResponseDto(
  @Schema(description = "회원 ID", example = "1")
  val memberId: Long,

  @Schema(description = "회원 이메일", example = "user@example.com")
  val email: String,

  @Schema(description = "회원 닉네임", example = "DevUser")
  val nickname: String,

  @Schema(description = "회원 직업", example = "Software Engineer")
  val job: String,

  @Schema(description = "회원 소속", example = "Company Name")
  val affiliation: String,

  @Schema(description = "회원 경력", example = "5")
  val career: Int,

  @Schema(description = "회원 자기소개", example = "안녕하세요! 저는 소프트웨어 엔지니어입니다.")
  val selfIntroduction: String,

  @Schema(description = "GitHub 링크", example = "https://github.com/user")
  val githubLink: String,

  @Schema(description = "블로그 링크", example = "https://userblog.com")
  val blogLink: String,

  @Schema(description = "회원 관심 분야", example = "TEAM_PROJECT")
  val interest: Interest,

  @Schema(description = "회원 기술 스택 목록")
  val techStacks: List<TechStackResponseDto>

) {
  companion object {
    fun from(member: Member): MemberResponseDto {
      return MemberResponseDto(
        memberId = member.memberId!!,
        email = member.email,
        nickname = member.nickname,
        job = member.job,
        affiliation = member.affiliation,
        career = member.career,
        selfIntroduction = member.selfIntroduction,
        githubLink = member.githubLink,
        blogLink = member.blogLink,
        interest = member.interest,
        techStacks = member.memberTechStacks.map {
          TechStackResponseDto.from(it.techStack)
        }.toList()
      )
    }
  }
}
