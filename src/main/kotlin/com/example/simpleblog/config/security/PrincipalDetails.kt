package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.Member
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class PrincipalDetails(
    member: Member,
) : UserDetails {
    var member: Member = member
        private set

    private val log = KotlinLogging.logger { }

    override fun getAuthorities(): Collection<GrantedAuthority?>? {
        log.info { "Role 인가 " }

        val authorities: MutableCollection<GrantedAuthority> = ArrayList()
        authorities.add(GrantedAuthority { "ROLE_${member.role}" })

        return authorities
    }

    override fun getPassword(): String? {
        return member.password
    }

    override fun getUsername(): String? {
        return member.email
    }
}