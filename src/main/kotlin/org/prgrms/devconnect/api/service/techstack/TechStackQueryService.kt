package org.prgrms.devconnect.api.service.techstack

import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.common.exception.ExceptionCode.NOT_FOUND_TECH_STACK
import org.prgrms.devconnect.common.exception.techstack.TechStackException
import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.prgrms.devconnect.domain.techstack.repository.TechStackRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional(readOnly = true)
@Service
class TechStackQueryService(
    private val techStackRepository: TechStackRepository
) {

  fun getTechStacksByIdsOrThrow(techStackIds: List<Long>): List<TechStack> {
    val techStacks = techStackRepository.findAllByTechStackIdIn(techStackIds)

    if (techStacks.size != techStackIds.size) throw TechStackException(NOT_FOUND_TECH_STACK)

    return techStacks
  }

  fun getTechStackByIdOrThrow(techStackId: Long): TechStack {
    return techStackRepository.findByIdOrNull(techStackId)
        ?: throw TechStackException(NOT_FOUND_TECH_STACK)
  }

  fun getAllTechStacks(): List<TechStackResponseDto> {
    val techStacks = techStackRepository.findAll()
    return techStacks.map { it ->
      TechStackResponseDto.from(it)
    }.toList()
  }
}
