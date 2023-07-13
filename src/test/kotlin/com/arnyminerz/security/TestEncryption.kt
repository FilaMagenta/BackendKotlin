package com.arnyminerz.security

import com.arnyminerz.filamagenta.commons.data.security.Encryption
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import kotlin.test.Test
import kotlin.test.assertEquals

class TestEncryption {
    @Test
    fun `test encryption and decryption on random key`() {
        val key = RSAKeyPairGenerator.newKey()
        val data = "some data to encrypt"
        val dataBytes = data.toByteArray(Charsets.UTF_8)

        val encrypted = Encryption.encrypt(key.public, dataBytes)
        val decrypted = Encryption.decrypt(key.private, encrypted)

        assertEquals(data, decrypted.toString(Charsets.UTF_8))
    }
}
