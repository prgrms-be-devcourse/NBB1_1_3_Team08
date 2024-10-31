package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.controller.board.dto.request.BoardUpdateRequestDto
import org.prgrms.devconnect.api.service.Board.query.BoardQueryService
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.board.BoardException
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.BoardTechStackMapping
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.repository.BoardTechStackMappingRepository
import org.prgrms.devconnect.domain.techstack.repository.TechStackRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardUpdateService(
    private val boardQueryService: BoardQueryService,
    private val techStackRepository: TechStackRepository,
    private val boardTechStackMappingRepository: BoardTechStackMappingRepository,
) {

    // 게시판 수정 메서드
    fun updateBoard(boardId: Long, requestDto: BoardUpdateRequestDto): Long {
        val board = boardQueryService.getBoardByIdOrThrow(boardId)

        println("boardId = ${boardId}")

        if (board.isDeleted()) {
            throw BoardException(ExceptionCode.ALREADY_DELETED_BOARD)
        }
        board.updateFromDto(requestDto)

        // 삭제할 TechStack 처리
        val deleteTechIds = requestDto.deleteTechStacks
        if (deleteTechIds != null) {
            deleteTechStacksFromBoard(board, deleteTechIds)
        }

        // 추가할 TechStack 처리
        val addTechIds = requestDto.addTechStacks
        if (addTechIds != null) {
            addTechStacksFromBoard(board, addTechIds)
        }

        return board.boardId!!
    }

    // 기술 스택 추가 메서드
    private fun addTechStacksFromBoard(board: Board, addTechIds: List<Long>) {
        val techStacks = techStackRepository.findAllByTechStackIdIn(addTechIds)

        if (techStacks.isNotEmpty()) {
            val mappingToSave = techStacks.map { techStack ->
                BoardTechStackMapping(techStack = techStack)
            }

            mappingToSave.forEach { it.assignBoard(board) }
            boardTechStackMappingRepository.saveAll(mappingToSave)
        }
    }

    // 기술 스택 삭제 메서드
    private fun deleteTechStacksFromBoard(board: Board, deleteTechIds: List<Long>) {
        val idsToDelete = boardTechStackMappingRepository
            .findAllByBoard_BoardIdAndTechStack_TechStackIdIn(board.boardId!!, deleteTechIds)
            .map { it.id }

        boardTechStackMappingRepository.deleteAllByIds(idsToDelete)
    }

    // 게시물 좋아요 증가
    fun likeBoard(boardId: Long) {
        val board = boardQueryService.getBoardByIdOrThrow(boardId)
        board.increaseLikes()
    }

    // 게시물 좋아요 감소
    fun unlikeBoard(boardId: Long) {
        val board = boardQueryService.getBoardByIdOrThrow(boardId)
        board.decreaseLikes()
    }

    // 조회수 증가
    fun increaseViews(boardId: Long) {
        val board = boardQueryService.getBoardByIdOrThrow(boardId)
        board.increaseViews()
    }


    // 게시물 종료 메서드
    fun closeBoardManually(boardId: Long) {
        val board = boardQueryService.getBoardByIdOrThrow(boardId)
        if (board.isClosed()) {
            throw BoardException(ExceptionCode.ALREADY_CLOSED_BOARD)
        }
        if (board.isDeleted()) {
            throw BoardException(ExceptionCode.NOT_FOUND_BOARD)
        }
        board.changeStatus(BoardStatus.CLOSED)
    }
}