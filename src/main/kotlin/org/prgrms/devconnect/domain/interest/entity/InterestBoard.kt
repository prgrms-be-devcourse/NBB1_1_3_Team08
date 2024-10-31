package org.prgrms.devconnect.domain.interest.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.member.entity.Member
import java.time.LocalDate


@Entity
@Table(name = "interest_board")
class InterestBoard(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "interest_board_id")
  val id: Long? = null,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", nullable = false)
  val member: Member,

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false)
  val board: Board
) {

  fun isUrgent(): Boolean {
    val today = LocalDate.now()
    val endDate = board.endDate.toLocalDate()

    // 종료 날짜가 이미 지났으면 false
    if (endDate.isBefore(today)) {
      return false
    }
    return endDate.isBefore(today.plusDays(3L))
  }

}