package org.prgrms.devconnect.api.controller.chatting.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.domain.chatting.entity.constant.ChattingRoomStatus

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChatRoomListResponse(
    @Schema(description = "멤버 ID", example = "2")
    val memberId : Long,

    @Schema(description = "채팅 참여자 ID", example = "1")
    val chatpartId : Long,

    @Schema(description = "채팅 방 ID", example = "100")
    val roomId: Long,

    @Schema(description = "채팅 방 상태", example = "ACTIVE")
    val status: ChattingRoomStatus,
){

}
