package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.controller.board.dto.request.BoardCreateRequestDto
import org.prgrms.devconnect.api.service.jobpost.JobPostQueryService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.api.service.techstack.TechStackQueryService
import org.prgrms.devconnect.domain.board.entity.BoardTechStackMapping
import org.prgrms.devconnect.domain.board.repository.BoardRepository
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardCreateService (
    private val boardRepository: BoardRepository,
    private val memberQueryService: MemberQueryService,
    private val jobPostQueryService: JobPostQueryService,
    private val techStackQueryService: TechStackQueryService,
) {

    fun createBoard(boardCreateRequestDto: BoardCreateRequestDto, memberId: Long): Long {
        val member = memberQueryService.getMemberByIdOrThrow(memberId)

        var jobPost: JobPost? = null
        if (boardCreateRequestDto.jobPostId != null) {
            jobPost = jobPostQueryService.getJobPostByIdOrThrow(boardCreateRequestDto.jobPostId)
        }

        val boardTechStackMappings = createBoardTechStackMappings(boardCreateRequestDto)

        val board = boardCreateRequestDto.toEntity(member, jobPost, boardTechStackMappings)
        boardRepository.save(board)

        return board.boardId!!
    }


    private fun createBoardTechStackMappings(boardCreateRequestDto: BoardCreateRequestDto): List<BoardTechStackMapping> {
        val techStackIds = boardCreateRequestDto.techStackRequests.map { it.techStackId }

        val techStacks = techStackQueryService.getTechStacksByIdsOrThrow(techStackIds)

        return techStacks.map { techStack ->
            BoardTechStackMapping(techStack = techStack)
        }
    }
}