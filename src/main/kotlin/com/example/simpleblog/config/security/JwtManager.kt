package com.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.TimeUnit

const val AUTH_HEADER = "Authorization"
const val AUTH_TYPE = "Bearer"

// TODO: env
const val SECRET_KEY = "SECRET!!"
const val CLAIM_EMAIL = "email"
const val CLAIM_PASSWORD = "password"
const val ACCESS_TOKEN_EXPIRE_MINUTES: Long = 5

class JwtManager {

    private val log = KotlinLogging.logger { }

    fun generateAccessToken(principal: PrincipalDetails): String {
        val expireDate = Date(System.nanoTime() + TimeUnit.MINUTES.toMillis(ACCESS_TOKEN_EXPIRE_MINUTES))

        return JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(expireDate)
            .withClaim(CLAIM_EMAIL, principal.username)
            .withClaim(CLAIM_PASSWORD, principal.password)
            .sign(Algorithm.HMAC512(SECRET_KEY))
            .toString()
    }

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getClaim(CLAIM_EMAIL).asString()
    }
}
