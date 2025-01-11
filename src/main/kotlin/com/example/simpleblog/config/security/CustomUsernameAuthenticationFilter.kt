package com.example.simpleblog.config.security

import com.example.simpleblog.domain.member.LoginDto
import com.example.simpleblog.util.func.responseData
import com.example.simpleblog.util.value.CmResDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class CustomUsernameAuthenticationFilter(
    private val objectMapper: ObjectMapper,
) : UsernamePasswordAuthenticationFilter() {
    private val log = KotlinLogging.logger { }

    private val jwtManager = JwtManager()

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication? {
        log.info { "login 요청 전달됨" }

        lateinit var loginDto: LoginDto
        try {
            loginDto = objectMapper.readValue(request?.inputStream, LoginDto::class.java)
            log.info { "loginDto: $loginDto" }
        } catch (e: Exception) {
            log.error { "loginFilter: 로그인 요청 Dto 생성 실패!   ${e.stackTraceToString()}" }
        }
        val authenticationToken = UsernamePasswordAuthenticationToken(loginDto.email, loginDto.password)

        return authenticationManager.authenticate(authenticationToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication,
    ) {
        log.info { "로그인 완료되어서 JWT를 만들고 response 생성" }

        val principalDetails = authResult.principal as PrincipalDetails
        val jwtToken = jwtManager.generateAccessToken(objectMapper.writeValueAsString(principalDetails))
        response.addHeader(AUTH_HEADER, "$AUTH_TYPE $jwtToken")

        val jsonData = objectMapper.writeValueAsString(
            CmResDto(
                HttpStatus.OK.toString(),
                "login success",
                principalDetails.member,
            )
        )

        responseData(response, jsonData)
    }
}