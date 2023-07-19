package com.arnyminerz.database

import com.arnyminerz.DatabaseTestProto
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import com.arnyminerz.filamagenta.commons.data.types.EventType
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class DatabaseEventsTest: DatabaseTestProto() {
    val sampleEvent = RSAKeyPairGenerator.newKey().let { keyPair ->
        EventType(
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
    }

    @Test
    fun `test events creation`() = runBlocking {
        eventsInterface.getAll {
            assertTrue(it.empty())
        }

        eventsInterface.new(sampleEvent)

        eventsInterface.getAll {
            assertEquals(1, it.count())
            val item = it.first()
            assertEquals(sampleEvent.name, item.name)
            assertEquals(sampleEvent.description, item.description)
            assertEquals(sampleEvent.date.toString(), item.date)
            assertNull(item.until)
            assertNull(item.reservations)
        }
    }

    @Test
    fun `test multiple event prices`() {
        runBlocking {
            // First create the event to test on
            val event = eventsInterface.new(sampleEvent)

            eventsInterface.setEventPrice(event, Category.UNKNOWN, 10.0)
            eventsInterface.setEventPrice(event, Category.FESTER, 20.0)

            // Setting a price for a category that already has a price is a mistake
            assertFailsWith(IllegalStateException::class) {
                eventsInterface.setEventPrice(event, Category.UNKNOWN, 10.0)
            }
        }
    }

    @Test
    fun `test fetching event prices`() {
        runBlocking {
            // First create the event to test on
            val event = eventsInterface.new(sampleEvent)

            eventsInterface.setEventPrice(event, Category.UNKNOWN, 10.0)
            eventsInterface.setEventPrice(event, Category.FESTER, 20.0)

            val prices = transaction(database) { event.prices }
            assertEquals(10.0, prices[Category.UNKNOWN])
            assertEquals(20.0, prices[Category.FESTER])
        }
    }
}