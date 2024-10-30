package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.controller.board.dto.request.CommentCreateRequestDto
import org.prgrms.devconnect.api.service.Board.query.BoardQueryService
import org.prgrms.devconnect.api.service.Board.query.CommentQueryService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.board.CommentException
import org.prgrms.devconnect.domain.board.entity.Comment
import org.prgrms.devconnect.domain.board.repository.CommentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommentCreateService(
    private val commentRepository: CommentRepository,
    private val memberQueryService: MemberQueryService,
    private val boardQueryService: BoardQueryService,
    private val commentQueryService: CommentQueryService
) {

    // TODO 허은정 Alarm Regist Event AOP
//    @RegisterPublisher
    fun createComment(commentCreateRequestDto: CommentCreateRequestDto, memberId: Long?): Comment {
        val member = memberQueryService.getMemberByIdOrThrow(memberId)
        val board = boardQueryService.getBoardByIdOrThrow(commentCreateRequestDto.boardId)

        var parentComment: Comment? = null
        commentCreateRequestDto.parentId?.let { parentId ->
            parentComment = commentQueryService.getCommentByIdOrThrow(parentId)
            if (parentComment != null && !parentComment!!.isRootComment()) {
                throw CommentException(ExceptionCode.INVALID_PARENT_COMMENT)
            }
        }

        val comment = commentCreateRequestDto.toEntity(member, board, parentComment)
        commentRepository.save(comment)

        return comment
    }
}