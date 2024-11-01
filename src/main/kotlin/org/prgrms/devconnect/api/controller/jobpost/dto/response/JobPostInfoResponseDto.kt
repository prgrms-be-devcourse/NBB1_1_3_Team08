package org.prgrms.devconnect.api.controller.jobpost.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.jobpost.entity.JobPostTechStackMapping
import org.prgrms.devconnect.domain.jobpost.entity.constant.JobType
import org.prgrms.devconnect.domain.jobpost.entity.constant.Status
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class JobPostInfoResponseDto(
        @Schema(description = "채용 공고 ID", example = "123")
        val jobPostId: Long,

        @Schema(description = "게시물 ID", example = "456")
        val postId: Long,

        @Schema(description = "채용 공고 이름", example = "Backend Developer")
        val jobPostName: String,

        @Schema(description = "회사 이름", example = "ABC Corp")
        val companyName: String,

        @Schema(description = "회사 주소", example = "서울시 강남구 테헤란로 123")
        val companyAddress: String? = "",

        @Schema(description = "회사 링크", example = "http://example.com")
        val companyLink: String? = "",

        @Schema(description = "게시 날짜", example = "2024-10-04T10:15:30")
        val postDate: LocalDateTime,

        @Schema(description = "공고 시작 날짜", example = "2024-10-05T10:15:30")
        val openDate: LocalDateTime,

        @Schema(description = "공고 종료 날짜", example = "2024-10-30T10:15:30")
        val endDate: LocalDateTime,

        @Schema(description = "급여", example = "회사 내규에 따름")
        val salary: String,

        @Schema(description = "직무 타입", example = "REGULAR")
        val jobType: JobType,

        @Schema(description = "상태", example = "RECRUITING")
        val status: Status,

        @Schema(description = "조회수", example = "100")
        val views: Int,

        @Schema(description = "좋아요 수", example = "100")
        val likes: Int,

        @Schema(description = "직무 기술 스택 목록")
        val techStacks: List<TechStackResponseDto>
) {
    companion object {
        fun from(jobPost: JobPost): JobPostInfoResponseDto {
            val techStackDtos = jobPost.jobPostTechStackMappings
                    .map(JobPostTechStackMapping::techStack)
                    .map(TechStackResponseDto::from)

            return JobPostInfoResponseDto(
                    jobPostId = jobPost.jobPostId!!,
                    postId = jobPost.postId,
                    jobPostName = jobPost.jobPostName,
                    companyName = jobPost.companyName,
                    companyAddress = jobPost.companyAddress,
                    companyLink = jobPost.companyLink,
                    postDate = jobPost.postDate,
                    openDate = jobPost.openDate,
                    endDate = jobPost.endDate,
                    salary = jobPost.salary,
                    jobType = jobPost.jobType,
                    status = jobPost.status,
                    views = jobPost.views,
                    likes = jobPost.likes,
                    techStacks = techStackDtos
            )
        }
    }
}
