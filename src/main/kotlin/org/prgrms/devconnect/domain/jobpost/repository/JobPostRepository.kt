package org.prgrms.devconnect.domain.jobpost.repository

import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.jobpost.repository.custom.JobPostRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JobPostRepository : JpaRepository<JobPost, Long> , JobPostRepositoryCustom {
    fun existsByPostId(postId: Long): Boolean
}