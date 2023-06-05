package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.types.EventType
import com.arnyminerz.database.types.UserType
import com.arnyminerz.security.Passwords
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse
import kotlin.test.assertNull
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

    @Test
    fun test_events() = runBlocking {
        ServerDatabase.instance.eventsInterface.getAll {
            assertTrue(it.empty())
        }

        val create = EventType(
            "Test Event",
            "Test event Description",
            ZonedDateTime.of(2023, 5, 10, 12, 5, 0, 0, ZoneOffset.UTC),
            null,
            null
        )

        ServerDatabase.instance.eventsInterface.new(create)

        ServerDatabase.instance.eventsInterface.getAll {
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
