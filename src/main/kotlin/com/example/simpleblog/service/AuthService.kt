package com.example.simpleblog.service

import com.example.simpleblog.config.security.PrincipalDetails
import com.example.simpleblog.domain.member.MemberRepository
import mu.KotlinLogging
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository,
) : UserDetailsService {
    val log = KotlinLogging.logger { }

    override fun loadUserByUsername(email: String): UserDetails? {
        val member = memberRepository.findMemberByEmail(email)

        log.info { member }

        return PrincipalDetails(member)
    }
}