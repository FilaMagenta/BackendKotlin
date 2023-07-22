package com.arnyminerz.application.events

import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.filamagenta.commons.utils.getJSONObjectOrNull
import com.arnyminerz.filamagenta.commons.utils.getStringOrNull
import com.arnyminerz.utils.assertFailure
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import org.junit.Test

class ApplicationTestEventNew : ApplicationTestEventProto() {

    @Test
    fun `test creating event - admin`() = testLoggedInAdmin { token ->
        provideSampleEvent(token)

        getAllEvents(token) { events ->
            assertEquals(1, events.length())

            val event = events.getJSONObject(0)
            assertNotNull(event.getInt("id"))
            assertEquals(eventSampleData.name, event.getString("name"))
            assertEquals(eventSampleData.description, event.getString("description"))
            assertEquals(eventSampleData.date.toEpochMilli(), event.getLong("date"))
            assertNull(event.getStringOrNull("until"))
            assertNull(event.getStringOrNull("reservations"))
            assertNull(event.getJSONObject("key_pair").getJSONObjectOrNull("private"))

            // At first, assistance is not confirmed
            assertFalse(event.getBoolean("assists"))
        }
    }

    @Test
    fun `test creating event - no permission`() = testLoggedIn { token ->
        client.post("/v1/events") {
            header("Authorization", "Bearer $token")
            setBody(eventSampleData.toString())
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}
