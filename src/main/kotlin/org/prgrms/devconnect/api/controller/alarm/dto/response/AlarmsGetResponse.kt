package org.prgrms.devconnect.api.controller.alarm.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.domain.alarm.entity.Alarm

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "전체 알림 응답 정보")
data class AlarmsGetResponse(
    @field:Schema(
        description = "전체 알림의 개수",
        example = "123"
    ) @param:Schema(description = "전체 알림의 개수", example = "123") val count: Int,
    @field:Schema(description = "여러 개의 단일 알림 응답 정보") @param:Schema(
        description = "여러 개의 단일 알림 응답 정보"
    ) val alarms: List<AlarmGetResponse>
) {
    companion object {
        fun from(alarms: List<Alarm>): AlarmsGetResponse {
            return AlarmsGetResponse(
                count = alarms.size,
                alarms = (
                        alarms.stream()
                            .map { alarm -> AlarmGetResponse.from(alarm) }
                            .toList()
                        )
            )
        }
    }
}