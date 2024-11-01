package org.prgrms.devconnect.api.controller.alarm

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.prgrms.devconnect.api.controller.alarm.dto.response.AlarmsGetResponse
import org.prgrms.devconnect.api.service.alarm.AlarmCommandService
import org.prgrms.devconnect.api.service.alarm.AlarmQueryService
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "알림 API", description = "알림 관련 기능을 제공하는 API")
@RestController
@RequestMapping("/api/v1/alarms")
class AlarmController(
    private val alarmQueryService: AlarmQueryService,
    private val alarmCommandService: AlarmCommandService

) {
    @Operation(summary = "알림 전체 조회", description = "사용자 아이디별 수신된 모든 알림을 반환합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "수신된 모든 알림을 성공적으로 반환합니다.",
        content = [Content(schema = Schema(implementation = AlarmsGetResponse::class))]
    )
    @GetMapping
    fun getAlarms(@AuthenticationPrincipal member: Member): ResponseEntity<AlarmsGetResponse> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(alarmQueryService.getAlarmsByMemberIdOrThrow(member.memberId!!))
    }

    @Operation(summary = "알림 전체 삭제", description = "사용자 아이디별 수신된 모든 알림을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "수신된 전체 알림 목록을 성공적으로 삭제했습니다.")
    @DeleteMapping
    fun deleteAlarmsByMemberId(@AuthenticationPrincipal member: Member): ResponseEntity<Void> {
        alarmCommandService.deleteAlarmsByMemberId(member.memberId!!)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build()
    }

    @Operation(summary = "알림 단일 삭제", description = "특정 알림 id를 가진 알림을 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "특정 알림 id를 가진 알림을 성공적으로 삭제했습니다.")
    @DeleteMapping("/{alarmId}")
    fun deleteAlarm(
        @AuthenticationPrincipal member: Member,
        @PathVariable alarmId: Long
    ): ResponseEntity<Void> {
        alarmCommandService.deleteAlarmByAlarmIdAndMemberId(alarmId, member.memberId!!)
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .build()
    }


    @Operation(summary = "읽지 않은 알림 수 조회", description = "읽지 않은 알림의 수를 조회합니다.")
    @ApiResponse(
        responseCode = "200",
        description = "읽지 않은 알림의 수를 성공적으로 조회했습니다.",
        content = [Content(schema = Schema(implementation = Int::class))]
    )
    @GetMapping("/counts")
    fun getUnReadAlarmsCount(@AuthenticationPrincipal member: Member): ResponseEntity<Int> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(alarmQueryService.getUnReadAlarmsCountByMemberId(member.memberId!!))
    }
}
