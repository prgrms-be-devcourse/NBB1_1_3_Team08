package org.prgrms.devconnect.api.controller.chatting.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class MessageResponse(
    @Schema(description = "메시지 ID", example = "10")
    val messageId: Long?,

    @Schema(description = "보낸 사람의 ID", example = "2")
    val senderId: Long?,

    @Schema(description = "보낸 사람의 닉네임", example = "김철수")
    val nickname: String,

    @Schema(description = "메시지 내용", example = "안녕하세요!")
    val content: String?,

    @Schema(description = "메시지 생성 시간", type = "string", format = "date-time", example = "2024-10-01T12:34:56")
    val createdAt: LocalDateTime?
)
