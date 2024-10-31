package org.prgrms.devconnect.api.service.Board.command

import org.prgrms.devconnect.api.controller.board.dto.request.BoardCreateRequestDto
import org.prgrms.devconnect.api.service.jobpost.JobPostQueryService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.api.service.techstack.TechStackQueryService
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.BoardTechStackMapping
import org.prgrms.devconnect.domain.board.repository.BoardRepository
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.techstack.entity.TechStack
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class BoardCreateService(
    private val boardRepository: BoardRepository,
    private val memberQueryService: MemberQueryService,
    private val jobPostQueryService: JobPostQueryService,
    private val techStackQueryService: TechStackQueryService,
) {

    fun createBoard(boardCreateRequestDto: BoardCreateRequestDto, memberId: Long): Long? {
        val member: Member = memberQueryService.getMemberByIdOrThrow(memberId)

        var jobPost: JobPost? = null
        if (boardCreateRequestDto.jobPostId != null) {
            jobPost = jobPostQueryService.getJobPostByIdOrThrow(boardCreateRequestDto.jobPostId)
        }

        val board = Board(
            member = member,
            jobPost = jobPost,
            title = boardCreateRequestDto.title,
            content = boardCreateRequestDto.content,
            category = boardCreateRequestDto.category,
            recruitNum = boardCreateRequestDto.recruitNum,
            progressWay = boardCreateRequestDto.progressWay,
            progressPeriod = boardCreateRequestDto.progressPeriod,
            endDate = boardCreateRequestDto.endDate,
        )

        for (teckId in boardCreateRequestDto.techStackRequests) {
            val teckStack:TechStack = techStackQueryService.getTechStackByIdOrThrow(teckId.techStackId)
            val boardTechStack = BoardTechStackMapping(
                techStack = teckStack
            )
            board.addTechStack(boardTechStack)
        }

        boardRepository.save(board)

        return board.boardId
    }
}