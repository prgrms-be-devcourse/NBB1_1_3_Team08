package org.prgrms.devconnect.api.service.member

import org.prgrms.devconnect.api.controller.member.dto.request.MemberCreateRequestDto
import org.prgrms.devconnect.api.controller.member.dto.request.MemberUpdateRequestDto
import org.prgrms.devconnect.api.service.techstack.TechStackQueryService
import org.prgrms.devconnect.common.auth.redis.RefreshToken
import org.prgrms.devconnect.common.auth.redis.RefreshTokenRepository
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.refresh.RefreshTokenException
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional
@Service
class MemberCommandService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val techStackQueryService: TechStackQueryService,
    private val memberQueryService: MemberQueryService,
    private val refreshTokenRepository: RefreshTokenRepository,
) {

  private val log = LoggerFactory.getLogger(this::class.java)

  fun createMember(requestDto: MemberCreateRequestDto): Member {
    memberQueryService.validateDuplicatedEmail(requestDto.email)

    val memberTechStacks = getTechStackMappings(requestDto.techStackIds)

    val member: Member = requestDto.toEntity(memberTechStacks)
    member.passwordEncode(passwordEncoder)
    memberRepository.save<Member>(member)

    return member
  }

  fun updateMember(memberId: Long, requestDto: MemberUpdateRequestDto) {
    val member: Member = memberQueryService.getMemberByIdWithTechStackOrThrow(memberId)
    member.updateFromDto(requestDto)

    // 삭제할 TechStack 처리
    val deleteTechIds: List<Long> = requestDto.deleteTechStacks
    if (deleteTechIds.isNotEmpty()) {
      deleteTechStacksFromMember(member, deleteTechIds)
    }

    // 추가할 TechStack 처리
    val addTechIds: List<Long> = requestDto.addTechStacks
    if (addTechIds.isNotEmpty()) {
      addTechStacksToMember(member, addTechIds)
    }

  }

  private fun getTechStackMappings(techStackIds: List<Long>): List<MemberTechStackMapping> {
    val techStacks: List<TechStack> = techStackQueryService.getTechStacksByIdsOrThrow(techStackIds)
    return techStacks.map { it ->
      MemberTechStackMapping(techStack = it)
    }.toList()
  }


  // 기술 스택 삭제 기능
  fun deleteTechStacksFromMember(member: Member, deleteTechIds: List<Long>) {
    // TODO
  }

  // 기술 스택 추가 기능
  fun addTechStacksToMember(member: Member?, addTechIds: List<Long?>?) {
    //TODO
  }

  fun logout(email: String) {
    log.info("[REDIS] Refresh Token 삭제 시도: {}", email)

    val token: RefreshToken = refreshTokenRepository.findByUserEmail(email)
        ?: throw RefreshTokenException(ExceptionCode.NOT_FOUND_REFRESH_TOKEN)

    refreshTokenRepository.delete(token)
    log.info("[REDIS] Refresh Token 삭제 성공: {}", email)
  }
}