package com.example.simpleblog.config.security


import com.example.simpleblog.domain.member.Member
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import mu.KotlinLogging
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@JsonIgnoreProperties("authorities")   // TODO: 역직렬화 안되는 이슈 수정
class PrincipalDetails(
    member: Member = Member.createFakeMember(0),
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

