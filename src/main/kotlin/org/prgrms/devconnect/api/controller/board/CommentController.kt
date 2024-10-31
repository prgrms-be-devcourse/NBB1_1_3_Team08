package org.prgrms.devconnect.api.controller.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.prgrms.devconnect.api.controller.board.dto.request.CommentCreateRequestDto
import org.prgrms.devconnect.api.controller.board.dto.request.CommentUpdateRequestDto
import org.prgrms.devconnect.api.controller.board.dto.response.CommentResponseDto
import org.prgrms.devconnect.api.service.Board.command.CommentCreateService
import org.prgrms.devconnect.api.service.Board.command.CommentDeleteService
import org.prgrms.devconnect.api.service.Board.command.CommentUpdateService
import org.prgrms.devconnect.api.service.Board.query.CommentQueryService
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "댓글 API", description = "댓글 관련 기능을 제공하는 API")
@ApiResponses(
    value = [
        ApiResponse(responseCode = "400", description = "잘못된 요청"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ]
)
class CommentController(
    private val commentCreateService: CommentCreateService,
    private val commentUpdateService: CommentUpdateService,
    private val commentDeleteService: CommentDeleteService,
    private val commentQueryService: CommentQueryService
) {

    @PostMapping
    @Operation(summary = "댓글 생성", description = "새로운 댓글을 작성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "댓글 생성 성공"),
            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
        ]
    )
    fun createComment(
        @AuthenticationPrincipal member: Member,
        @RequestBody @Valid commentCreateRequestDto: CommentCreateRequestDto
    ): ResponseEntity<Void> {
        commentCreateService.createComment(commentCreateRequestDto, member.memberId!!)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @GetMapping("/{boardId}")
    @Operation(
        summary = "게시물 댓글 조회",
        description = "특정 게시물에 대한 모든 댓글을 페이징하여 조회합니다.",
        parameters = [Parameter(name = "boardId", description = "게시물 ID", required = true, example = "1")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 조회 성공"),
            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
        ]
    )
    fun getComments(@PathVariable boardId: Long, pageable: Pageable): ResponseEntity<Page<CommentResponseDto>> {
        val comments = commentQueryService.getCommentsByBoardId(boardId, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(comments)
    }

    @PutMapping("/{commentId}")
    @Operation(
        summary = "댓글 수정",
        description = "댓글 ID를 기반으로 댓글을 수정합니다.",
        parameters = [Parameter(name = "commentId", description = "댓글 ID", required = true, example = "1")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
        ]
    )
    fun updateComment(
        @PathVariable commentId: Long,
        @RequestBody @Valid commentUpdateRequestDto: CommentUpdateRequestDto
    ): ResponseEntity<Void> {
        commentUpdateService.updateComment(commentId, commentUpdateRequestDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @DeleteMapping("/{commentId}")
    @Operation(
        summary = "댓글 삭제",
        description = "특정 댓글을 삭제합니다.",
        parameters = [Parameter(name = "commentId", description = "댓글 ID", required = true, example = "1")]
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
        ]
    )
    fun deleteComment(@PathVariable commentId: Long): ResponseEntity<Void> {
        commentDeleteService.deleteComment(commentId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}