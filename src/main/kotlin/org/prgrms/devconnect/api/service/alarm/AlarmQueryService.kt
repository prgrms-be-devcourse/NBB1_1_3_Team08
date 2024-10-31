package org.prgrms.devconnect.api.service.alarm

import org.prgrms.devconnect.api.controller.alarm.dto.response.AlarmsGetResponse
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.alarm.AlarmException
import org.prgrms.devconnect.domain.alarm.entity.Alarm
import org.prgrms.devconnect.domain.alarm.repository.AlarmRepository
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class AlarmQueryService(
    private val alarmRepository: AlarmRepository
) {

    private val memberQueryService: MemberQueryService? = null

    fun getAlarmsByMemberIdOrThrow(memberId: Long?): AlarmsGetResponse {
        val member: Member = memberQueryService!!.getMemberByIdOrThrow(memberId!!)
        val alarms: List<Alarm> = alarmRepository.findAllByMember(member)
        if (alarms.isEmpty()) {
            throw AlarmException(ExceptionCode.EMPTY_ALARMS)
        }
        alarms.forEach(Alarm::updateAlarmStatusToRead)
        return AlarmsGetResponse.from(alarms)
    }

    fun getAlarmByAlarmIdAndMemberIdOrThrow(alarmId: Long?, memberId: Long?): Alarm {
        return alarmRepository.findByAlarmIdAndMemberMemberId(alarmId, memberId).orElseThrow {
            AlarmException(
                ExceptionCode.NOT_FOUND_ALARM
            )
        }
    }

    fun getUnReadAlarmsCountByMemberId(memberId: Long?): Int {
        return alarmRepository.countUnreadAlarmsByMemberId(memberId)
    }
}
