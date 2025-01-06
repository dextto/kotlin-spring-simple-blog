package com.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.util.*

class JwtManager {
    private val log = KotlinLogging.logger { }

    // TODO: env
    private val secretKey: String = "SECRET!!"
    private val claimEmail: String = "email"
    private val claimPassword: String = "password"

    fun generateAccessToken(principal: PrincipalDetails): String {
        return JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(Date(System.nanoTime() + 1000 * 60 * 60))
            .withClaim(claimEmail, principal.username)
            .withClaim(claimPassword, principal.password)
            .sign(Algorithm.HMAC512(secretKey))
            .toString()
    }

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token).getClaim(claimEmail).asString()
    }
}