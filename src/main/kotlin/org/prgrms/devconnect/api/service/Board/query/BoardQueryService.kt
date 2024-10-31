package org.prgrms.devconnect.api.service.Board.query

import org.prgrms.devconnect.api.controller.board.dto.request.BoardFilterDto
import org.prgrms.devconnect.api.controller.board.dto.response.BoardResponseDto
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.board.BoardException
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import org.prgrms.devconnect.domain.board.repository.BoardRepository
import org.prgrms.devconnect.domain.board.repository.CommentRepository
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class BoardQueryService (
    private val boardRepository: BoardRepository ,
    //TODO 주석 삭제
//    private val memberRepository: MemberRepository,
//    private val jobPostRepository: JobPostRepository
) {

    fun getBoardByIdOrThrow(boardId: Long): Board {
        return boardRepository.findByIdAndStatusNotDeleted(boardId)
            .orElseThrow { BoardException(ExceptionCode.NOT_FOUND_BOARD) }
    }

    fun getBoardById(boardId: Long): BoardResponseDto {
        val board = getBoardByIdOrThrow(boardId)
        return BoardResponseDto.from(board)
    }

    fun getAllBoards(pageable: Pageable): Page<BoardResponseDto> {
        val boards: Page<Board> = boardRepository.findAllWithTechStackByStatusNotDeleted(pageable)
        return boards.map { BoardResponseDto.from(it) }
    }

    fun findAllByEndDateAndStatus(): List<Board> {
        return boardRepository.findAllByEndDateAndStatus(LocalDateTime.now(), BoardStatus.RECRUITING)
    }

    fun getBoardsByFilter(
        category: BoardCategory?,
        status: BoardStatus?,
        techStackIds: List<Long>?,
        progressWay: ProgressWay?,
        pageable: Pageable
    ): Page<BoardResponseDto>? {
        val filterDto = BoardFilterDto(
            category = category,
            status = status,
            techStackIds = techStackIds ?: emptyList(),
            progressWay = progressWay
        )
        return if (filterDto.isEmpty()) {
            getAllBoards(pageable)
        } else {
            val filteredBoards = boardRepository.findByFilter(filterDto, pageable)
            filteredBoards?.map { BoardResponseDto.from(it) }
        }
    }

    fun getTop10PopularBoardsThisWeek(): List<BoardResponseDto>? {
        val startOfWeek = LocalDateTime.now()
            .with(DayOfWeek.MONDAY)
            .toLocalDate()
            .atStartOfDay()
        val endOfWeek = LocalDateTime.now()
            .with(DayOfWeek.SUNDAY)
            .toLocalDate()
            .atTime(23, 59, 59)
        val boards = boardRepository.findTop10PopularBoardsThisWeek(startOfWeek, endOfWeek)
        return boards?.map { BoardResponseDto.from(it) }
    }

    //TODO 주석 삭제
//    fun getBoardsByMemberInterests(memberId: Long): List<BoardResponseDto> {
//        val memberTechStacks = memberQueryService.getTechStacksByMemberId(memberId)
//        val boards = boardRepository.findAllByTechStacks(memberTechStacks)
//        return boards.map { BoardResponseDto.from(it) }
//    }

    //TODO 주석 삭제
//    fun getBoardsByJobPostId(jobPostId: Long): List<BoardResponseDto> {
//        jobPostQueryService.getJobPostByIdOrThrow(jobPostId)
//        val boards = boardRepository.findAllByJobPostId(jobPostId)
//        return boards.map { BoardResponseDto.from(it) }
//    }

    fun getBoardsWithPopularTagCondition(): List<BoardResponseDto> {
        val startOfWeek = LocalDateTime.now().with(DayOfWeek.MONDAY).toLocalDate().atStartOfDay()
        val endOfWeek = LocalDateTime.now().with(DayOfWeek.SUNDAY).toLocalDate().atTime(23, 59, 59)
        val boards = boardRepository.findBoardsWithPopularTagCondition(startOfWeek, endOfWeek)
        return boards.map { BoardResponseDto.from(it) }
    }

    fun getBoardsWithDeadlineApproaching(): List<BoardResponseDto> {
        val currentDate = LocalDateTime.now()
        val deadlineDate = currentDate.plusDays(2)
        val boards = boardRepository.findBoardsWithDeadlineApproaching(currentDate, deadlineDate)
        return boards.map { BoardResponseDto.from(it) }
    }
}