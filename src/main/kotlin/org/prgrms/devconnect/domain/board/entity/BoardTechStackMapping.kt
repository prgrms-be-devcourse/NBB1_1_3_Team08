package org.prgrms.devconnect.domain.board.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.techstack.entity.TechStack

@Entity
@Table(name = "board_tech_stack_mapping")
class BoardTechStackMapping(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    var board: Board? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_id", nullable = false)
    var techStack: TechStack
) {
    // 연관관계 편의 메소드
    fun assignBoard(board: Board) {
        this.board = board
    }

    // 연관관계 편의 메소드
    fun assignTechStack(techStack: TechStack) {
        this.techStack = techStack
    }
}