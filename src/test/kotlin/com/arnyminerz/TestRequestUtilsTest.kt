package com.arnyminerz

import com.arnyminerz.utils.receiveJson
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.server.application.call
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import java.util.concurrent.TimeoutException
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import org.junit.Test

class TestRequestUtilsTest {
    private suspend fun waitOrTimeout(timeout: Long = 10_000, condition: () -> Boolean) {
        val start = System.currentTimeMillis()
        while (!condition()) {
            if (System.currentTimeMillis() - start < timeout) throw TimeoutException()
            delay(1)
        }
    }

    private fun testEndpoint(body: String, assertion: HttpResponse.(JSONObject) -> Unit) = testApplication {
        var response: JSONObject? = null

        application {
            routing {
                post { response = call.receiveJson() }
            }
        }

        client.post("") { setBody(body) }.apply {
            runBlocking { waitOrTimeout { response != null } }
            val json = response!!
            assertion(this, json)
        }
        response = null
    }

    @Test
    fun test_receiveJson_empty() = testEndpoint("") {
        assertTrue(it.isEmpty)
    }

    @Test
    fun test_receiveJson_invalid() = testEndpoint("invalid-body") {
        assertTrue(it.isEmpty)
    }

    @Test
    fun test_receiveJson_content() = testEndpoint(
        JSONObject().apply {
            put("test", "value")
        }.toString()
    ) {
        assertEquals(1, it.length())
        assertEquals("value", it.getString("test"))
    }
}