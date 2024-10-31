package org.prgrms.devconnect.domain.chatting.entity

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonManagedReference
import jakarta.persistence.*
import org.prgrms.devconnect.domain.member.entity.Member

@Entity
@Table(name = "chat_participation")
class ChatParticipation(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "chat_part_id")
    val chatPartId: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    var chattingRoom: ChattingRoom? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null,

    @OneToMany(mappedBy = "chatParticipation", fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST])
    @JsonManagedReference
    var messages: MutableList<Message> = mutableListOf()
) {
    fun addMessage(message: Message) {
        message.setChatParticipation(this)
        messages.add(message)
    }

    // 연관관계 편의 메서드
    fun setMember(member: Member?) {
        this.member = member
    }
}