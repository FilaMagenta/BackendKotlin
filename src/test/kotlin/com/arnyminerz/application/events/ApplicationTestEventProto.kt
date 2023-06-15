package com.arnyminerz.application.events

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.database.types.EventType
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.ApplicationTestBuilder
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertNotNull
import org.json.JSONArray
import org.json.JSONObject

abstract class ApplicationTestEventProto : ApplicationTestProto() {
    protected val eventSampleData = EventType(
        "Testing Event",
        "This is the description of a testing event.",
        ZonedDateTime.of(2023, 10, 5, 23, 30, 0, 0, ZoneOffset.UTC),
        null,
        null
    )

    protected suspend fun ApplicationTestBuilder.provideSampleEvent(token: String) {
        client.post("/v1/events") {
            header("Authorization", "Bearer $token")
            setBody(eventSampleData.toString())
        }.apply {
            println("Headers: " + call.request.headers.entries().joinToString { (k, v) -> "$k = $v" })
            assertSuccess(HttpStatusCode.Created)
        }
    }

    protected suspend fun ApplicationTestBuilder.getAllEvents(
        token: String,
        assertion: suspend (events: JSONArray) -> Unit = {}
    ) {
        client.get("/v1/events") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertNotNull(data["events"])

                val events = data.getJSONArray("events")
                assertion(events)
            }
        }
    }

    protected suspend fun ApplicationTestBuilder.getFirstEvent(
        token: String,
        assertion: suspend (event: JSONObject) -> Unit = {}
    ) {
        getAllEvents(token) {
            assertion(it.getJSONObject(0))
        }
    }
}
