package org.prgrms.devconnect.domain.interest.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member


@Entity
@Table(name = "interest_job_post")
class InterestJobPost(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "interest_job_post_id")
  val id: Long? = null,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  val member: Member,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "job_post_id", nullable = false)
  val jobPost: JobPost,
) {
}