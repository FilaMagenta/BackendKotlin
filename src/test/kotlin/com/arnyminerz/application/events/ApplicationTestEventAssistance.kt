package com.arnyminerz.application.events

import com.arnyminerz.database.dsl.Events
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.delete
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
        getFirstEvent(token) { assertFalse(it.getBoolean("assists")) }

        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            client.put("/v1/events/${event.id}/assistance") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that now the user is assisting
        getFirstEvent(token) { assertTrue(it.getBoolean("assists")) }
    }

    @Test
    fun test_event_confirmAssistance_rejection() = testLoggedIn { token ->
        provideSampleEvent(token)

        // Check that the user is not assisting
        getFirstEvent(token) { assertFalse(it.getBoolean("assists")) }

        // Now confirm assistance
        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            client.put("/v1/events/${event.id}/assistance") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that now the user is assisting
        getFirstEvent(token) { assertTrue(it.getBoolean("assists")) }

        // Cancel the assistance
        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            client.delete("/v1/events/${event.id}/assistance") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that the user is not assisting
        getFirstEvent(token) { assertFalse(it.getBoolean("assists")) }
    }
}