package org.prgrms.devconnect.domain.jobpost.repository

import org.prgrms.devconnect.domain.jobpost.entity.JobPostTechStackMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface JobPostTechStackRepository : JpaRepository<JobPostTechStackMapping, Long> {

}