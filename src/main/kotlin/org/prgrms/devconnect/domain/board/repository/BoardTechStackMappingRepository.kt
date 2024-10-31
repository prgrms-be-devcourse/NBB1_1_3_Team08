package org.prgrms.devconnect.domain.board.repository

import org.prgrms.devconnect.domain.board.entity.BoardTechStackMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BoardTechStackMappingRepository : JpaRepository<BoardTechStackMapping, Long> {

    fun findAllByBoard_BoardIdAndTechStack_TechStackIdIn(boardId:Long, techStackIds:List<Long>) : List<BoardTechStackMapping>

    @Modifying
    @Query("DELETE FROM BoardTechStackMapping b where b.id IN :ids")
    fun deleteAllByIds(@Param("ids") ids: List<Long?>?)
}