package org.prgrms.devconnect.domain.chatting.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import jakarta.persistence.*
import org.prgrms.devconnect.domain.CreateTimestamp

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
) : CreateTimestamp() {
}