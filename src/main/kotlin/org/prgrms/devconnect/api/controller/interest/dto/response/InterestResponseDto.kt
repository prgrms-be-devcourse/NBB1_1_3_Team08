package org.prgrms.devconnect.api.controller.interest.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.api.controller.board.dto.response.BoardResponseDto
import org.prgrms.devconnect.api.controller.jobpost.dto.response.JobPostInfoResponseDto
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.interest.entity.InterestJobPost


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "관심 게시물 전체 조회 응답 정보")
class InterestResponseDto(
  @Schema(description = "여러 개의 단일 게시글 정보")
  val interestBoards: List<BoardResponseDto>,

  @Schema(description = "여러 개의 채용공고 게시글 정보")
  val interestJobPosts: List<JobPostInfoResponseDto>,
) {

  companion object {
    fun from(
      interestBoards: List<InterestBoard>,
      interestJobPosts: List<InterestJobPost>
    ): InterestResponseDto {
      return InterestResponseDto(
        interestBoards = interestBoards.map { it ->
          BoardResponseDto.from(it.board)
        },
        interestJobPosts = interestJobPosts.map { it ->
          JobPostInfoResponseDto.from(it.jobPost)
        }
      )
    }
  }
}