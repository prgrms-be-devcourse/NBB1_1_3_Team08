package org.prgrms.devconnect.api.service.Board.query

import org.prgrms.devconnect.api.controller.board.dto.response.CommentResponseDto
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.board.CommentException
import org.prgrms.devconnect.domain.board.entity.Comment
import org.prgrms.devconnect.domain.board.repository.CommentRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class CommentQueryService (
    private val commentRepository: CommentRepository
) {
    fun getCommentByIdOrThrow(commentId: Long): Comment {
        return commentRepository.findById(commentId).orElseThrow { CommentException(ExceptionCode.NOT_FOUND_COMMENT) }
    }

    fun findAllByBoardId(boardId: Long, pageable: Pageable): Page<Comment> {
        return commentRepository.findAllByBoard_BoardId(boardId, pageable)
    }

    fun getCommentsByBoardId(boardId: Long, pageable: Pageable): Page<CommentResponseDto> {
        val comments = findAllByBoardId(boardId, pageable)
        return comments.map { CommentResponseDto.from(it) }
    }
}