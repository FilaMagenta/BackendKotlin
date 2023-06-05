package com.arnyminerz

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.dsl.Events
import com.arnyminerz.database.entity.Event
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Test

class Serialization: DatabaseTestProto() {
    @Test
    fun test_event_json() = runBlocking {
        ServerDatabase.instance.transaction {
            val event = Event.new {
                name = "testing name"
                description = "testing description"
                date = ZonedDateTime.of(2023, 10, 10, 12, 35, 0, 0, ZoneOffset.UTC).toString()
            }
            val json = event.toJSON()
            val serializedEvent = Events.fromJson(json)
            assertEquals(event, serializedEvent)
        }
    }
}
