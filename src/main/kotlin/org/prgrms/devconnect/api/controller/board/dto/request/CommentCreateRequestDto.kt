package org.prgrms.devconnect.api.controller.board.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.Comment
import org.prgrms.devconnect.domain.member.entity.Member

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CommentCreateRequestDto(

    @field:Schema(description = "게시판 ID", example = "1")
    @field:NotNull(message = "게시판 ID는 필수입니다.")
    val boardId: Long,

    @field:Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "2")
    val parentId: Long? = null,

    @field:Schema(description = "댓글 내용", example = "이 게시물 정말 유익하네요!")
    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String
) {
    // 엔티티로 변환하는 함수
    fun toEntity(member: Member, board: Board, parentComment: Comment?): Comment {
        return Comment(
            member = member,
            board = board,
            parent = parentComment,
            content = content
        )
    }
}
