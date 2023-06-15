package com.arnyminerz.security

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

object RSAKeyPairGenerator {
    private const val MIN_KEY_LENGTH = 2048

    /**
     * Generates a new random RSA key.
     * @param size The size of the key to generate.
     * @throws SecurityException if provided a [size] lower than [MIN_KEY_LENGTH].
     */
    fun newKey(size: Int = 2048, random: SecureRandom = SecureRandom()): KeyPair {
        if (size < MIN_KEY_LENGTH) throw SecurityException("Key size must be greater or equal to $MIN_KEY_LENGTH.")
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(size, random)
        return generator.generateKeyPair()
    }
}
