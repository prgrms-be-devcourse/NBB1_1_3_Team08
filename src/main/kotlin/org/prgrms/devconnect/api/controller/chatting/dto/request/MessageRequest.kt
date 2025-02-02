package org.prgrms.devconnect.api.controller.chatting.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MessageRequest(
    @NotNull(message = "채팅 참여 ID는 필수입니다")
    val chatpartId: Long,
    @NotBlank(message = "메세지는 공백이거나 빈값이면 안됩니다")
    val content: String,
)
