package org.prgrms.devconnect.domain.chatting.repository.custom

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.prgrms.devconnect.api.controller.chatting.dto.response.ChatRoomListResponse
import org.prgrms.devconnect.api.controller.chatting.dto.response.MessageResponse
import org.prgrms.devconnect.domain.chatting.entity.QChatParticipation
import org.prgrms.devconnect.domain.chatting.entity.QChattingRoom
import org.prgrms.devconnect.domain.chatting.entity.QMessage
import org.prgrms.devconnect.domain.chatting.entity.constant.ChattingRoomStatus
import org.prgrms.devconnect.domain.member.entity.QMember
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class ChatPartRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory
) : ChatPartRepositoryCustom {

    override fun findByMemberIdAndStatusIsActive(memberId: Long): List<ChatRoomListResponse> {
        val chatpart = QChatParticipation.chatParticipation
        val chattingRoom = QChattingRoom.chattingRoom

        return queryFactory.select(
            Projections.constructor(
                ChatRoomListResponse::class.java,
                chatpart.member.memberId,
                chatpart.chatPartId,
                chattingRoom.roomId,
                chattingRoom.status
            )
        )
            .from(chatpart)
            .join(chatpart.chattingRoom, chattingRoom)
            .where(
                chatpart.member.memberId.eq(memberId)
                    .and(chattingRoom.status.eq(ChattingRoomStatus.ACTIVE))
            )
            .fetch()
    }

    override fun findByMemberIdAndStatusIsActiveWithPage(
        memberId: Long,
        pageable: Pageable
    ): Page<ChatRoomListResponse> {
        val chatpart = QChatParticipation.chatParticipation
        val chattingRoom = QChattingRoom.chattingRoom

        val content = queryFactory.select(
            Projections.constructor(
                ChatRoomListResponse::class.java,
                chatpart.member.memberId,
                chatpart.chatPartId,
                chattingRoom.roomId,
                chattingRoom.status
            )
        )
            .from(chatpart)
            .join(chatpart.chattingRoom, chattingRoom)
            .where(
                chatpart.member.memberId.eq(memberId)
                    .and(chattingRoom.status.eq(ChattingRoomStatus.ACTIVE))
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory.select(chattingRoom.count())
            .from(chattingRoom)
            .join(chatpart.chattingRoom, chattingRoom)
            .where(
                chatpart.member.memberId.eq(memberId)
                    .and(chattingRoom.status.eq(ChattingRoomStatus.ACTIVE))
            )
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    override fun findAllMessageByRoomId(roomId: Long, pageable: Pageable): Page<MessageResponse> {
        val chatpart = QChatParticipation.chatParticipation
        val message = QMessage.message
        val member = QMember.member

        val results = queryFactory.select(
            Projections.constructor(
                MessageResponse::class.java,
                message.messageId,
                chatpart.member.memberId,
                member.nickname,
                message.content,
                message.createdAt
            )
        )
            .from(message)
            .join(message.chatParticipation, chatpart)
            .join(chatpart.member, member)
            .where(chatpart.chattingRoom.roomId.eq(roomId))
            .orderBy(message.createdAt.desc())
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory.select(message.count())
            .from(message)
            .join(message.chatParticipation, chatpart)
            .where(chatpart.chattingRoom.roomId.eq(roomId))
            .fetchOne() ?: 0L

        return PageImpl(results, pageable, total)
    }


}