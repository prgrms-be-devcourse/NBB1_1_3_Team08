package org.prgrms.devconnect.domain.member.entity

import jakarta.persistence.*
import org.prgrms.devconnect.api.controller.member.dto.request.MemberUpdateRequestDto
import org.prgrms.devconnect.domain.Timestamp
import org.prgrms.devconnect.domain.member.entity.constant.Interest
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.function.Consumer


@Entity
@Table(name = "member")
class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    var memberId: Long? = null,

    @Column(name = "email", length = 100)
    var email: String,

    @Column(name = "password", length = 100)
    var password: String,

    @Column(name = "nickname", length = 50)
    var nickname: String,

    @Column(name = "job", length = 50)
    var job: String,

    @Column(name = "affiliation", length = 100)
    var affiliation: String,

    @Column(name = "career")
    var career: Int,

    @Column(name = "self_introduction")
    var selfIntroduction: String,

    @Column(name = "blog_link", length = 100)
    var blogLink: String,

    @Column(name = "github_link", length = 100)
    var githubLink: String,

    @Column(name = "interest", length = 100)
    @Enumerated(value = EnumType.STRING)
    var interest: Interest,

    memberTechStacks: List<MemberTechStackMapping>
) : Timestamp() {


  @OneToMany(mappedBy = "member", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
  val memberTechStacks: MutableList<MemberTechStackMapping> = ArrayList()

  init {
    if (memberTechStacks.isNotEmpty()) {
      memberTechStacks.forEach(Consumer { memberTechStack: MemberTechStackMapping -> this.addTechStackMapping(memberTechStack) })
    }
  }

  // == 연관관계 편의 메서드 == //
  fun addTechStackMapping(memberTechStack: MemberTechStackMapping) {
    memberTechStacks.add(memberTechStack)
    memberTechStack.assignMember(this)
  }

  fun passwordEncode(passwordEncoder: PasswordEncoder) {
    this.password = passwordEncoder.encode(this.password)
  }

  fun updateFromDto(requestDto: MemberUpdateRequestDto) {
    TODO("Not yet implemented")
  }
}