package org.prgrms.devconnect.api.controller.interest.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotNull
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.member.entity.Member

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "관심 게시글 등록 요청 정보")
class InterestBoardRequestDto(
  @field:NotNull(message = "게시물 ID는 필수입니다.")
  @Schema(description = "게시물 ID", example = "1")
  val boardId: Long
) {

  fun toEntity(member: Member, board: Board): InterestBoard {
    return InterestBoard(
      member = member,
      board = board
    )
  }
}
