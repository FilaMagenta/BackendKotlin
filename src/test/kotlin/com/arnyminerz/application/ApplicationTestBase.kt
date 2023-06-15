package com.arnyminerz.application

import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import org.junit.Test

class ApplicationTestBase : ApplicationTestProto() {
    @Test
    fun test_notFound() = test {
        client.get("/").apply {
            assertFailure(Errors.EndpointNotFound)
        }
    }

    @Test
    fun test_apiRoot() = test {
        client.get("/v1").apply { assertSuccess() }
        client.post("/v1").apply { assertSuccess() }
        client.get("/v1/").apply { assertSuccess() }
        client.post("/v1/").apply { assertSuccess() }
    }

    @Test
    fun test_wellKnown_jwks() = test {
        client.get("/certs/jwks.json").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(
                this::class.java.getResourceAsStream("/certs/jwks.json")?.use { it.bufferedReader().readText() },
                bodyAsText()
            )
        }
    }
}
