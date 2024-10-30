package org.prgrms.devconnect.api.service.jobpost


import org.prgrms.devconnect.domain.jobpost.entity.constant.Status
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class JobPostCommandService(
        private val jobPostQueryService: JobPostQueryService,
) {


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
