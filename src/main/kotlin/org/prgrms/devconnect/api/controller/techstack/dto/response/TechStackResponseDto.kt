package org.prgrms.devconnect.api.controller.techstack.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import org.prgrms.devconnect.domain.techstack.entity.TechStack


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
class TechStackResponseDto(
    val techStackId: Long,
    val name: String,
    val code: String
) {
  companion object {
    fun from(techStack: TechStack): TechStackResponseDto {
      return TechStackResponseDto(
          techStackId = techStack.techStackId!!,
          name = techStack.name,
          code = techStack.code
      )
    }
  }
}
