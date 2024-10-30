package org.prgrms.devconnect.domain.board.entity

import jakarta.persistence.*
import org.prgrms.devconnect.api.controller.board.dto.request.CommentUpdateRequestDto
import org.prgrms.devconnect.domain.Timestamp
import org.prgrms.devconnect.domain.member.entity.Member

@Entity
@Table(name = "comment")
class Comment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val commentId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    var board: Board,

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = true)
    var parent: Comment? = null,

    @Column(length = 500)
    var content: String
) : Timestamp(){


    // 루트 댓글인지 확인하는 메서드
    fun isRootComment(): Boolean {
        return parent == null
    }

    // DTO에서 업데이트를 처리하는 메서드
    fun updateFromDto(dto: CommentUpdateRequestDto) {
        if (dto.content != this.content) {
            this.content = dto.content
        }
    }
}
