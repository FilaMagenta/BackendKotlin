package com.arnyminerz.application.events

import com.arnyminerz.database.dsl.Events
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.http.HttpStatusCode
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestEventAssistance: ApplicationTestEventProto() {
    @Test
    fun test_event_confirmAssistance() = testLoggedIn { token ->
        provideSampleEvent(token)

        // Check that the user is not assisting
        getAllEvents(token) { events ->
            val event = events.getJSONObject(0)
            assertFalse(event.getBoolean("assists"))
        }

        getAllEvents(token) { events ->
            val event = events.getJSONObject(0)?.let { Events.fromJson(it) }!!

            client.put("/v1/events/${event.id}/assistance") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that now the user is assisting
        getAllEvents(token) { events ->
            val event = events.getJSONObject(0)
            assertTrue(event.getBoolean("assists"))
        }
    }
}