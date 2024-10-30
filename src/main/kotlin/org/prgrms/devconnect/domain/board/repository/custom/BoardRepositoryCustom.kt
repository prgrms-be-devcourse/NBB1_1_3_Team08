package org.prgrms.devconnect.domain.board.repository.custom

import org.prgrms.devconnect.api.controller.board.dto.request.BoardFilterDto
import org.prgrms.devconnect.domain.board.entity.Board
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.time.LocalDateTime

interface BoardRepositoryCustom {
    fun findTop10PopularBoardsThisWeek(startOfWeek: LocalDateTime, endOfWeek : LocalDateTime) : List<Board>?

    fun findByFilter(filterDto:BoardFilterDto, pageable: Pageable) : Page<Board>?
}