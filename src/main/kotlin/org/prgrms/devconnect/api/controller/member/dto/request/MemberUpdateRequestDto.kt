package org.prgrms.devconnect.api.controller.member.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.prgrms.devconnect.domain.member.entity.constant.Interest

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class MemberUpdateRequestDto(
    val nickname: String,
    val job: String,
    val affiliation: String,
    val career: Int,
    val selfIntroduction: String,
    val githubLink: String,
    val blogLink: String,
    val interest: Interest,
    val addTechStacks: List<Long>,
    val deleteTechStacks: List<Long>,
) {
}