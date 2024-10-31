package org.prgrms.devconnect.api.controller.board.dto.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import io.swagger.v3.oas.annotations.media.Schema
import org.prgrms.devconnect.api.controller.techstack.dto.response.TechStackResponseDto
import org.prgrms.devconnect.domain.board.entity.Board
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import java.time.LocalDateTime

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class BoardResponseDto(
    @Schema(description = "게시물 ID", example = "1")
    val boardId: Long?,

    @Schema(description = "작성자 ID", example = "42")
    val authorId: Long?,

    @Schema(description = "작성자 닉네임", example = "john_doe")
    val author: String,

    @Schema(description = "게시물 제목", example = "새로운 프로젝트 모집")
    val title: String,

    @Schema(description = "게시물 내용", example = "프로젝트 설명을 작성합니다.")
    val content: String,

    @Schema(description = "게시물 카테고리", example = "PROJECT")
    val category: BoardCategory,

    @Schema(description = "모집 인원", example = "5")
    val recruitNum: Int,

    @Schema(description = "진행 방식", example = "ONLINE")
    val progressWay: ProgressWay,

    @Schema(description = "진행 기간", example = "2개월")
    val progressPeriod: String,

    @Schema(description = "게시물 종료 날짜", example = "2024-12-31T23:59:59")
    val endDate: LocalDateTime?,

    @Schema(description = "좋아요 수", example = "150")
    val likes: Int,

    @Schema(description = "조회수", example = "500")
    val views: Int,

    @Schema(description = "생성일자", example = "2024-01-01T12:00:00")
    val createdDate: LocalDateTime?,

    @Schema(description = "수정일자", example = "2024-01-02T14:00:00")
    val updatedDate: LocalDateTime?,

    @Schema(description = "게시물 상태", example = "RECRUITING")
    val status: BoardStatus,

    @Schema(description = "기술 스택 리스트")
    val techStacks: List<TechStackResponseDto>
) {
    companion object {
        fun from(board: Board): BoardResponseDto {
            val techStackDtos = board.boardTechStacks.map { BoardTechStackMapping ->
                TechStackResponseDto.from(BoardTechStackMapping.techStack)
            }

            return BoardResponseDto(
                boardId = board.boardId,
                authorId = board.member?.memberId,
                author = board.member!!.nickname,
                title = board.title,
                content = board.content,
                category = board.category,
                recruitNum = board.recruitNum,
                progressWay = board.progressWay,
                progressPeriod = board.progressPeriod,
                endDate = board.endDate,
                likes = board.likes,
                views = board.views,
                createdDate = board.createdAt,
                updatedDate = board.updatedAt,
                status = board.status,
                techStacks = techStackDtos
            )
        }
    }
}