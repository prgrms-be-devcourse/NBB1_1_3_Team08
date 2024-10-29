package org.prgrms.devconnect.api.service.member

import org.prgrms.devconnect.api.controller.member.dto.request.MemberLoginRequestDto
import org.prgrms.devconnect.api.controller.member.dto.response.MemberResponseDto
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.member.MemberException
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional(readOnly = true)
@Service
class MemberQueryService(
    private val memberRepository: MemberRepository
) {

  fun getMember(memberId: Long): MemberResponseDto {
    val member: Member = getMemberByIdWithTechStackOrThrow(memberId)
    return MemberResponseDto.from(member)
  }

  fun getMemberByIdWithTechStackOrThrow(memberId: Long): Member {
    return memberRepository.findByMemberIdWithTechStack(memberId)
        ?: throw MemberException(ExceptionCode.NOT_FOUND_MEMBER)
  }

  fun getTechStacksByMemberId(memberId: Long): List<TechStack> {
    val member: Member = getMemberByIdWithTechStackOrThrow(memberId)
    return member.memberTechStacks.map {
      it.techStack
    }.toList()
  }

  fun getMemberByIdOrThrow(memberId: Long): Member {
    return memberRepository.findByIdOrNull(memberId)
        ?: throw MemberException(ExceptionCode.NOT_FOUND_MEMBER)
  }

  fun getMemberByEmailOrThrow(email: String): Member {
    return memberRepository.findByEmail(email)
        ?: throw MemberException(ExceptionCode.NOT_FOUND_MEMBER)
  }

  fun validateDuplicatedEmail(email: String) {
    if (memberRepository.existsByEmail(email)) throw MemberException(ExceptionCode.DUPLICATED_MEMBER_EMAIL)
  }
}