package org.prgrms.devconnect.domain.chatting.entity

import jakarta.persistence.*
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.chatting.ChattingException
import org.prgrms.devconnect.domain.chatting.entity.constant.ChattingRoomStatus

@Entity
@Table(name = "chatting_room")
class ChattingRoom(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    val roomId: Long? = null,


    @Column(name = "status", columnDefinition = "VARCHAR(50)")
    @Enumerated(value = EnumType.STRING)
    var status: ChattingRoomStatus = ChattingRoomStatus.ACTIVE
) {
    // 채팅방 비활성화 메서드
    fun closeChatRoom() {
        if (status === ChattingRoomStatus.INACTIVE) throw ChattingException(ExceptionCode.CHATROOM_ALREADY_INACTIVE)
        status = ChattingRoomStatus.INACTIVE
    }
}