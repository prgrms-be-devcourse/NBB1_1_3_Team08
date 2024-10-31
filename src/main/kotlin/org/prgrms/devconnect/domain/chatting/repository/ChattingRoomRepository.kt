package org.prgrms.devconnect.domain.chatting.repository

import org.prgrms.devconnect.domain.chatting.entity.ChattingRoom
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChattingRoomRepository : JpaRepository<ChattingRoom, Long> {
}