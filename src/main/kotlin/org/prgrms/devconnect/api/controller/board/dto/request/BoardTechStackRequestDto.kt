package org.prgrms.devconnect.api.controller.board.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Positive
import org.prgrms.devconnect.domain.board.entity.BoardTechStackMapping
import org.prgrms.devconnect.domain.techstack.entity.TechStack

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BoardTechStackRequestDto(
    @Schema(description = "기술 스택 ID", example = "1")
    @field:Positive(message = "기술 스택 ID는 0 이상이어야 합니다.")
    val techStackId: Long
) {
    fun toEntity(techStack: TechStack): BoardTechStackMapping {
        return BoardTechStackMapping(
            techStack = techStack
        )
    }
}
