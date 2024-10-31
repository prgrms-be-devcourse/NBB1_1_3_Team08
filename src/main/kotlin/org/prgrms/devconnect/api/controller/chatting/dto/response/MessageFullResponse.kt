package org.prgrms.devconnect.api.controller.chatting.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.data.domain.Page

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MessageFullResponse(
    @Schema(description = "채팅 방 ID", example = "100")
    val roomId : Long,

    @Schema(description = "메시지 목록", implementation = MessageResponse::class)
    val messageList: Page<MessageResponse>
    )
