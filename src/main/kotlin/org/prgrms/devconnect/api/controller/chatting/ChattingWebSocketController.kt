package org.prgrms.devconnect.api.controller.chatting

import org.prgrms.devconnect.api.controller.chatting.dto.request.JoinRequest
import org.prgrms.devconnect.api.controller.chatting.dto.request.MessageRequest
import org.prgrms.devconnect.api.controller.chatting.dto.response.MessageResponse
import org.prgrms.devconnect.api.service.chatting.command.ChattingCreateService
import org.prgrms.devconnect.api.service.chatting.command.ChattingDeleteService
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller


@Controller
class ChattingWebSocketController(
    private val messagingTemplate: SimpMessagingTemplate,
    private val chattingCreateService: ChattingCreateService,
    private val chattingDeleteService: ChattingDeleteService,
) {
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/room/{roomId}")
    fun sendMessage(@DestinationVariable("roomId") roomId: Long, messageRequest: MessageRequest) {
        // 메시지 저장 처리
        val response: MessageResponse = chattingCreateService.sendMessage(messageRequest.chatpartId, messageRequest.content)

        // 저장된 메시지를 WebSocket을 통해 모든 구독자에게 브로드캐스트
        messagingTemplate.convertAndSend("/topic/room/$roomId", response)
    }

    @MessageMapping("/chat/join")
    fun joinChatRoom(@AuthenticationPrincipal member: Member, joinRoomRequest: JoinRequest) {
        // 사용자의 채팅방 참여 처리
        chattingCreateService.joinChatRoom(member.memberId!!, joinRoomRequest.chatroomId)
    }

    // 사용자가 채팅방을 나갈 때 처리하는 메서드
    @MessageMapping("/chat/leave/{chatpartId}")
    fun leaveChatRoom(@DestinationVariable("chatpartId") chatpartId: Long) {
        // 사용자의 채팅방 나가기 처리
        chattingDeleteService.leaveChatRoom(chatpartId)
    }
}