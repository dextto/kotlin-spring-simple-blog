package com.example.simpleblog.config.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.JWTVerifier
import mu.KotlinLogging
import java.util.*
import java.util.concurrent.TimeUnit

const val AUTH_HEADER = "Authorization"
const val AUTH_TYPE = "Bearer"

// TODO: env
const val SECRET_KEY = "SECRET!!"
const val CLAIM_EMAIL = "email"
const val CLAIM_PRINCIPAL = "principal"
const val JWT_SUBJECT = "my-token"

class JwtManager(
    val accessTokenExpireSeconds: Long = 300,
) {
    private val log = KotlinLogging.logger { }

    fun generateAccessToken(principal: String): String {
        val expireDate = Date(System.nanoTime() + TimeUnit.SECONDS.toMillis(accessTokenExpireSeconds))

        return JWT.create()
            .withSubject(JWT_SUBJECT)
            .withExpiresAt(expireDate)
            .withClaim(CLAIM_PRINCIPAL, principal)
            .sign(Algorithm.HMAC512(SECRET_KEY))
            .toString()
    }

    fun getMemberEmail(token: String): String? {
        return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token).getClaim(CLAIM_EMAIL).asString()
    }

    fun validatedJwt(accessToken: String): DecodedJWT {
        try {
            val verifier: JWTVerifier = JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
            val jwt: DecodedJWT = verifier.verify(accessToken)

            return jwt
        } catch (e: JWTVerificationException) {
            log.error { "error: ${e.stackTraceToString()}" }

            throw RuntimeException("Invalid jwt")
        }
    }
}
