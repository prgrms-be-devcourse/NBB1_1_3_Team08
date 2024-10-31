package org.prgrms.devconnect.domain.interest.repository

import org.prgrms.devconnect.domain.interest.entity.InterestJobPost
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface InterestJobPostRepository : JpaRepository<InterestJobPost, Long> {
  @Query("SELECT ij FROM InterestJobPost ij LEFT JOIN FETCH ij.jobPost WHERE ij.member = :member")
  fun findAllByMemberWithJobPost(member: Member): List<InterestJobPost>

  @Query("SELECT ij FROM InterestJobPost ij WHERE ij.member.memberId = :memberId And ij.jobPost.jobPostId = :jobPostId")
  fun findByMemberIdAndJobPostId(memberId: Long, jobPostId: Long): InterestJobPost?

  fun existsByMemberAndJobPost(member: Member, jobPost: JobPost): Boolean
}