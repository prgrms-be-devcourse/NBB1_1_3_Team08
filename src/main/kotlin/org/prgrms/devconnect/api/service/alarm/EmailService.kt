package org.prgrms.devconnect.api.service.alarm

import groovy.util.logging.Slf4j
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.alarm.EmailException
import org.prgrms.devconnect.domain.alarm.entity.Alarm
import org.slf4j.LoggerFactory
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.thymeleaf.context.Context
import org.thymeleaf.spring6.SpringTemplateEngine


@Service
@Slf4j
@Transactional(readOnly = true)
class EmailService(
    private val javaMailSender: JavaMailSender,
    private val templateEngine: SpringTemplateEngine
) {
    @Async
    fun sendEmail(alarm: Alarm) {
        val logger  = LoggerFactory.getLogger(EmailService::class.java)

        val mimeMessage = javaMailSender.createMimeMessage()
        try {
            val mimeMessageHelper = MimeMessageHelper(mimeMessage, false, "UTF-8")
            mimeMessageHelper.setTo(alarm.member.email) // 메일 수신자
            mimeMessageHelper.setSubject("오늘의 DevConnect에서 온 알림") // 메일 제목
            mimeMessageHelper.setText(setContext(alarm), true) // 메일 본문 내용, HTML 여부
            javaMailSender.send(mimeMessage)
            logger.info("이메일 발송 완료")
        } catch (e: Exception) {
            logger.error("이메일 발송 실패"+ e.message)
            throw EmailException(ExceptionCode.EMAIL_SERVER_ERROR)
        }
    }

    //thymeleaf를 통한 html 적용
    fun setContext(alarm: Alarm): String {
        val context = Context()
        context.setVariable("alertText", alarm.alertText)
        context.setVariable("relatedUrl", alarm.relatedUrl)
        return templateEngine.process("emailForm", context)
    }
}