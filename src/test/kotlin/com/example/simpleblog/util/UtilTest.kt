package com.example.simpleblog.util

import com.example.simpleblog.config.security.JwtManager
import com.example.simpleblog.config.security.PrincipalDetails
import com.example.simpleblog.domain.member.Member
import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.Test

@SpringBootTest
class UtilTest {
    val log = KotlinLogging.logger { }

    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()

        val encPassword = encoder.encode("1234")
        log.info { encPassword }
    }

    @Test
    fun generateJwtTest() {
        val jwtManager = JwtManager()

        val details = PrincipalDetails(Member.createFakeMember(1))
        val accessToken = jwtManager.generateAccessToken(details)

        val email = jwtManager.getMemberEmail(accessToken)

        log.info("accessToken: $accessToken")
        log.info("email: $email")
    }
}