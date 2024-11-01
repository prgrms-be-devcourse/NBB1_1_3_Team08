package org.prgrms.devconnect.domain.jobpost.repository

import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.jobpost.repository.custom.JobPostRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface JobPostRepository : JpaRepository<JobPost, Long>, JobPostRepositoryCustom {
    fun existsByPostId(postId: Long): Boolean

    @Query("SELECT j FROM JobPost j LEFT JOIN FETCH j.jobPostTechStackMappings ts LEFT JOIN FETCH ts.techStack WHERE j.jobPostId = :jobPostId")
    fun findByIdWithTechStack(jobPostId: Long): JobPost?
}
