package org.prgrms.devconnect.domain.board.repository

import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.repository.custom.BoardRepositoryCustom
import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface BoardRepository : JpaRepository<Board, Long>, BoardRepositoryCustom {
    fun findAllByEndDateBeforeAndStatus(currentDate: LocalDateTime, status: BoardStatus) : List<Board>

    @Query("SELECT b FROM Board b LEFT JOIN FETCH b.boardTechStacks ts LEFT JOIN FETCH ts.techStack WHERE b.status != 'DELETED'")
    fun findAllWithTechStackByStatusNotDeleted(pageable: Pageable): Page<Board>

    fun findByBoardIdAndStatusNot(boardId: Long, status: BoardStatus = BoardStatus.DELETED) : Optional<Board>

    @Query("SELECT b FROM Board b WHERE b.jobPost.jobPostId = :jobPostId AND b.status != 'DELETED'")
    fun findAllByJobPostId(jobPostId: Long): List<Board>

    @Query("SELECT b FROM Board b WHERE b.createdAt BETWEEN :startOfWeek AND :endOfWeek AND b.views >= 500 AND b.status != 'DELETED'")
    fun findBoardsWithPopularTagCondition(startOfWeek: LocalDateTime, endOfWeek: LocalDateTime): List<Board>

    @Query("SELECT b FROM Board b WHERE b.endDate BETWEEN :currentDate AND :deadlineDate AND b.status != 'DELETED'")
    fun findBoardsWithDeadlineApproaching(currentDate: LocalDateTime, deadlineDate: LocalDateTime): List<Board>

    @Query("SELECT DISTINCT b FROM Board b JOIN b.boardTechStacks ts JOIN ts.techStack t WHERE t IN :techStacks AND b.status = 'RECRUITING'")
    fun findAllByTechStacks(techStacks: List<TechStack>): List<Board>
}