package org.prgrms.devconnect.domain.alarm.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.Timestamp
import org.prgrms.devconnect.domain.member.entity.Member

@Entity
@Table(name = "alarm")
class Alarm(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private var alarmId: Long? = null,

    @Column(name = "alert_text", length = 500)
    private var alertText: String,

    @Column(name = "related_url", length = 500)
    private var relatedUrl: String,

    @Column(name = "is_read")
    private var isRead: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private val member: Member
) : Timestamp() {
    fun updateAlarmStatusToRead() {
        this.isRead = true
    }
}

