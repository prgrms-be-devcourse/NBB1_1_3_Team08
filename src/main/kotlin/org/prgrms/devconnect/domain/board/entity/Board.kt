package org.prgrms.devconnect.domain.board.entity

import jakarta.persistence.*
import org.prgrms.devconnect.api.controller.board.dto.request.BoardUpdateRequestDto
import org.prgrms.devconnect.domain.Timestamp
import org.prgrms.devconnect.domain.board.entity.constant.BoardCategory
import org.prgrms.devconnect.domain.board.entity.constant.BoardStatus
import org.prgrms.devconnect.domain.board.entity.constant.ProgressWay
import org.prgrms.devconnect.domain.jobpost.entity.JobPost
import org.prgrms.devconnect.domain.member.entity.Member
import java.time.LocalDateTime

@Entity
@Table(name = "board")
class Board(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    val boardId: Long? = null,

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    val member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_post_id")
    private val jobPost:JobPost? = null,

    @Column(name = "title", length = 200)
    var title: String,

    @Column(name = "content", length = 200)
    var content: String,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "category", length = 200)
    var category: BoardCategory,

    @Column(name = "recruit_num")
    var recruitNum: Int,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "progress_way", length = 50)
    var progressWay: ProgressWay,

    @Column(name = "progress_period", length = 50)
    var progressPeriod: String,

    @Column(name = "end_date")
    var endDate: LocalDateTime,

    @Column(name = "likes")
    var likes: Int = 0,

    @Column(name = "views")
    var views: Int = 0,

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status", length = 50)
    var status: BoardStatus = BoardStatus.RECRUITING,

    @OneToMany(mappedBy = "board", cascade = [CascadeType.PERSIST])
    var boardTechStacks: MutableList<BoardTechStackMapping> = mutableListOf(),

) : Timestamp() {

//    init {
//        // 초기화 시점에서 boardTechStacks의 요소를 추가하는 로직
//        boardTechStacks.forEach { addTechStack(it) }
//    }

    // 연관관계 편의 메소드
    fun addTechStack(boardTechStack: BoardTechStackMapping) {
        boardTechStacks.add(boardTechStack)
        boardTechStack.board = this
    }

    // Board의 상태를 변경하는 메소드
    fun changeStatus(status: BoardStatus) {
        this.status = status
    }

    fun isDeleted(): Boolean {
        return this.status == BoardStatus.DELETED
    }

    fun isClosed(): Boolean {
        return this.status == BoardStatus.CLOSED
    }

    // Board 정보를 업데이트하는 메소드
    fun updateFromDto(dto: BoardUpdateRequestDto) {
        if (dto.title != this.title) {
            this.title = dto.title
        }
        if (dto.content != this.content) {
            this.content = dto.content
        }
        if (dto.category != this.category) {
            this.category = dto.category
        }
        if (dto.recruitNum != this.recruitNum) {
            this.recruitNum = dto.recruitNum
        }
        if (dto.progressWay != this.progressWay) {
            this.progressWay = dto.progressWay
        }
        if (dto.progressPeriod != this.progressPeriod) {
            this.progressPeriod = dto.progressPeriod
        }
        if (dto.endDate != this.endDate) {
            this.endDate = dto.endDate
        }
    }

    // 조회수 1 증가
    fun increaseViews() {
        this.views += 1
    }

    // 좋아요수 1 증가
    fun increaseLikes() {
        this.likes += 1
    }

    // 좋아요수 1 감소
    fun decreaseLikes() {
        this.likes -= 1
    }
}