package org.prgrms.devconnect.domain.member.repository

import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MemberTechStackMappingRepository : JpaRepository<MemberTechStackMapping, Long> {

  @Modifying
  @Query("DELETE FROM MemberTechStackMapping m WHERE m.member.memberId = :memberId and m.techStack.techStackId IN :techStackIds")
  fun deleteAllByMemberIdAndInTechStackIds(memberId: Long, techStackIds: List<Long>)

}