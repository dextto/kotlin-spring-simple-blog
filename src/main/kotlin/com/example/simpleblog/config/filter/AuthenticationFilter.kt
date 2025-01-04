package com.example.simpleblog.config.filter

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging

/**
 * 인증 처리
 * TODO: Spring Security 에서 이미 제공하므로 이것으로 변경
 */
class AuthenticationFilter : Filter {
    val log = KotlinLogging.logger { }

    override fun doFilter(
        request: ServletRequest?,
        response: ServletResponse?,
        chain: FilterChain?,
    ) {
        val servletRequest = request as HttpServletRequest
        val principal = servletRequest.getSession().getAttribute("principal")

        if (principal == null) {
            throw RuntimeException("session을 찾을 수 없습니다!!")
        }

        chain?.doFilter(request, response)
    }
}