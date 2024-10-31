package org.prgrms.devconnect.domain.board.repository.custom

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.prgrms.devconnect.api.controller.board.dto.request.BoardFilterDto
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.QBoard
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime


@Repository
class BoardRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : BoardRepositoryCustom {

    override fun findTop10PopularBoardsThisWeek(startOfWeek: LocalDateTime, endOfWeek: LocalDateTime): List<Board> {
        val board = QBoard.board
        return queryFactory
            .selectFrom(board)
            .where(
                board.status.eq(BoardStatus.RECRUITING)
                    .and(board.createdAt.between(startOfWeek, endOfWeek))
            )
            .orderBy(board.views.desc())
            .limit(10)
            .fetch()
    }

    override fun findByFilter(filterDto: BoardFilterDto, pageable: Pageable): Page<Board> {
        val board = QBoard.board
        val query: JPAQuery<Board> = queryFactory
            .selectFrom(board)
            .where(
                board.status.ne(BoardStatus.DELETED),
                filterByCategory(filterDto.category),
                filterByStatus(filterDto.status),
                filterByTechStack(filterDto.techStackIds),
                filterByProgressWay(filterDto.progressWay)
            )

        return PageableExecutionUtils.getPage(query.fetch(), pageable) { query.fetchCount() }
    }

    private fun filterByCategory(category: BoardCategory?): BooleanExpression? {
        return category?.let { QBoard.board.category.eq(it) }
    }

    private fun filterByStatus(status: BoardStatus?): BooleanExpression? {
        return status?.let { QBoard.board.status.eq(it) }
    }

    private fun filterByTechStack(techStackIds: List<Long>?): BooleanExpression? {
        return if (techStackIds.isNullOrEmpty()) {
            null
        } else {
            QBoard.board.boardTechStacks.any().techStack.techStackId.`in`(techStackIds)
        }
    }

    private fun filterByProgressWay(progressWay: ProgressWay?): BooleanExpression? {
        return progressWay?.let { QBoard.board.progressWay.eq(it) }
    }
}