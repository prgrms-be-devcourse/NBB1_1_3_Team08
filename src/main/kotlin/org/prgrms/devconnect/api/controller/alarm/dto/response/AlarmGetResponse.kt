package org.prgrms.devconnect.api.controller.alarm.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.domain.alarm.entity.Alarm
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "단일 알림 응답 정보")
data class AlarmGetResponse(
    @field:Schema(
        description = "알림 수신자 닉네임",
        example = "홍길동"
    ) @param:Schema(
        description = "알림 수신자 닉네임",
        example = "홍길동"
    ) val sender: String,

    @field:Schema(
        description = "알림 생성 시각",
        example = "2024-00-00T00:00:00"
    ) @param:Schema(
        description = "알림 생성 시각",
        example = "2024-00-00T00:00:00"
    ) val createdTime: LocalDateTime?,

    @field:Schema(
        description = "알림 메시지",
        example = "알림 메시지가 도착했어요!"
    ) @param:Schema(
        description = "알림 메시지",
        example = "알림 메시지가 도착했어요!"
    ) val content: String,

    @field:Schema(
        description = "읽음표시: true - 읽음, false - 읽지않음",
        example = "true"
    ) @param:Schema(
        description = "읽음표시: true - 읽음, false - 읽지않음",
        example = "true"
    ) val isRead: Boolean
) {
    companion object {
        fun from(alarm: Alarm): AlarmGetResponse {
            return  AlarmGetResponse(
                sender = alarm.member.nickname,
                createdTime = alarm.createdAt,
                content = alarm.alertText,
                isRead = alarm.isRead
            )
        }
    }
}
