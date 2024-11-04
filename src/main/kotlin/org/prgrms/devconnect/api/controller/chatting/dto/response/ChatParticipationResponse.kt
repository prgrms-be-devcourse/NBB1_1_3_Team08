package org.prgrms.devconnect.api.controller.chatting.dto.response

import org.prgrms.devconnect.domain.chatting.entity.ChatParticipation

class ChatParticipationResponse(
  val participations: List<String>
) {

  companion object {
    fun from(participations: List<ChatParticipation>): ChatParticipationResponse {
      return ChatParticipationResponse(
        participations = participations.map { p -> p.member.nickname }
      )
    }
  }
}