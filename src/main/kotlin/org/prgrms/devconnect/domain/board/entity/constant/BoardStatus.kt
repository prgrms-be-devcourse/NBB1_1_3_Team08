package org.prgrms.devconnect.domain.board.entity.constant

enum class BoardStatus (
    private val text:String
) {
    RECRUITING("모집중"),
    CLOSED("마감"),
    DELETED("삭제된 게시글")
}