package org.prgrms.devconnect.domain.member.entity

import jakarta.persistence.*
import org.prgrms.devconnect.domain.techstack.entity.TechStack

@Entity
@Table(name = "member_tech_stack_mapping")
class MemberTechStackMapping(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    var member: Member? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_id", nullable = false)
    val techStack: TechStack
) {

  fun assignMember(member: Member?) {
    this.member = member
  }
}
