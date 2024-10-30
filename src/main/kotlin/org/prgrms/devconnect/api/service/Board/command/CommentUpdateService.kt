package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.controller.board.dto.request.CommentUpdateRequestDto
import org.prgrms.devconnect.api.service.Board.query.CommentQueryService
import org.prgrms.devconnect.domain.board.repository.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentUpdateService(
    private val commentQueryService: CommentQueryService,
    private val commentRepository: CommentRepository,

) {
    fun updateComment(commentId: Long, commentUpdateRequestDto: CommentUpdateRequestDto) {
        val comment = commentQueryService.getCommentByIdOrThrow(commentId)
        comment.updateFromDto(commentUpdateRequestDto)
        commentRepository.save(comment)
    }
}