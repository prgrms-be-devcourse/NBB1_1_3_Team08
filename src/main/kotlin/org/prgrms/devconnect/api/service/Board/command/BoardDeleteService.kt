package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.service.Board.query.BoardQueryService
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardDeleteService(
    private val boardQueryService: BoardQueryService,
) {

    fun deleteBoard(boardId: Long) {
        val board = boardQueryService.getBoardByIdOrThrow(boardId)
        board.changeStatus(BoardStatus.DELETED)
    }
}