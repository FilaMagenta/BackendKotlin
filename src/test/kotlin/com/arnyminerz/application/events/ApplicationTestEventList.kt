package com.arnyminerz.application.events

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.database.types.EventType
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestEventList: ApplicationTestProto() {
    @Test
    fun test_events() = testLoggedIn { token ->
        client.get("/v1/events") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertNotNull(data["events"])
                val events = data.getJSONArray("events")
                assertTrue(events.isEmpty)
            }
        }
    }
}