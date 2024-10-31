package org.prgrms.devconnect.api.controller.chatting.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChatPartResponse(
    @Schema(description = "채팅 참여자 ID", example = "1")
    val chatpartId: Long,

    @Schema(description = "채팅 방 ID", example = "1")
    val roomId: Long,
)
