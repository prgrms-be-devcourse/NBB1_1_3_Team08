package org.prgrms.devconnect.domain.jobpost.repository

import org.prgrms.devconnect.domain.jobpost.entity.JobPostTechStackMapping
import org.springframework.data.jpa.repository.JpaRepository

interface JobPostTechStackMappingRepository : JpaRepository<JobPostTechStackMapping, Long> {

}