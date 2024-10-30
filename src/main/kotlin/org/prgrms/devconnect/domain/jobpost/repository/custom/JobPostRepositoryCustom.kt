package org.prgrms.devconnect.domain.jobpost.repository.custom

import org.prgrms.devconnect.api.controller.jobpost.dto.response.JobPostInfoResponseDto
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface JobPostRepositoryCustom {

    // 채용중인 공고 전체 조회
    fun findAllRecruiting(pageable: Pageable): Page<JobPostInfoResponseDto>

    // StackName 별 공고 조회
    fun findAllByTechStackName(techStackName: String, pageable: Pageable): Page<JobPostInfoResponseDto>

    // JobCode 별 공고 조회
    fun findAllByTechStackJobCode(jobCode: String, pageable: Pageable): Page<JobPostInfoResponseDto>

    // JobPostName 별 조회 (제목별 공고 조회)
    fun findAllByJobPostNameContaining(keyword: String, pageable: Pageable): Page<JobPostInfoResponseDto>

    // 사용자 관심 스택별 조회
    fun findAllByMemberInterests(member: Member, pageable: Pageable): Page<JobPostInfoResponseDto>
}
