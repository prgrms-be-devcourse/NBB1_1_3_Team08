package org.prgrms.devconnect.domain.alarm.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.CreateTimestamp
import org.prgrms.devconnect.domain.Timestamp
import org.prgrms.devconnect.domain.member.entity.Member

@Entity
@Table(name = "alarm")
class Alarm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    var alarmId: Long? = null,

    @Column(name = "alert_text", length = 500)
    var alertText: String,

    @Column(name = "related_url", length = 500)
    var relatedUrl: String,

    @Column(name = "is_read")
    var isRead: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member
) : CreateTimestamp() {
    fun updateAlarmStatusToRead() {
        this.isRead = true
    }
}

