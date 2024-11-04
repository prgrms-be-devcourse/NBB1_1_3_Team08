package org.prgrms.devconnect.api.service.chatting.command

import org.prgrms.devconnect.api.controller.chatting.dto.response.ChatPartResponse
import org.prgrms.devconnect.api.controller.chatting.dto.response.MessageResponse
import org.prgrms.devconnect.api.service.chatting.query.ChattingQueryService
import org.prgrms.devconnect.api.service.member.MemberQueryService
import org.prgrms.devconnect.common.exception.ExceptionCode
import org.prgrms.devconnect.common.exception.chatting.ChattingException
import org.prgrms.devconnect.domain.alarm.aop.RegisterAlarmPublisher
import org.prgrms.devconnect.domain.chatting.entity.ChatParticipation
import org.prgrms.devconnect.domain.chatting.entity.ChattingRoom
import org.prgrms.devconnect.domain.chatting.entity.Message
import org.prgrms.devconnect.domain.chatting.entity.constant.ChattingRoomStatus
import org.prgrms.devconnect.domain.chatting.repository.ChatParticipationRepository
import org.prgrms.devconnect.domain.chatting.repository.ChattingRoomRepository
import org.prgrms.devconnect.domain.chatting.repository.MessageRepository
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChattingCreateService(
    private val chattingRoomRepository: ChattingRoomRepository,
    private val chatParticipationRepository: ChatParticipationRepository,
    private val messageRepository: MessageRepository,
    private val memberQueryService: MemberQueryService,
    private val chattingQueryService: ChattingQueryService,
) {
    /*
      새로운 채팅방을 생성하는 서비스 코드
      1대1 대화를 시작할 때 필요한 사용자 2명의 ID를 가져와서 처리
    */
    @RegisterAlarmPublisher
    fun createNewChatting(sendMemberId: Long, receiveMemberId: Long): Member {
        val sender = memberQueryService.getMemberByIdOrThrow(sendMemberId)
        val receiver = memberQueryService.getMemberByIdOrThrow(receiveMemberId)

        // 새로운 채팅방 생성
        val chattingRoom = ChattingRoom(status = ChattingRoomStatus.ACTIVE)
        chattingRoomRepository.save(chattingRoom)

        // 채팅 참여 객체 생성 및 채팅방 연결
        val senderChatPart = ChatParticipation(
            chattingRoom = chattingRoom,
            member = sender
        )
        val receiverChatPart = ChatParticipation(
            chattingRoom = chattingRoom,
            member = receiver
        )

        chatParticipationRepository.save(senderChatPart)
        chatParticipationRepository.save(receiverChatPart)

        return receiver
    }

    // 채팅방 참여 메서드
    fun joinChatRoom(memberId: Long, chatroomId: Long): ChatPartResponse {
        // 연관관계 매핑 준비
        val member = memberQueryService.getMemberByIdOrThrow(memberId)
        val chattingRoom = chattingQueryService.getChatRoomById(chatroomId)

        // 이미 참여했다면 오류 발생
        chatParticipationRepository.findByMember_MemberIdAndChattingRoom_RoomId(memberId, chatroomId)
            ?.let { throw ChattingException(ExceptionCode.ALREADY_JOINED_CHATROOM) }

        // 채팅 참여 엔티티 생성
        val chatParticipation = ChatParticipation(
            member = member,
            chattingRoom = chattingRoom
        )

        chatParticipationRepository.save(chatParticipation)

        return ChatPartResponse(chatParticipation.chatPartId!!, chattingRoom.roomId!!)
    }

    // 메세지를 저장하는 메서드
    fun sendMessage(chatpartId: Long, content: String): MessageResponse {
        // 채팅 참여 엔티티 조회
        val chatParticipation = chatParticipationRepository.findById(chatpartId)
            .orElseThrow { ChattingException(ExceptionCode.NOT_FOUND_CHATPART) }

        // 메세지 생성
        val message = Message(
            chatParticipation = chatParticipation,
            content = content
        )

        messageRepository.save(message)

        // 닉네임 조회
        val nickname = chatParticipation.member.nickname

        return MessageResponse(
            messageId = message.messageId,
            senderId = chatParticipation.member.memberId,
            nickname = nickname,
            content = message.content,
            createdAt = message.createdAt
        )
    }
}