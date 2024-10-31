package org.prgrms.devconnect.domain.alarm.event

import org.prgrms.devconnect.api.service.alarm.AlarmCommandService
import org.prgrms.devconnect.api.service.alarm.EmailService
import org.prgrms.devconnect.domain.alarm.entity.Alarm
import org.prgrms.devconnect.domain.alarm.event.child.CommentEvent
import org.prgrms.devconnect.domain.alarm.event.child.ReplyCommentEvent
import org.prgrms.devconnect.domain.alarm.event.child.WelcomeEvent
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component


@Component
class EventHandler(
    private val alarmService: AlarmCommandService,
    private val emailService: EmailService,

    ) {

    @Async
    @EventListener
    fun sendWelcomeMessage(event: WelcomeEvent) {
        val alarm: Alarm = alarmService.createWelcomeAlarmWhenSignIn(event.member)
        emailService.sendEmail(alarm)
    }

    @Async
    @EventListener
    fun sendCommentPostedOnBoardToBoardPoster(event: CommentEvent) {
        val alarm: Alarm = alarmService.createCommentPostedMessageToBoardPoster(event.comment)
        emailService.sendEmail(alarm)
    }

    @Async
    @EventListener
    fun sendRegisteredReplyCommentMessageToParentCommenter(event: ReplyCommentEvent) {
        val alarm: Alarm = alarmService.createReplyCommentReceivedAlarmToParentCommenter(event.comment)
        emailService.sendEmail(alarm)
    }

//    @Async
//    @EventListener
//    fun sendBoardUrgentAlarm(event: UrgentBoardEvent) {
//        val alarm: Alarm = alarmService.createUrgentAlarmAboutInterestBoard(event.interestBoard())
//        emailService.sendEmail(alarm)
//    }
}
