package org.prgrms.devconnect.api.controller.board.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
@Schema(description = "게시물 수정 요청 DTO")
data class BoardUpdateRequestDto(

    @field:NotBlank(message = "제목은 필수입니다.")
    @Schema(description = "게시물 제목", example = "프로젝트 참가자 모집")
    val title: String,

    @field:NotBlank(message = "내용은 필수입니다.")
    @Schema(description = "게시물 내용", example = "함께 프로젝트를 진행할 참가자를 모집합니다.")
    val content: String,

    @field:NotNull(message = "카테고리는 필수입니다.")
    @Schema(description = "게시물 카테고리", example = "STUDY")
    val category: BoardCategory,

    @field:NotNull(message = "모집 인원은 필수입니다.")
    @Schema(description = "모집 인원 수", example = "5")
    val recruitNum: Int,

    @field:NotNull(message = "진행 방식은 필수입니다.")
    @Schema(description = "진행 방식", example = "ONLINE")
    val progressWay: ProgressWay,

    @field:NotBlank(message = "진행 기간은 필수입니다.")
    @Schema(description = "진행 기간", example = "3개월")
    val progressPeriod: String,

    @field:NotNull(message = "종료 날짜는 필수입니다.")
    @Schema(description = "프로젝트 종료 날짜", example = "2024-12-31T23:59:59")
    val endDate: LocalDateTime,

    @Schema(description = "추가할 기술 스택 ID 목록", example = "[1, 2, 3]")
    val addTechStacks: List<Long>? = emptyList(),

    @Schema(description = "삭제할 기술 스택 ID 목록", example = "[4, 5]")
    val deleteTechStacks: List<Long>? = emptyList()
)
