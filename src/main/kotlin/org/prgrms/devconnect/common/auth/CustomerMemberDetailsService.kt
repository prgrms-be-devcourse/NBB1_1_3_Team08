package org.prgrms.devconnect.common.auth

import org.prgrms.devconnect.domain.member.entity.Member
import org.prgrms.devconnect.domain.member.repository.MemberRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service


@Service
class CustomerMemberDetailsService(
    private val memberRepository: MemberRepository
) : UserDetailsService {


  @Throws(UsernameNotFoundException::class)
  override fun loadUserByUsername(username: String): UserDetails {
    val member: Member = memberRepository.findByEmail(username)
        ?: throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다.")

    return User.builder()
        .username(member.email)
        .password(member.password)
        .build()
  }
}