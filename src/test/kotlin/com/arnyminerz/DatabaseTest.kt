package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.types.UserType
import com.arnyminerz.security.Passwords
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class DatabaseTest : DatabaseTestProto() {
    @Test
    fun test_users() = runBlocking {
        ServerDatabase.instance.usersInterface.getAll {
            assertTrue(it.empty())
        }

        ServerDatabase.instance.usersInterface.new(
            UserType("12345678Z", "Testing", "User", "example@mail.com"),
            "password" to "password"
        )

        ServerDatabase.instance.usersInterface.getAll {
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
