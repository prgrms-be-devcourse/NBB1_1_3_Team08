package org.prgrms.devconnect.api.controller.board.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.domain.board.entity.Comment
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class CommentResponseDto(
    @Schema(description = "댓글 ID", example = "1")
    val commentId: Long?,

    @Schema(description = "작성자 ID", example = "2")
    val memberId: Long?,

    @Schema(description = "작성자 닉네임", example = "JohnDoe")
    val author: String,

    @Schema(description = "댓글 내용", example = "이것은 댓글입니다.")
    val content: String,

    @Schema(description = "댓글 수정 날짜", example = "2024-12-31T23:59:59")
    val updatedAt: LocalDateTime?,

    @Schema(description = "부모 댓글 ID (대댓글인 경우)", example = "2")
    val parentId: Long?
) {
    companion object {
        fun from(comment:Comment) : CommentResponseDto {
            return CommentResponseDto(
                commentId = comment.commentId,
                memberId = comment.member?.memberId,
                author = comment.member!!.nickname,
                content = comment.content,
                updatedAt = comment.updatedAt,
                parentId = comment.parent?.commentId
            )
        }
    }
}
