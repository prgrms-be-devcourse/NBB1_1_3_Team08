package org.prgrms.devconnect.domain.alarm.aop

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.alarm.AlarmException
import org.prgrms.devconnect.domain.alarm.event.child.CommentEvent
import org.prgrms.devconnect.domain.alarm.event.child.ReplyCommentEvent
import org.prgrms.devconnect.domain.alarm.event.child.UrgentBoardEvent
import org.prgrms.devconnect.domain.alarm.event.child.WelcomeEvent
import org.prgrms.devconnect.domain.board.entity.Comment
import org.prgrms.devconnect.domain.interest.entity.InterestBoard
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component

@Aspect
@Component
class AlarmHandler(
    private val publisher: ApplicationEventPublisher,
) {
    @Around("@annotation(org.prgrms.devconnect.domain.alarm.aop.RegisterAlarmPublisher)")
    fun processRegisterPublisherAnnotation(proceedingJoinPoint: ProceedingJoinPoint): Any {

        val result = proceedingJoinPoint.proceed()
        val methodName = proceedingJoinPoint.signature.name

        // TODO: 팩토리 메서드 패턴으로 리펙토링
        if ("findAllUrgentBoards" == methodName) {
            for (interestBoard in result as List<InterestBoard>) {
                publisher.publishEvent(UrgentBoardEvent(interestBoard))
            }
        } else if ("createComment" == methodName) {
            val comment: Comment = result as Comment
            publisher.publishEvent(CommentEvent(comment))
            if (!comment.isRootComment()) {
                publisher.publishEvent(ReplyCommentEvent(comment))
            }
        } else if ("createMember" == methodName) {
            publisher.publishEvent(WelcomeEvent(result as Member))
        }
        else throw AlarmException(ExceptionCode.NOT_FOUND_ALARM)
        return result
    }
}