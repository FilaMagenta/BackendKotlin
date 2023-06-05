package com.arnyminerz.application.events

import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestEventList: ApplicationTestEventProto() {
    @Test
    fun test_events() = testLoggedIn { token ->
        getAllEvents(token) { events ->
            assertTrue(events.isEmpty)
        }
    }
}