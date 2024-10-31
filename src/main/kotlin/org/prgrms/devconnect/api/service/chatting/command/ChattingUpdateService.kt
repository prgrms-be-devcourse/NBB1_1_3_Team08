package org.prgrms.devconnect.api.service.chatting.command

import org.prgrms.devconnect.api.service.chatting.query.ChattingQueryService
import org.prgrms.devconnect.domain.chatting.repository.ChattingRoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChattingUpdateService(
    private val chattingQueryService: ChattingQueryService,
    private val chattingRoomRepository: ChattingRoomRepository,
) {
    // 채팅방 비활성화 서비스
    fun closeChattingRoom(chatroomId: Long) {
        val chattingRoom = chattingQueryService.getChatRoomById(chatroomId)
        chattingRoom.closeChatRoom()
        chattingRoomRepository.save(chattingRoom)
    }
}