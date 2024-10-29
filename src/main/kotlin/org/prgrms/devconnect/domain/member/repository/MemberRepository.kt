package org.prgrms.devconnect.domain.member.repository

import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface MemberRepository : JpaRepository<Member, Long> {

  fun findByEmail(email: String): Member?

  @Query("SELECT m FROM Member m LEFT JOIN FETCH m.memberTechStacks mts LEFT JOIN FETCH mts.techStack WHERE m.memberId = :memberId")
  fun findByMemberIdWithTechStack(@Param("memberId") memberId: Long): Member?

  fun existsByEmail(email: String): Boolean
}
