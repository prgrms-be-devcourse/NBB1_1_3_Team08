package org.prgrms.devconnect.api.controller.board.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BoardFilterDto(
    val category: BoardCategory? = null,
    val status: BoardStatus? = null,
    val techStackIds: List<Long>? = null,
    val progressWay: ProgressWay? = null
) {
    fun isEmpty(): Boolean {
        return category == null && status == null &&
                (techStackIds == null || techStackIds.isEmpty()) &&
                progressWay == null
    }
}

