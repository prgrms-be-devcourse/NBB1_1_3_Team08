package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.domain.board.repository.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentDeleteService (
    private val commentRepository: CommentRepository
) {
    fun deleteComment(commentId: Long) {
        commentRepository.deleteById(commentId)
    }
}