package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.service.Board.query.BoardQueryService
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardScheduleService (
    private val boardQueryService: BoardQueryService
) {
    @Scheduled(cron = "0 0 0 * * *")
    fun scheduleAutoClose() {
        closeExpiredBoardAutomatically()
    }

    fun closeExpiredBoardAutomatically() {
        val expiredBoards = boardQueryService.findAllByEndDateAndStatus()
        for (board in expiredBoards) {
            board.changeStatus(BoardStatus.CLOSED)
        }
    }
}