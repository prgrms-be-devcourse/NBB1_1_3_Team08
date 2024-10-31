package org.prgrms.devconnect.domain.alarm.event.child

import org.prgrms.devconnect.domain.alarm.event.Event
import org.prgrms.devconnect.domain.board.entity.Comment

class ReplyCommentEvent(val comment: Comment) : Event{
}