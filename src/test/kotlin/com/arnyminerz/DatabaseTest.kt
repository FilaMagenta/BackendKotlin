package com.arnyminerz

import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.security.Passwords
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.data.types.EventType
import com.arnyminerz.filamagenta.commons.data.types.UserType
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
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
            UserType(0, "12345678Z", Category.FESTER, Role.DEFAULT, "Testing", "User", "example@mail.com"),
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

    @Test
    fun test_events() = runBlocking {
        eventsInterface.getAll {
            assertTrue(it.empty())
        }

        val keyPair = RSAKeyPairGenerator.newKey()
        val create = EventType(
            0,
            ZonedDateTime.of(2023, 10, 13, 15, 10, 0, 0, ZoneId.of("UTC")),
            "Test Event",
            "Test event Description",
            ZonedDateTime.of(2023, 5, 10, 12, 5, 0, 0, ZoneOffset.UTC),
            null,
            null,
            -1,
            keyPair.public,
            keyPair.private
        )

        eventsInterface.new(create)

        eventsInterface.getAll {
            assertEquals(1, it.count())
            val item = it.first()
            assertEquals(create.name, item.name)
            assertEquals(create.description, item.description)
            assertEquals(create.date.toString(), item.date)
            assertNull(item.until)
            assertNull(item.reservations)
        }
    }
}
