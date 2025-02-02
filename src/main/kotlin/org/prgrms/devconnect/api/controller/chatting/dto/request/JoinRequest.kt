package org.prgrms.devconnect.api.controller.chatting.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotNull

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class JoinRequest(
    @NotNull(message = "채팅방 ID는 필수입니다")
    val chatroomId: Long,
)
