package com.arnyminerz.application.events

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.database.types.EventType
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.getStringOrNull
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.Test

class ApplicationTestEventNew: ApplicationTestProto() {
    private val eventSampleData = EventType(
        "Testing Event",
        "This is the description of a testing event.",
        ZonedDateTime.of(2023, 10, 5, 23, 30, 0, 0, ZoneOffset.UTC),
        null,
        null
    )

    @Test
    fun test_events_create() = testLoggedIn { token ->
        client.post("/v1/events") {
            header("Authorization", "Bearer $token")
            setBody(eventSampleData.toString())
        }.apply {
            println("Headers: " + call.request.headers.entries().joinToString { (k, v) -> "$k = $v" })
            assertSuccess(HttpStatusCode.Created)
        }
        client.get("/v1/events") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertNotNull(data["events"])

                val events = data.getJSONArray("events")
                assertEquals(1, events.length())

                val event = events.getJSONObject(0)
                assertEquals(eventSampleData.name, event.getString("name"))
                assertEquals(eventSampleData.description, event.getString("description"))
                assertEquals(eventSampleData.date.toString(), event.getString("date"))
                assertNull(event.getStringOrNull("until"))
                assertNull(event.getStringOrNull("reservations"))
            }
        }
    }
}