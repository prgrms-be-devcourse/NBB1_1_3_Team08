package org.prgrms.devconnect.api.controller.jobpost


import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.prgrms.devconnect.api.controller.jobpost.dto.response.JobPostInfoResponseDto
import org.prgrms.devconnect.api.service.jobpost.JobPostCommandService
import org.prgrms.devconnect.api.service.jobpost.JobPostQueryService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/job-posts")
@Tag(name = "채용 공고 API", description = "채용 관련 공고를 관리하는 API")
class JobPostController(
        private val jobPostQueryService: JobPostQueryService,
        private val jobPostCommandService: JobPostCommandService,
        private val memberQueryService: MemberQueryService
) {

    @Operation(summary = "채용 공고 삭제", description = "특정 채용 공고를 삭제합니다.")
    @DeleteMapping("/{jobPostId}")
    fun deleteJobPost(@PathVariable jobPostId: Long): ResponseEntity<Void> {
        jobPostCommandService.deleteJobPost(jobPostId)
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "모든 채용 공고 조회", description = "모든 채용 공고를 페이징 처리하여 조회합니다.")
    @GetMapping
    fun getAllJobPosts(pageable: Pageable): ResponseEntity<Page<JobPostInfoResponseDto>> {
        val jobPosts = jobPostQueryService.getAllJobPosts(pageable)
        return ResponseEntity.ok(jobPosts)
    }

    @Operation(summary = "특정 채용 공고 조회", description = "특정 ID에 해당하는 채용 공고의 상세 정보를 조회합니다.")
    @GetMapping("/{jobPostId}")
    fun getJobPost(@PathVariable jobPostId: Long): ResponseEntity<JobPostInfoResponseDto> {
        jobPostCommandService.increaseViews(jobPostId)
        val jobPost = jobPostQueryService.getJobPost(jobPostId)
        return ResponseEntity.ok(jobPost)
    }

    @Operation(summary = "기술 스택 이름으로 공고 조회", description = "특정 기술 스택 이름으로 채용 공고를 조회합니다.")
    @GetMapping("/techstack-name/{name}")
    fun getJobPostsByTechStackName(
            @PathVariable name: String,
            pageable: Pageable
    ): ResponseEntity<Page<JobPostInfoResponseDto>> {
        val jobPosts = jobPostQueryService.getJobPostsByTechStackName(name, pageable)
        return ResponseEntity.ok(jobPosts)
    }

    @Operation(summary = "기술 스택 코드로 공고 조회", description = "특정 기술 스택 코드로 채용 공고를 조회합니다.")
    @GetMapping("/techstack-code/{code}")
    fun getJobPostsByTechStackCode(
            @PathVariable code: String,
            pageable: Pageable
    ): ResponseEntity<Page<JobPostInfoResponseDto>> {
        val jobPosts = jobPostQueryService.getJobPostsByTechStackJobCode(code, pageable)
        return ResponseEntity.ok(jobPosts)
    }

    @Operation(summary = "제목으로 공고 검색", description = "제목에 특정 키워드가 포함된 채용 공고를 조회합니다.")
    @GetMapping("/search")
    fun getJobPostsByJobPostNameContaining(
            @RequestParam keyword: String,
            pageable: Pageable
    ): ResponseEntity<Page<JobPostInfoResponseDto>> {
        val jobPosts = jobPostQueryService.getJobPostsByJobPostNameContaining(keyword, pageable)
        return ResponseEntity.ok(jobPosts)
    }

    @Operation(summary = "사용자의 관심 기술 스택으로 공고 조회", description = "사용자의 관심 기술 스택에 맞는 채용 공고를 조회합니다.")
    @GetMapping("/interests")
    fun getJobPostsByMemberInterestsTechStack(
            @AuthenticationPrincipal member: Member
    ): ResponseEntity<Page<JobPostInfoResponseDto>> {
        memberQueryService.getMemberByIdOrThrow(member.memberId!!)
        val jobPosts = jobPostQueryService.getJobPostsByMemberInterestsTechStack(member)
        return ResponseEntity.ok(jobPosts)
    }
}
