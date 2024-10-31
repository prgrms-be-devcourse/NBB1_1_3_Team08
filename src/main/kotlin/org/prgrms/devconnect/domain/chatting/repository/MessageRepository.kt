package org.prgrms.devconnect.domain.chatting.repository

import org.prgrms.devconnect.domain.chatting.entity.Message
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MessageRepository : JpaRepository<Message, Long> {
}