package org.prgrms.devconnect.domain.alarm.repository

import org.prgrms.devconnect.domain.alarm.entity.Alarm
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface AlarmRepository : JpaRepository<Alarm?, Long?> {
    fun findAllByMember(member: Member): List<Alarm>

    @Query("select count(*) from Alarm a where a.member.memberId = :memberId and a.isRead = false")
    fun countUnreadAlarmsByMemberId(@Param("memberId") memberId: Long?): Int
    fun deleteAllByMemberMemberId(memberId: Long)
    fun deleteByAlarmIdAndMemberMemberId(alarmId: Long, memberId: Long)
    fun findByAlarmIdAndMemberMemberId(alarmId: Long?, memberId: Long?): Optional<Alarm>
}