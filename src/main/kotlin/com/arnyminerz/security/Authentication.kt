package com.arnyminerz.security

import com.arnyminerz.utils.io
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.RSAKeyProvider
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.Base64

object Authentication {
    lateinit var audience: String

    lateinit var issuer: String

    lateinit var privateKeyString: String

    private val privateKeySpec by lazy { PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString)) }

    private val rsaKeyPair by lazy { RSAKeyPairGenerator.newKey() }

    private suspend fun getPublicKey(): RSAPublicKey = io {
        // jwkProvider.get("6f8856ed-9189-488f-9011-0ff4b6c08edc").publicKey as RSAPublicKey
        rsaKeyPair.public as RSAPublicKey
    }

    private suspend fun getPrivateKey(): RSAPrivateKey = io {
        // KeyFactory.getInstance("RSA").generatePrivate(privateKeySpec) as RSAPrivateKey
        rsaKeyPair.private as RSAPrivateKey
    }

    lateinit var jwkProvider: JwkProvider

    private suspend fun getAlgorithm(): Algorithm =
        Algorithm.RSA256(getPublicKey(), getPrivateKey())

    /**
     * Creates a new JWT token for the given NIF.
     */
    suspend fun newToken(nif: String, expiration: ZonedDateTime): String =
        JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("nif", nif)
            .withExpiresAt(expiration.toInstant())
            .sign(getAlgorithm())
}