package com.arnyminerz.application.inventory

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.json.JSONObject
import org.junit.Test

class ApplicationTestInventoryList : ApplicationTestProto() {
    @Test
    fun `test get inventory items`() = testLoggedIn { token ->
        client.get("/v1/inventory") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                val items = data.getJSONArray("items")
                assertTrue(items.isEmpty)
            }
        }
    }

    @Test
    fun `test create inventory item - no permission`() = testLoggedIn { token ->
        client.post("/v1/inventory") {
            header(HttpHeaders.Authorization, "Bearer $token")

            val body = JSONObject().apply {
                put("name", "Testing item")
                put("price", "10")
            }
            setBody(body.toString())
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }

    @Test
    fun `test create inventory item - correct`() = testLoggedInAdmin { token ->
        client.post("/v1/inventory") {
            header(HttpHeaders.Authorization, "Bearer $token")

            val body = JSONObject().apply {
                put("name", "Testing item")
                put("price", "10")
            }
            setBody(body.toString())
        }.apply {
            assertSuccess(HttpStatusCode.Created)
        }

        client.get("/v1/inventory") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                val items = data.getJSONArray("items")
                assertEquals(1, items.count())

                val item = items.getJSONObject(0)
                assertEquals("Testing item", item.getString("name"))
                assertEquals(10f, item.getFloat("unit_price"))
            }
        }
    }
}
