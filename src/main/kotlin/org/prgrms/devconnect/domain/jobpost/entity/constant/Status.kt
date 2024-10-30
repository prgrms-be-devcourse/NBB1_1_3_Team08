package org.prgrms.devconnect.domain.jobpost.entity.constant

enum class Status (
        private val text: String
){
    RECRUITING("모집중"),
    DEADLINE("마감"),
    DELETED("삭제됨");
}