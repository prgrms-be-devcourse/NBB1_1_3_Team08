package org.prgrms.devconnect.api.controller.board

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.prgrms.devconnect.api.controller.board.dto.request.BoardUpdateRequestDto
import org.prgrms.devconnect.api.controller.board.dto.response.BoardResponseDto
import org.prgrms.devconnect.api.service.Board.command.BoardCreateService
import org.prgrms.devconnect.api.service.Board.command.BoardDeleteService
import org.prgrms.devconnect.api.service.Board.command.BoardUpdateService
import org.prgrms.devconnect.api.service.Board.query.BoardQueryService
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import org.prgrms.devconnect.domain.member.entity.Member
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/boards")
@Tag(name = "게시판 API", description = "게시물 관련 기능을 제공하는 API")
@ApiResponses(
    value = [
        ApiResponse(responseCode = "400", description = "잘못된 요청"),
        ApiResponse(responseCode = "500", description = "서버 오류")
    ]
)
class BoardController(
    private val boardCreateService: BoardCreateService,
    private val boardUpdateService: BoardUpdateService,
    private val boardDeleteService: BoardDeleteService,
    private val boardQueryService: BoardQueryService,
) {

    //TODO 주석 삭제
//    @PostMapping
//    @Operation(summary = "게시물 생성", description = "새로운 게시물을 생성합니다.")
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "201", description = "게시물 생성 성공"),
//            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
//        ]
//    )
//    fun createBoard(
//        @RequestBody @Valid boardCreateRequestDto: BoardCreateRequestDto,
//        @AuthenticationPrincipal member: Member
//    ): ResponseEntity<Void> {
//        boardCreateService.createBoard(boardCreateRequestDto, member.memberId)
//        return ResponseEntity.status(HttpStatus.CREATED).build()
//    }

    @DeleteMapping("/{boardId}")
    @Operation(summary = "게시물 삭제", description = "특정 게시물을 삭제합니다.", parameters = [
        Parameter(name = "boardId", description = "게시물 ID", required = true, example = "1")
    ])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "게시물 삭제 성공"),
            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
        ]
    )
    fun deleteBoard(@PathVariable boardId: Long): ResponseEntity<Void> {
        boardDeleteService.deleteBoard(boardId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PutMapping("/{boardId}")
    @Operation(summary = "게시물 수정", description = "게시물 ID를 기반으로 게시물을 수정합니다.")
    fun updateBoard(
        @PathVariable boardId: Long,
        @RequestBody @Valid boardUpdateRequestDto: BoardUpdateRequestDto
    ): ResponseEntity<Void> {
        boardUpdateService.updateBoard(boardId, boardUpdateRequestDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @PutMapping("/{boardId}/close")
    @Operation(summary = "게시물을 마감", description = "특정 게시물을 수동으로 마감합니다.", parameters = [
        Parameter(name = "boardId", description = "게시물 ID", required = true, example = "1")
    ])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 마감 성공"),
            ApiResponse(responseCode = "403", description = "이미 마감된 게시물입니다."),
            ApiResponse(responseCode = "404", description = "존재하지 않는 게시물 또는 삭제된 게시물입니다.")
        ]
    )
    fun closeBoard(@PathVariable boardId: Long): ResponseEntity<Void> {
        boardUpdateService.closeBoardManually(boardId)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @GetMapping("/{boardId}")
    @Operation(summary = "단일 게시물 조회", description = "특정 게시물의 조회수를 증가시키고 게시물 정보를 반환합니다.", parameters = [
        Parameter(name = "boardId", description = "게시물 ID", required = true, example = "1")
    ])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
        ]
    )
    fun getBoardById(@PathVariable boardId: Long): ResponseEntity<BoardResponseDto> {
        boardUpdateService.increaseViews(boardId)
        val boardResponse = boardQueryService.getBoardById(boardId)
        return ResponseEntity.status(HttpStatus.OK).body(boardResponse)
    }

    @GetMapping
    @Operation(summary = "전체 게시물 조회", description = "모든 게시물을 페이징하여 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 조회 성공")
        ]
    )
    fun getAllBoards(
        @PageableDefault(size = 20, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<BoardResponseDto>> {
        val boards = boardQueryService.getAllBoards(pageable)
        return ResponseEntity.status(HttpStatus.OK).body(boards)
    }

    @GetMapping("/filter")
    @Operation(summary = "필터로 게시물 조회", description = "카테고리, 상태, 기술 스택 등의 조건으로 게시물을 필터링하여 조회합니다.", parameters = [
        Parameter(name = "category", description = "게시물 카테고리", required = false),
        Parameter(name = "status", description = "게시물 상태", required = false),
        Parameter(name = "techStackIds", description = "기술 스택 ID 목록", required = false),
        Parameter(name = "progressWay", description = "진행 방식", required = false)
    ])
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 조회 성공")
        ]
    )
    fun getBoardsByFilter(
        @RequestParam(required = false) category: BoardCategory?,
        @RequestParam(required = false) status: BoardStatus?,
        @RequestParam(required = false) techStackIds: List<Long>?,
        @RequestParam(required = false) progressWay: ProgressWay?,
        @PageableDefault(size = 20, sort = ["createdDate"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<Page<BoardResponseDto>> {
        val boards = boardQueryService.getBoardsByFilter(category, status, techStackIds, progressWay, pageable)
        return ResponseEntity.status(HttpStatus.OK).body(boards)
    }

    @GetMapping("/popular")
    @Operation(summary = "이번 주 인기 게시물 조회", description = "이번 주 조회수가 높은 10개의 게시물을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "게시물 조회 성공")
        ]
    )
    fun getPopularBoardsThisWeek(): ResponseEntity<List<BoardResponseDto>> {
        val popularBoards = boardQueryService.getTop10PopularBoardsThisWeek()
        return ResponseEntity.status(HttpStatus.OK).body(popularBoards)
    }

    //TODO 주석 삭제
//    @GetMapping("/interests")
//    @Operation(summary = "사용자 관심사 기반 게시물 조회", description = "특정 사용자의 관심사를 기반으로 추천 게시물을 조회합니다.")
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
//            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
//        ]
//    )
//    fun getBoardsByMemberInterests(@AuthenticationPrincipal member: Member): ResponseEntity<List<BoardResponseDto>> {
//        val boards = boardQueryService.getBoardsByMemberInterests(member.memberId)
//        return ResponseEntity.status(HttpStatus.OK).body(boards)
//    }
//
//    @GetMapping("/jobpost/{jobPostId}")
//    @Operation(summary = "특정 구직 공고와 연관된 게시물 조회", description = "특정 구직 공고와 연관된 게시물들을 조회합니다.", parameters = [
//        Parameter(name = "jobPostId", description = "공고 ID", required = true, example = "1")
//    ])
//    @ApiResponses(
//        value = [
//            ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
//            ApiResponse(responseCode = "404", description = "엔티티 NOT FOUND")
//        ]
//    )
//    fun getBoardsByJobPostId(@PathVariable jobPostId: Long): ResponseEntity<List<BoardResponseDto>> {
//        val boards = boardQueryService.getBoardsByJobPostId(jobPostId)
//        return ResponseEntity.status(HttpStatus.OK).body(boards)
//    }

    @GetMapping("/popular-tag")
    @Operation(summary = "인기 태그 게시물 조회", description = "이번 주에 작성되고 조회수가 500 이상인 게시물을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "인기 태그 게시물 조회 성공")
        ]
    )
    fun getBoardsWithPopularTagCondition(): ResponseEntity<List<BoardResponseDto>> {
        val popularTaggedBoards = boardQueryService.getBoardsWithPopularTagCondition()
        return ResponseEntity.status(HttpStatus.OK).body(popularTaggedBoards)
    }

    @GetMapping("/deadline-approaching")
    @Operation(summary = "마감 임박 게시물 조회", description = "마감 2일 전 게시물을 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "마감 임박 게시물 조회 성공")
        ]
    )
    fun getBoardsWithDeadlineApproaching(): ResponseEntity<List<BoardResponseDto>> {
        val deadlineApproachingBoards = boardQueryService.getBoardsWithDeadlineApproaching()
        return ResponseEntity.status(HttpStatus.OK).body(deadlineApproachingBoards)
    }
}
