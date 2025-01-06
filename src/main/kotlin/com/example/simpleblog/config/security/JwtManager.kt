package com.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import mu.KotlinLogging
import java.util.*

const val JWT_HEADER = "Authorization"
const val AUTH_TYPE = "Bearer"

// TODO: env
const val SECRET_KEY = "SECRET!!"
const val CLAIM_EMAIL: String = "email"
const val CLAIM_PASSWORD: String = "password"

class JwtManager {

    private val log = KotlinLogging.logger { }

    fun generateAccessToken(principal: PrincipalDetails): String {
        return JWT.create()
            .withSubject(principal.username)
            .withExpiresAt(Date(System.nanoTime() + 1000 * 60 * 60))
            .withClaim(CLAIM_EMAIL, principal.username)
            .withClaim(CLAIM_PASSWORD, principal.password)
            .sign(Algorithm.HMAC512(SECRET_KEY))
            .toString()
    }

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getClaim(CLAIM_EMAIL).asString()
    }
}
