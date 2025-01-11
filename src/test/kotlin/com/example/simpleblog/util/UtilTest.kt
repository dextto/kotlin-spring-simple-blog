package com.example.simpleblog.util

import com.example.simpleblog.config.security.CLAIM_PRINCIPAL
import com.example.simpleblog.config.security.JwtManager
import com.example.simpleblog.config.security.PrincipalDetails
import com.example.simpleblog.domain.member.Member
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import kotlin.test.Test

@SpringBootTest
class UtilTest {
    val log = KotlinLogging.logger { }

    val objectMapper = ObjectMapper()

    @Test
    fun bcryptEncodeTest() {
        val encoder = BCryptPasswordEncoder()

        val encPassword = encoder.encode("1234")
        log.info { encPassword }
    }

    @Test
    fun generateJwtTest() {
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val jwtManager = JwtManager(accessTokenExpireSeconds = 60)

        val details = PrincipalDetails(Member.createFakeMember(1))
        val jsonPrincipal = objectMapper.writeValueAsString(details)
        val accessToken = jwtManager.generateAccessToken(jsonPrincipal)

        val jwtString = jwtManager.validatedJwt(accessToken)

        val principal = jwtString.getClaim(CLAIM_PRINCIPAL).asString()
        val principalDetails: PrincipalDetails = objectMapper.readValue(principal, PrincipalDetails::class.java)

        log.info("principalDetails: $principalDetails.member")
    }
}