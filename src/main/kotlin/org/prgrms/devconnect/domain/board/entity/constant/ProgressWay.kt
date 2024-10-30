package org.prgrms.devconnect.domain.board.entity.constant

enum class ProgressWay (
    private val text:String
) {
    ONLINE("온라인"),
    OFFLINE("오프라인"),
    HYBRID("온/오프라인")
}