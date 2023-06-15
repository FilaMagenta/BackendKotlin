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

class ApplicationTestEventTables : ApplicationTestEventProto() {
    @Test
    fun `events table creation`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

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
    fun `events double table creation`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

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
    fun `events table joining`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

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

        // Join the created table
        getFirstEvent(tokenAdmin) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)

            client.put("/v1/events/${event.getInt("id")}/table/${table.getInt("id")}") {
                header("Authorization", "Bearer $tokenAdmin")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that the new member can be fetched
        getFirstEvent(token) { event ->
            val secondUser = usersInterface.findWithNif(registerSampleData2.getValue("nif")) { it!! }

            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)
            val members = table.getJSONArray("members")
            assertEquals(1, members.count())
            assertEquals(secondUser.id.value, members.getInt(0))
        }
    }

    @Test
    fun `events table leaving - member`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

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

        // Join the created table
        getFirstEvent(tokenAdmin) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)

            client.put("/v1/events/${event.getInt("id")}/table/${table.getInt("id")}") {
                header("Authorization", "Bearer $tokenAdmin")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Leave the table as member
        getFirstEvent(tokenAdmin) { event ->
            client.delete("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $tokenAdmin")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that the table now has no members
        getFirstEvent(token) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)
            val members = table.getJSONArray("members")
            assertTrue(members.isEmpty)
        }
    }

    @Test
    fun `events table leaving - responsible`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        provideSampleEvent(tokenAdmin)

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

        // Join the created table
        getFirstEvent(tokenAdmin) { event ->
            val tables = event.getJSONArray("tables")
            val table = tables.getJSONObject(0)

            client.put("/v1/events/${event.getInt("id")}/table/${table.getInt("id")}") {
                header("Authorization", "Bearer $tokenAdmin")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Leave the table as responsible
        getFirstEvent(token) { event ->
            client.delete("/v1/events/${event.getInt("id")}/table") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }
        }

        // Check that now there are no tables
        getFirstEvent(token) { event ->
            val tables = event.getJSONArray("tables")
            assertTrue(tables.isEmpty, "Tables is not empty: $tables")
        }
    }
}
