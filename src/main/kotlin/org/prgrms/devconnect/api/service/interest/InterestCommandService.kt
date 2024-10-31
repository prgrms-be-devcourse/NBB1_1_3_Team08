package org.prgrms.devconnect.api.service.interest

import org.prgrms.devconnect.api.controller.interest.dto.request.InterestBoardRequestDto
import org.prgrms.devconnect.api.controller.interest.dto.request.InterestJobPostRequestDto
import org.prgrms.devconnect.api.service.Board.command.BoardUpdateService
import org.prgrms.devconnect.api.service.Board.query.BoardQueryService
import org.prgrms.devconnect.api.service.jobpost.JobPostCommandService
import org.prgrms.devconnect.api.service.jobpost.JobPostQueryService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.interest.entity.InterestJobPost
import org.prgrms.devconnect.domain.interest.repository.InterestBoardRepository
import org.prgrms.devconnect.domain.interest.repository.InterestJobPostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Transactional
@Service
class InterestCommandService(
  private val interestBoardRepository: InterestBoardRepository,
  private val interestJobPostRepository: InterestJobPostRepository,
  private val memberQueryService: MemberQueryService,
  private val boardQueryService: BoardQueryService,
  private val boardUpdateService: BoardUpdateService,
  private val jobPostQueryService: JobPostQueryService,
  private val jobPostCommandService: JobPostCommandService,
  private val interestQueryService: InterestQueryService
) {

  fun addInterestBoard(requestDto: InterestBoardRequestDto, memberId: Long) {
    val member = memberQueryService.getMemberByIdOrThrow(memberId)
    val board = boardQueryService.getBoardByIdOrThrow(requestDto.boardId)

    interestQueryService.validateDuplicatedInterestBoard(member, board)

    // 좋아요 증가
    boardUpdateService.likeBoard(board.boardId!!)

    val interestBoard: InterestBoard = requestDto.toEntity(member, board)
    interestBoardRepository.save(interestBoard)
  }

  fun removeInterestBoard(memberId: Long, boardId: Long) {
    val interestBoard = interestQueryService.getInterestBoardByMemberIdAndBoardIdOrThrow(
      memberId, boardId
    )
    
    // 좋아요 감소
    boardUpdateService.unlikeBoard(boardId)

    interestBoardRepository.delete(interestBoard)
  }

  fun addInterestJobPost(requestDto: InterestJobPostRequestDto, memberId: Long) {
    val member = memberQueryService.getMemberByIdOrThrow(memberId)
    val jobPost = jobPostQueryService.getJobPostByIdOrThrow(requestDto.jobPostId)

    interestQueryService.validateDuplicatedInterestJobPost(member, jobPost)

    // 좋아요 증가
    jobPostCommandService.likeJobPost(jobPost.jobPostId!!)

    val interestJobPost: InterestJobPost = requestDto.toEntity(member, jobPost)
    interestJobPostRepository.save(interestJobPost)
  }

  fun removeInterestJobPost(memberId: Long, jobPostId: Long) {
    val interestJobPost = interestQueryService
      .getInterestJobPostByMemberIdAndJobPostIdOrThrow(memberId, jobPostId)

    // 좋아요 감소
    jobPostCommandService.unlikeJobPost(jobPostId)

    interestJobPostRepository.delete(interestJobPost)
  }
}