package com.arnyminerz.application.events

import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestEventTables: ApplicationTestEventProto() {
    @Test
    fun `events table creation`() = testLoggedIn { token ->
        provideSampleEvent(token)

        getFirstEvent(token) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty)
        }

        // Create a new table as the user for responsible
        getFirstEvent(token) { event ->
            client.post("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Created)
            }
        }

        // Now check that the table has been created
        getFirstEvent(token) { event ->
            val tables = event.getJSONArray("tables")
            assertEquals(1, tables.count())

            val user = usersInterface.findWithNif(registerSampleData.getValue("nif")) { it!! }

            val table = tables.getJSONObject(0)
            assertNotNull(table.getInt("id"))
            assertEquals(user.id.value, table.getInt("responsible_id"))
            assertTrue(table.getJSONArray("members").isEmpty)
        }
    }

    @Test
    fun `events double table creation`() = testLoggedIn { token ->
        provideSampleEvent(token)

        getFirstEvent(token) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty)
        }

        // Create a new table as the user for responsible
        getFirstEvent(token) { event ->
            client.post("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Created)
            }
        }

        // Try to create the table again
        getFirstEvent(token) { event ->
            client.post("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertFailure(Errors.UserAlreadyInTable)
            }
        }
    }
}