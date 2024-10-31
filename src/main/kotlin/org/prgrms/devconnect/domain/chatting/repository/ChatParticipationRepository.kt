package org.prgrms.devconnect.domain.chatting.repository

import org.prgrms.devconnect.domain.chatting.entity.ChatParticipation
import org.prgrms.devconnect.domain.chatting.repository.custom.ChatPartRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ChatParticipationRepository : JpaRepository<ChatParticipation, Long>, ChatPartRepositoryCustom {
    fun findAllByChattingRoom_RoomId(roomId: Long?): List<ChatParticipation?>?
    fun findByMember_MemberIdAndChattingRoom_RoomId(memberId: Long?, chatRoomId: Long?): Optional<ChatParticipation?>?
}