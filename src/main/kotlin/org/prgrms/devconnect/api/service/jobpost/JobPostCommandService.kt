package org.prgrms.devconnect.api.service.jobpost


import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.jobpost.entity.constant.Status
import org.prgrms.devconnect.domain.jobpost.repository.JobPostRepository
import org.prgrms.devconnect.domain.jobpost.repository.JobPostTechStackRepository
import org.prgrms.devconnect.external.saramin.JobPostApi
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JobPostCommandService(
        private val jobPostRepository: JobPostRepository,
        private val jobPostTechStackRepository: JobPostTechStackRepository,
        private val jobPostQueryService: JobPostQueryService,
        private val jobPostAPI: JobPostApi
) {

    companion object {
        private const val BATCH_SIZE = 100 // 한 번에 가져올 데이터 수
    }

    // 채용공고 저장 로직
    fun saveJobPosts() {
        var start = 0
        val total = jobPostAPI.getTotal(start)

        while (start < total) {
            val jobPosts = jobPostAPI.fetchJobPosts(start)

            if (jobPosts.isEmpty()) {
                break // 더 이상 가져올 공고가 없을 경우 종료
            }

            // JobPost와 JobPostTechStackMapping을 저장
            saveJobPostsAndMappings(jobPosts)

            // start 값을 증가시켜 다음 배치로 이동
            start += BATCH_SIZE
        }
    }

    private fun saveJobPostsAndMappings(jobPosts: List<JobPost>) {
        // JobPost 저장
        jobPostRepository.saveAll(jobPosts)
        jobPostRepository.flush()

        // JobPost와 연결된 JobPostTechStackMapping 저장
        val mappings = jobPosts.flatMap { it.jobPostTechStackMappings }
        jobPostTechStackRepository.saveAll(mappings)
    }


    // 공고 삭제
    fun deleteJobPost(jobPostId: Long) {
        val jobPost = jobPostQueryService.getJobPostByIdOrThrow(jobPostId)
        jobPost.updateStatus(Status.DELETED)
    }

    // 공고 좋아요 증가
    fun likeJobPost(jobPostId: Long) {
        val jobPost = jobPostQueryService.getJobPostByIdOrThrow(jobPostId)
        jobPost.increaseLikes()
    }

    // 공고 좋아요 감소
    fun unlikeJobPost(jobPostId: Long) {
        val jobPost = jobPostQueryService.getJobPostByIdOrThrow(jobPostId)
        jobPost.decreaseLikes()
    }

    // 공고 조회수 증가
    fun increaseViews(jobPostId: Long) {
        val jobPost = jobPostQueryService.getJobPostByIdOrThrow(jobPostId)
        jobPost.increaseViews()
    }
}
