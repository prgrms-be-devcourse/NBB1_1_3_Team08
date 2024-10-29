package org.prgrms.devconnect.api.controller.member.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.constant.Interest

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MemberResponseDto(
    val memberId: Long,
    val email: String,
    val nickname: String,
    val job: String,
    val affiliation: String,
    val career: Int,
    val selfIntroduction: String,
    val githubLink: String,
    val interest: Interest,
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
          interest = member.interest,
          techStacks = member.memberTechStacks.map {
            TechStackResponseDto.from(it.techStack)
          }.toList()
      )
    }
  }
}
