package org.prgrms.devconnect.api.service.member

import org.prgrms.devconnect.api.controller.member.dto.request.MemberCreateRequestDto
import org.prgrms.devconnect.api.controller.member.dto.request.MemberUpdateRequestDto
import org.prgrms.devconnect.api.service.techstack.TechStackQueryService
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.prgrms.devconnect.domain.member.repository.MemberTechStackMappingRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional
@Service
class MemberCommandService(
  private val memberRepository: MemberRepository,
  private val memberTechStackRepository: MemberTechStackMappingRepository,
  private val passwordEncoder: PasswordEncoder,
  private val techStackQueryService: TechStackQueryService,
  private val memberQueryService: MemberQueryService,
) {

  fun createMember(requestDto: MemberCreateRequestDto): Member {
    memberQueryService.validateDuplicatedEmail(requestDto.email)

    val memberTechStacks = getTechStackMappings(requestDto.techStackIds)
    val member = requestDto.toEntity(memberTechStacks).apply {
      passwordEncode(passwordEncoder)
    }

    return memberRepository.save(member)
  }

  fun updateMember(memberId: Long, requestDto: MemberUpdateRequestDto) {
    val member = memberQueryService.getMemberByIdWithTechStackOrThrow(memberId).apply {
      updateFromDto(requestDto)
    }

    handleTechStackUpdates(member, requestDto)
  }

  private fun handleTechStackUpdates(member: Member, requestDto: MemberUpdateRequestDto) {
    requestDto.deleteTechStacks.takeIf { it.isNotEmpty() }?.let {
      deleteTechStacksFromMember(member, it)
    }

    requestDto.addTechStacks.takeIf { it.isNotEmpty() }?.let {
      addTechStacksToMember(member, it)
    }
  }

  private fun getTechStackMappings(techStackIds: List<Long>): List<MemberTechStackMapping> {
    val techStacks = techStackQueryService.getTechStacksByIdsOrThrow(techStackIds)
    return techStacks.map { MemberTechStackMapping(techStack = it) }
  }

  private fun deleteTechStacksFromMember(member: Member, deleteTechIds: List<Long>) {
    memberTechStackRepository.deleteAllByMemberIdAndInTechStackIds(member.memberId!!, deleteTechIds)
  }

  private fun addTechStacksToMember(member: Member, addTechIds: List<Long>) {
    val techStacks = techStackQueryService.getTechStacksByIdsOrThrow(addTechIds)
    val memberTechStacks =
      techStacks.map { MemberTechStackMapping(techStack = it, member = member) }

    memberTechStackRepository.saveAll(memberTechStacks)
  }

}