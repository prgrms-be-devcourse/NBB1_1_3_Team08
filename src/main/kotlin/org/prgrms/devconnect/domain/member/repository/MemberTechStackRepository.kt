package org.prgrms.devconnect.domain.member.repository

import org.prgrms.devconnect.domain.member.entity.MemberTechStackMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MemberTechStackMappingRepository : JpaRepository<MemberTechStackMapping, Long> {

  @Modifying
  @Query("DELETE FROM MemberTechStackMapping m WHERE m.id IN :ids")
  fun deleteAllByIds(@Param("ids") ids: List<Long>)
}