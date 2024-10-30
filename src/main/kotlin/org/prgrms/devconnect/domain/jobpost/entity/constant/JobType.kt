package org.prgrms.devconnect.domain.jobpost.entity.constant

enum class JobType (
        private val text: String
) {
    REGULAR("정규직"),
    IRREGULAR("비정규직"),
    CONTRACT("인턴");
}