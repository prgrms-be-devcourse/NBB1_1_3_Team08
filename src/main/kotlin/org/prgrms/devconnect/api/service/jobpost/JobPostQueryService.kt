package org.prgrms.devconnect.api.service.jobpost


import org.prgrms.devconnect.api.controller.jobpost.dto.response.JobPostInfoResponseDto
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.jobpost.JobPostException
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.jobpost.repository.JobPostRepository
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class JobPostQueryService(
        private val jobPostRepository: JobPostRepository
) {

    fun isJobPostByPostId(postId: Long): Boolean {
        return jobPostRepository.existsByPostId(postId)
    }

    fun getJobPostByIdOrThrow(jobPostId: Long): JobPost {
        return jobPostRepository.findById(jobPostId)
                .orElseThrow { JobPostException(ExceptionCode.NOT_FOUND_JOB_POST) }
    }

    // 공고 전체 조회
    fun getAllJobPosts(pageable: Pageable): Page<JobPostInfoResponseDto> {
        return jobPostRepository.findAllRecruiting(pageable)
    }

    // 공고 개별 조회
    fun getJobPost(jobPostId: Long): JobPostInfoResponseDto {
        val jobPost = getJobPostByIdOrThrow(jobPostId)
        jobPost.increaseViews()
        return JobPostInfoResponseDto.from(jobPost)
    }

    // TechStack name으로 공고 조회
    fun getJobPostsByTechStackName(name: String, pageable: Pageable): Page<JobPostInfoResponseDto> {
        return jobPostRepository.findAllByTechStackName(name, pageable)
    }

    // TechStack job_code로 공고 조회
    fun getJobPostsByTechStackJobCode(code: String, pageable: Pageable): Page<JobPostInfoResponseDto> {
        return jobPostRepository.findAllByTechStackJobCode(code, pageable)
    }

    // JobPostName 을 (제목별 공고 조회)
    fun getJobPostsByJobPostNameContaining(keyword: String, pageable: Pageable): Page<JobPostInfoResponseDto> {
        return jobPostRepository.findAllByJobPostNameContaining(keyword, pageable)
    }

    // 사용자 관심스택별 공고 조회
    fun getJobPostsByMemberInterestsTechStack(member: Member): Page<JobPostInfoResponseDto> {
        val pageable = Pageable.ofSize(5)
        return jobPostRepository.findAllByMemberInterests(member, pageable)
    }
}
