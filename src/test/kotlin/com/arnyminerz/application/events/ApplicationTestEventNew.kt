package com.arnyminerz.application.events

import com.arnyminerz.utils.getStringOrNull
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import org.junit.Test

class ApplicationTestEventNew: ApplicationTestEventProto() {

    @Test
    fun test_events_create() = testLoggedIn { token ->
        provideSampleEvent(token)

        getAllEvents(token) { events ->
            assertEquals(1, events.length())

            val event = events.getJSONObject(0)
            assertEquals(eventSampleData.name, event.getString("name"))
            assertEquals(eventSampleData.description, event.getString("description"))
            assertEquals(eventSampleData.date.toString(), event.getString("date"))
            assertNull(event.getStringOrNull("until"))
            assertNull(event.getStringOrNull("reservations"))

            // At first, assistance is not confirmed
            assertFalse(event.getBoolean("assists"))
        }
    }
}