package org.prgrms.devconnect.domain.alarm.event.child

import org.prgrms.devconnect.domain.alarm.event.Event
import org.prgrms.devconnect.domain.member.entity.Member

class WelcomeEvent(val member: Member) : Event {
}