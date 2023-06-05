package com.arnyminerz.application.events

import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.delete
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
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

    @Test
    fun `events table joining`() = testDoubleLoggedIn { token1, token2 ->
        provideSampleEvent(token1)

        getFirstEvent(token1) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty)
        }

        // Create a new table as the user for responsible
        getFirstEvent(token1) { event ->
            client.post("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token1")
            }.apply {
                assertSuccess(HttpStatusCode.Created)
            }
        }

        // Join the created table
        getFirstEvent(token2) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)

            client.put("/v1/events/${event.getInt("id")}/table/${table.getInt("id")}") {
                header("Authorization", "Bearer $token2")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that the new member can be fetched
        getFirstEvent(token1) { event ->
            val secondUser = usersInterface.findWithNif(registerSampleData2.getValue("nif")) { it!! }

            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)
            val members = table.getJSONArray("members")
            assertEquals(1, members.count())
            assertEquals(secondUser.id.value, members.getInt(0))
        }
    }

    @Test
    fun `events table leaving - member`() = testDoubleLoggedIn { token1, token2 ->
        provideSampleEvent(token1)

        getFirstEvent(token1) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty)
        }

        // Create a new table as the user for responsible
        getFirstEvent(token1) { event ->
            client.post("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token1")
            }.apply {
                assertSuccess(HttpStatusCode.Created)
            }
        }

        // Join the created table
        getFirstEvent(token2) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)

            client.put("/v1/events/${event.getInt("id")}/table/${table.getInt("id")}") {
                header("Authorization", "Bearer $token2")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Leave the table as member
        getFirstEvent(token2) { event ->
            client.delete("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token2")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that the table now has no members
        getFirstEvent(token1) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)
            val members = table.getJSONArray("members")
            assertTrue(members.isEmpty)
        }
    }

    @Test
    fun `events table leaving - responsible`() = testDoubleLoggedIn { token1, token2 ->
        provideSampleEvent(token1)

        getFirstEvent(token1) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty)
        }

        // Create a new table as the user for responsible
        getFirstEvent(token1) { event ->
            client.post("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token1")
            }.apply {
                assertSuccess(HttpStatusCode.Created)
            }
        }

        // Join the created table
        getFirstEvent(token2) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)

            client.put("/v1/events/${event.getInt("id")}/table/${table.getInt("id")}") {
                header("Authorization", "Bearer $token2")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Leave the table as responsible
        getFirstEvent(token1) { event ->
            client.delete("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token1")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that now there are no tables
        getFirstEvent(token1) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty, "Tables is not empty: $tables")
        }
    }
}