package com.arnyminerz.security

import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import java.security.spec.InvalidKeySpecException
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object Passwords {
    private const val SALT_SIZE = 16
    private const val KEY_ITERATION_COUNT = 1000
    private const val KEY_LENGTH = 256

    private val random = SecureRandom()

    fun generateSalt(): ByteArray {
        val salt = ByteArray(SALT_SIZE)
        random.nextBytes(salt)
        return salt
    }

    fun isExpectedPassword(password: String, salt: ByteArray, expectedHash: ByteArray): Boolean {
        val pwdHash = hash(password, salt)
        if (pwdHash.size != expectedHash.size) return false
        return pwdHash.indices.all { pwdHash[it] == expectedHash[it] }
    }

    fun hash(password: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, KEY_ITERATION_COUNT, KEY_LENGTH)
        try {
            val skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
            return skf.generateSecret(spec).encoded
        } catch (e: NoSuchAlgorithmException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } catch (e: InvalidKeySpecException) {
            throw AssertionError("Error while hashing a password: " + e.message, e)
        } finally {
            spec.clearPassword()
        }
    }
}
