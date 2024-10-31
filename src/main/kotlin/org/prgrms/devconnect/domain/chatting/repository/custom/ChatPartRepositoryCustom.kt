package org.prgrms.devconnect.domain.chatting.repository.custom

import org.prgrms.devconnect.api.controller.chatting.dto.response.ChatRoomListResponse
import org.prgrms.devconnect.api.controller.chatting.dto.response.MessageResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ChatPartRepositoryCustom {
    fun findByMemberIdAndStatusIsActive(memberId: Long): List<ChatRoomListResponse>?

    fun findByMemberIdAndStatusIsActiveWithPage(memberId: Long, pageable: Pageable): Page<ChatRoomListResponse>?

    fun findAllMessageByRoomId(roomId: Long, pageable: Pageable): Page<MessageResponse>
}