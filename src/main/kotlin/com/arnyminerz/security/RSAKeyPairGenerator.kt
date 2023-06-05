package com.arnyminerz.security

import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom

object RSAKeyPairGenerator {
    fun newKey(size: Int = 2048, random: SecureRandom = SecureRandom()): KeyPair {
        val generator = KeyPairGenerator.getInstance("RSA")
        generator.initialize(size, random)
        return generator.generateKeyPair()
    }
}
