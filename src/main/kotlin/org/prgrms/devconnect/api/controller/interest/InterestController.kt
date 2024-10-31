package org.prgrms.devconnect.api.controller.interest

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.prgrms.devconnect.api.controller.interest.dto.request.InterestBoardRequestDto
import org.prgrms.devconnect.api.controller.interest.dto.request.InterestJobPostRequestDto
import org.prgrms.devconnect.api.controller.interest.dto.response.InterestResponseDto
import org.prgrms.devconnect.api.service.interest.InterestCommandService
import org.prgrms.devconnect.api.service.interest.InterestQueryService
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@Tag(name = "관심게시물 API", description = "관심 게시물 관련 API")
@RequestMapping("/api/v1/interests")
@RestController
class InterestController(
  private val interestCommandService: InterestCommandService,
  private val interestQueryService: InterestQueryService
) {
  @GetMapping
  @Operation(summary = "전체 관심 게시물 조회", description = "관심 게시물로 등록된 모든 게시물을 조회합니다.")
  fun getInterestBoards(@AuthenticationPrincipal member: Member): ResponseEntity<InterestResponseDto> {
    val responseDto = interestQueryService.getInterestsByMemberId(member.memberId!!)
    return ResponseEntity.ok(responseDto)
  }

  @Operation(summary = "관심 게시물로 등록", description = "관심 게시물로 등록합니다.")
  @PostMapping("/boards")
  fun addInterestBoard(
    @Valid @RequestBody requestDto: InterestBoardRequestDto,
    @AuthenticationPrincipal member: Member
  ): ResponseEntity<Void> {
    interestCommandService.addInterestBoard(requestDto, member.memberId!!)
    return ResponseEntity.status(HttpStatus.CREATED).build()
  }

  @DeleteMapping("/boards/{boardId}")
  @Operation(summary = "관심 게시물에서 해제", description = "관심 게시물에서 해제합니다.")
  fun removeInterestBoard(
    @AuthenticationPrincipal member: Member,
    @PathVariable boardId: Long
  ): ResponseEntity<Void> {
    interestCommandService.removeInterestBoard(member.memberId!!, boardId)

    return ResponseEntity.noContent().build()
  }

  @Operation(summary = "관심 채용공고으로 등록", description = "관심 채용공고으로 등록합니다.")
  @PostMapping("/job-posts")
  fun addInterestJob(
    @RequestBody requestDto: @Valid InterestJobPostRequestDto,
    @AuthenticationPrincipal member: Member
  ): ResponseEntity<Void> {
    interestCommandService.addInterestJobPost(requestDto, member.memberId!!)
    return ResponseEntity.status(HttpStatus.CREATED).build()
  }

  @DeleteMapping("/job-posts/{jobPostId}")
  @Operation(summary = "관심 채용공고에서 해제", description = "관심 채용공고에서 해제합니다.")
  fun removeInterestJobPost(
    @AuthenticationPrincipal member: Member,
    @PathVariable jobPostId: Long
  ): ResponseEntity<Void> {
    interestCommandService.removeInterestJobPost(member.memberId!!, jobPostId)
    return ResponseEntity.noContent().build()
  }
}