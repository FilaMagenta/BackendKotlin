package com.arnyminerz.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.ZonedDateTime

object Authentication {
    lateinit var audience: String

    lateinit var issuer: String

    lateinit var secret: String

    fun getAlgorithm(): Algorithm =
        Algorithm.HMAC256(secret)

    /**
     * Creates a new JWT token for the given NIF.
     */
    fun newToken(nif: String, expiration: ZonedDateTime): String =
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("nif", nif)
            .withExpiresAt(expiration.toInstant())
            .sign(getAlgorithm())
}