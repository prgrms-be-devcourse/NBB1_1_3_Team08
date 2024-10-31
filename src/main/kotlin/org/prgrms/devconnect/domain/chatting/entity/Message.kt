package org.prgrms.devconnect.domain.chatting.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*

@Entity
@Table(name = "message")
class Message(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    val messageId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_part_id", nullable = false)
    @JsonBackReference
    var chatParticipation: ChatParticipation? = null,

    @Column(name = "content", columnDefinition = "TEXT")
    var content: String? = null,
) {
    // 연관관계 편의 메서드
    fun setChatParticipation(chatParticipation: ChatParticipation?) {
        this.chatParticipation = chatParticipation
    }
}