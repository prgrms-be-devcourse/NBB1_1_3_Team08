package org.prgrms.devconnect.api.controller.member.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.prgrms.devconnect.domain.member.entity.constant.Interest


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MemberCreateRequestDto(
    val email: String,
    val password: String,
    val nickname: String,
    val job: String,
    val affiliation: String,
    val career: Int,
    val selfIntroduction: String,
    val githubLink: String,
    val blogLink: String,
    val interest: Interest,
    val techStackIds: List<Long>,

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