package org.prgrms.devconnect.domain.interest.repository

import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query


interface InterestBoardRepository : JpaRepository<InterestBoard, Long> {

  @Query("SELECT ib FROM InterestBoard ib left join fetch ib.board WHERE ib.member = :member")
  fun findAllByMemberWithBoard(member: Member): List<InterestBoard>

  @Query("select ib from InterestBoard ib where ib.member.memberId =:memberId and ib.board.boardId = :boardId")
  fun findByMemberIdAndBoardId(memberId: Long, boardId: Long): InterestBoard?

  fun existsByMemberAndBoard(member: Member, board: Board): Boolean
}