package org.prgrms.devconnect.api.service.interest

import org.prgrms.devconnect.api.controller.interest.dto.response.InterestResponseDto
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.common.exception.ExceptionCode.*
import org.prgrms.devconnect.common.exception.interest.InterestException
import org.prgrms.devconnect.domain.alarm.aop.RegisterAlarmPublisher
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.interest.entity.InterestJobPost
import org.prgrms.devconnect.domain.interest.repository.InterestBoardRepository
import org.prgrms.devconnect.domain.interest.repository.InterestJobPostRepository
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors


@Transactional(readOnly = true)
@Service
class InterestQueryService(
  private val interestBoardRepository: InterestBoardRepository,
  private val interestJobPostRepository: InterestJobPostRepository,
  private val memberQueryService: MemberQueryService,
) {

  fun getInterestsByMemberId(memberId: Long): InterestResponseDto {
    val member = memberQueryService.getMemberByIdOrThrow(memberId)
    val interestBoards = interestBoardRepository.findAllByMemberWithBoard(member)
    val interestJobPosts = interestJobPostRepository.findAllByMemberWithJobPost(member)

    return InterestResponseDto.from(interestBoards, interestJobPosts)
  }

  fun getInterestBoardByMemberIdAndBoardIdOrThrow(memberId: Long, boardId: Long): InterestBoard {
    return interestBoardRepository.findByMemberIdAndBoardId(memberId, boardId)
      ?: throw InterestException(NOT_FOUND_INTEREST_BOARD)
  }

  fun getInterestJobPostByMemberIdAndJobPostIdOrThrow(
    memberId: Long,
    jobPostId: Long
  ): InterestJobPost {
    return interestJobPostRepository.findByMemberIdAndJobPostId(memberId, jobPostId)
      ?: throw InterestException(NOT_FOUND_INTEREST_JOB_POST)
  }

  fun validateDuplicatedInterestBoard(member: Member, board: Board) {
    if (interestBoardRepository.existsByMemberAndBoard(member, board)) {
      throw InterestException(DUPLICATED_INTEREST_BOARD)
    }
  }

  fun validateDuplicatedInterestJobPost(member: Member, jobPost: JobPost) {
    if (interestJobPostRepository.existsByMemberAndJobPost(member, jobPost)) {
      throw InterestException(DUPLICATED_INTEREST_JOB_POST)
    }
  }

  @Scheduled(cron = "0 0 0 * * *")
  @RegisterAlarmPublisher
  fun findAllUrgentBoards(): List<InterestBoard> {
    val allInterestBoards = interestBoardRepository.findAll()
    val urgentBoards = allInterestBoards.stream()
      .filter { obj: InterestBoard -> obj.isUrgent() }
      .collect(Collectors.toList())
    return urgentBoards
  }
}
