package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.MemberRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

class CustomBasicAuthenticationFilter(
    authenticationManager: AuthenticationManager,
    private val memberRepository: MemberRepository,
) : BasicAuthenticationFilter(authenticationManager) {
    val log = KotlinLogging.logger { }

    private val jwtManager = JwtManager()

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        log.info { "권한이나 인증이 필요한 요청이 들어옴" }

        val token = request.getHeader(AUTH_HEADER)?.replace("$AUTH_TYPE ", "")
        if (token == null) {
            log.error { "토큰이 필요합니다" }
            chain.doFilter(request, response)
            return
        }

        val email = jwtManager.getMemberEmail(token) ?: throw RuntimeException("토큰에 이메일이 없습니다.")
        val member = memberRepository.findMemberByEmail(email)
        val principalDetails = PrincipalDetails(member)
        val authentication: Authentication = UsernamePasswordAuthenticationToken(
            principalDetails,
            principalDetails.password,
            principalDetails.authorities,  // NOTE: password의 평문과 암호문을 비교하기 위해 필요
        )
        SecurityContextHolder.getContext().authentication = authentication

        chain.doFilter(request, response)
    }
}