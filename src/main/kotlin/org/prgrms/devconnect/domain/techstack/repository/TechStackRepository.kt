package org.prgrms.devconnect.domain.techstack.repository

import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.springframework.data.jpa.repository.JpaRepository


interface TechStackRepository : JpaRepository<TechStack, Long> {
  fun findAllByTechStackIdIn(techStackIds: List<Long>): List<TechStack>
}