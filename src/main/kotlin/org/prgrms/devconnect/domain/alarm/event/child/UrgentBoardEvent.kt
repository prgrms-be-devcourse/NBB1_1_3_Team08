package org.prgrms.devconnect.domain.alarm.event.child

import org.prgrms.devconnect.domain.alarm.event.Event
import org.prgrms.devconnect.domain.interest.entity.InterestBoard

class UrgentBoardEvent(val interestBoard: InterestBoard) : Event {
}