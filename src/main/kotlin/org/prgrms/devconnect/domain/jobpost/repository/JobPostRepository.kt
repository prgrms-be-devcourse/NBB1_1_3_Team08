package org.prgrms.devconnect.domain.jobpost.repository

import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.springframework.data.jpa.repository.JpaRepository

interface JobPostRepository : JpaRepository<JobPost, Long> {
}