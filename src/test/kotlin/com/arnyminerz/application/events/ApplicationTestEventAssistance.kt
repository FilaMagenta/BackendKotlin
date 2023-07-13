package com.arnyminerz.application.events

import com.arnyminerz.database.dsl.Events
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestEventAssistance : ApplicationTestEventProto() {
    private suspend fun ApplicationTestBuilder.confirmAssistance(
        token: String,
        eventId: Int,
        assertion: suspend HttpResponse.() -> Unit = { assertSuccess(HttpStatusCode.Accepted) }
    ) = client.put("/v1/events/$eventId/assistance") {
        header("Authorization", "Bearer $token")
    }.apply { assertion() }

    @Test
    fun test_event_confirmAssistance() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

        // Check that the user is not assisting
        getFirstEvent(token) { assertFalse(it.getBoolean("assists")) }

        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            confirmAssistance(token, event.id.value)
        }

        // Check that now the user is assisting
        getFirstEvent(token) { assertTrue(it.getBoolean("assists")) }
    }

    @Test
    fun test_event_confirmAssistance_double() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

        // Confirm assistance once
        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            confirmAssistance(token, event.id.value)
        }

        // Confirm assistance again
        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            confirmAssistance(token, event.id.value) {
                assertFailure(Errors.AssistanceAlreadyConfirmed)
            }
        }
    }

    @Test
    fun test_event_rejectAssistance() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

        // Check that the user is not assisting
        getFirstEvent(token) { assertFalse(it.getBoolean("assists")) }

        // Now confirm assistance
        getFirstEvent(token) { eventJson ->
            val event = eventJson.let { Events.fromJson(it) }!!

            confirmAssistance(token, event.id.value)
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

    @Test
    fun test_event_rejectAssistance_notConfirmed() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

        // Check that the user is not assisting
        getFirstEvent(token) { assertFalse(it.getBoolean("assists")) }

        // Try canceling the assistance
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
