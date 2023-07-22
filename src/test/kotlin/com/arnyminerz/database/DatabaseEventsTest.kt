package com.arnyminerz.database

import com.arnyminerz.DatabaseTestProto
import com.arnyminerz.database.DatabaseSamples.sampleEvent
import com.arnyminerz.filamagenta.commons.data.Category
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Test

class DatabaseEventsTest : DatabaseTestProto() {
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
            assertEquals(sampleEvent.date, item.date)
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
