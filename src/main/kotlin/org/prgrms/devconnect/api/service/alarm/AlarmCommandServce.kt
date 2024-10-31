package org.prgrms.devconnect.api.service.alarm

import jakarta.transaction.Transactional
import org.prgrms.devconnect.domain.alarm.entity.Alarm
import org.prgrms.devconnect.domain.alarm.repository.AlarmRepository
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.Comment
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
@Transactional
class AlarmCommandService(
    private val alarmRepository: AlarmRepository,
    private val alarmQueryService: AlarmQueryService
) {

    fun createWelcomeAlarmWhenSignIn(member: Member): Alarm {
        //TODO: 프론트 메인페이지 url 추가

        val mainPage = ""
        val welcomeMessage: String =
            (member.nickname + "님, 만나서 반가워요👋\n 1만명의 DevConnector들이 " + member.nickname).toString() + "님에 대해 알고싶대요!"

        val alarm: Alarm = Alarm(
            alertText = welcomeMessage,
            relatedUrl = mainPage,
            member = member
        )

        alarmRepository.save(alarm)

        return alarm
    }

    fun deleteAlarmByAlarmIdAndMemberId(alarmId: Long, memberId: Long?) {
        alarmQueryService.getAlarmByAlarmIdAndMemberIdOrThrow(alarmId, memberId)
        alarmRepository.deleteByAlarmIdAndMemberMemberId(alarmId, memberId!!)
    }

    fun deleteAlarmsByMemberId(memberId: Long?) {
        alarmQueryService.getAlarmsByMemberIdOrThrow(memberId)
        alarmRepository.deleteAllByMemberMemberId(memberId!!)
    }

    fun createCommentPostedMessageToBoardPoster(comment: Comment): Alarm {
        val postedBoard: Board = comment.board
        val boardedPoster: Member = postedBoard.member!!
        val commenter: Member = comment.member!!

        val linkedPage = ""
        val commentPostedMessage: String = boardedPoster.nickname + "님이 포스팅 한 " + postedBoard.title + "에 " + commenter.nickname + "님이 댓글을 남겼습니다!"

        val alarm: Alarm = Alarm(
            alertText = commentPostedMessage,
            relatedUrl = linkedPage,
            member = boardedPoster
        )

        alarmRepository.save(alarm)

        return alarm
    }

    fun createReplyCommentReceivedAlarmToParentCommenter(comment: Comment): Alarm {
        val parentCommenter: Member = comment.parent?.member!!
        val replier: Member = comment.member!!

        val likedPage = ""
        val replyMessage: String =
            parentCommenter.nickname + "님이 작성한 댓글 \"" + comment.parent!!.content + "\"에 답글이 달렸어요!"

        val alarm = Alarm(
            alertText = replyMessage,
            relatedUrl = likedPage,
            member = replier
        )

        alarmRepository.save(alarm)

        return alarm
    }

    fun createUrgentAlarmAboutInterestBoard(interestBoard: InterestBoard): Alarm {
        val member: Member = interestBoard.member
        val board: Board = interestBoard.board
        val remainingDate = LocalDate.now().until(board.endDate.toLocalDate(), ChronoUnit.DAYS)
        val likedPage = ""
        val urgentMessage: String =
            member.nickname + "님이 관심 표시한 " + board.title + "의 마감 기한까지" + remainingDate + "일 남았습니다. 얼른 지원해보세요!"

        val alarm: Alarm = Alarm(
            member = member,
            alertText = urgentMessage,
            relatedUrl = likedPage
        )

        alarmRepository.save(alarm)

        return alarm
    }
}