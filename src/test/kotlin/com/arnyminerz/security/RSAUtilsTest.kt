package com.arnyminerz.security

import com.arnyminerz.utils.getRSAPrivateKey
import com.arnyminerz.utils.getRSAPublicKey
import com.arnyminerz.utils.toJSON
import kotlin.test.assertEquals
import org.json.JSONObject
import org.junit.Test

class RSAUtilsTest {
    @Test
    fun `test JSON RSA Keys conversion`() {
        val keyPair = RSAKeyPairGenerator.newKey()
        val json = JSONObject().apply {
            put("public", keyPair.public.toJSON())
            put("private", keyPair.private.toJSON())
        }

        val publicKey = json.getRSAPublicKey("public")
        assertEquals(keyPair.public, publicKey)

        val privateKey = json.getRSAPrivateKey("private")
        assertEquals(keyPair.private, privateKey)
    }
}
