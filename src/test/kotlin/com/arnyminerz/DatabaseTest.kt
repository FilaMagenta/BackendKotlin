package com.arnyminerz

import com.arnyminerz.database.DatabaseSamples.sampleUser
import com.arnyminerz.filamagenta.commons.data.security.Passwords
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test

class DatabaseTest : DatabaseTestProto() {
    @Test
    fun test_users() = runBlocking {
        usersInterface.getAll {
            assertTrue(it.empty())
        }

        usersInterface.new(
            sampleUser,
            "password" to "password"
        )

        usersInterface.getAll {
            assertEquals(1, it.count())
            val user = it.first()
            assertEquals("12345678Z", user.nif)
            assertEquals("Testing", user.name)
            assertEquals("User", user.surname)
            assertEquals("example@mail.com", user.email)

            val salt = Base64.getMimeDecoder().decode(user.passwordSalt)
            val hash = Base64.getMimeDecoder().decode(user.passwordHash)
            assertTrue(Passwords.isExpectedPassword("password", salt, hash))
            assertFalse(Passwords.isExpectedPassword("wrong", salt, hash))
        }
    }
}
