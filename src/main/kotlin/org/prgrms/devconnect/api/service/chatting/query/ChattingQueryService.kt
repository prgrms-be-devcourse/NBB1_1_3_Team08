package org.prgrms.devconnect.api.service.chatting.query

import org.prgrms.devconnect.api.controller.chatting.dto.response.ChatParticipationResponse
import org.prgrms.devconnect.api.controller.chatting.dto.response.ChatRoomListResponse
import org.prgrms.devconnect.api.controller.chatting.dto.response.MessageFullResponse
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.chatting.ChattingException
import org.prgrms.devconnect.domain.chatting.entity.ChattingRoom
import org.prgrms.devconnect.domain.chatting.repository.ChatParticipationRepository
import org.prgrms.devconnect.domain.chatting.repository.ChattingRoomRepository
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ChattingQueryService(
    private val chatParticipationRepository: ChatParticipationRepository,
    private val chattingRoomRepository: ChattingRoomRepository,
) {
    fun findAllActivateChattingsByMemberId(memberId: Long): List<ChatRoomListResponse>? {
        return chatParticipationRepository.findByMemberIdAndStatusIsActive(memberId)
    }

    // 채팅방 메세지 조회하는 메서드
    fun getAllMessageByRoomId(roomId: Long, pageable: Pageable): MessageFullResponse {
        val content = chatParticipationRepository.findAllMessageByRoomId(roomId, pageable)
        return MessageFullResponse(
            roomId = roomId,
            messageList = content)
    }

    // 채팅방 ID로 채팅방 엔티티를 가져오는 메서드
    fun getChatRoomById(chatroomId: Long): ChattingRoom {
        return chattingRoomRepository.findById(chatroomId)
            .orElseThrow { ChattingException(ExceptionCode.NOT_FOUND_CHATROOM) }
    }

    fun getParticipation(roomId: Long): ChatParticipationResponse {
        val chatRoom = getChatRoomById(roomId)
        val chatParticipation =
            chatParticipationRepository.findAllByChattingRoomWithMember(chatRoom)
        return ChatParticipationResponse.from(chatParticipation)
    }
}