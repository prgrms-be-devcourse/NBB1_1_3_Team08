package org.prgrms.devconnect.api.service.chatting.command

import org.prgrms.devconnect.domain.chatting.repository.ChatParticipationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChattingDeleteService(
    private val chatParticipationRepository: ChatParticipationRepository,
) {
    fun leaveChatRoom(chatpartId: Long) {
        chatParticipationRepository.deleteById(chatpartId)
    }
}