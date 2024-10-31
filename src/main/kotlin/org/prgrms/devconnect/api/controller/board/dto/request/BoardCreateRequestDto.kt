package org.prgrms.devconnect.api.controller.board.dto.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.BoardTechStackMapping
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BoardCreateRequestDto(

    @Schema(description = "직업 공고 ID", example = "1")
    val jobPostId: Long?,

    @Schema(description = "게시물 제목", example = "Spring Boot 개발자 모집")
    @field:NotBlank(message = "제목은 필수입니다.")
    val title: String,

    @Schema(description = "게시물 내용", example = "우리는 Spring Boot 벡엔드 개발자를 찾고 있습니다.")
    @field:NotBlank(message = "내용은 필수입니다.")
    val content: String,

    @Schema(description = "카테고리", example = "STUDY")
    @field:NotNull(message = "카테고리는 필수입니다.")
    val category: BoardCategory,

    @Schema(description = "모집 인원", example = "3")
    @field:NotNull(message = "모집 인원은 필수입니다.")
    val recruitNum: Int,

    @Schema(description = "진행 방식", example = "ONLINE")
    @field:NotNull(message = "진행 방식은 필수입니다.")
    val progressWay: ProgressWay,

    @Schema(description = "진행 기간", example = "3개월")
    @field:NotBlank(message = "진행 기간은 필수입니다.")
    val progressPeriod: String,

    @Schema(description = "종료 날짜", example = "2024-12-31T23:59:59")
    @field:NotNull(message = "종료 날짜는 필수입니다.")
    val endDate: LocalDateTime,

    @ArraySchema(schema = Schema(description = "기술 스택 목록"))
    @field:NotEmpty(message = "적어도 하나 이상의 기술 스택을 선택해야 합니다.")
    val techStackRequests: List<BoardTechStackRequestDto>
) {

    fun toEntity(member: Member, jobPost: JobPost?, boardTechStacks: List<BoardTechStackMapping>): Board {
        return Board(
            member = member,
            jobPost = jobPost,
            title = title,
            content = content,
            category = category,
            recruitNum = recruitNum,
            progressWay = progressWay,
            progressPeriod = progressPeriod,
            endDate = endDate,
            boardTechStacks = boardTechStacks.toMutableList()
        )
    }
}