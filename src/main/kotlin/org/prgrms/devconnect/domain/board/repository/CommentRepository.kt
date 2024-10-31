package org.prgrms.devconnect.domain.board.repository

import org.prgrms.devconnect.domain.board.entity.Comment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
interface CommentRepository : JpaRepository<Comment, Long> {

    fun findAllByBoard_BoardId(boardId:Long, pageable: Pageable) : Page<Comment>

}