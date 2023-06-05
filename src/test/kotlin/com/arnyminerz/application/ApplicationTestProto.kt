package com.arnyminerz.application

import com.arnyminerz.DatabaseTestProto
import com.arnyminerz.installModules
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.jsonOf
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.server.util.url
import kotlin.test.assertNotNull
import org.json.JSONObject

abstract class ApplicationTestProto: DatabaseTestProto() {
    protected fun test(block: suspend ApplicationTestBuilder.() -> Unit) = testApplication {
        application {
            installModules()
        }
        block()
    }

    protected fun testLoggedIn(assertion: suspend ApplicationTestBuilder.(token: String) -> Unit) = test {
        provideSampleUser {
            loginWithSampleUser {
                assertion(this@test, it)
            }
        }
    }

    protected val registerSampleData = mapOf(
        "nif" to "12345678Z",
        "name" to "Testing",
        "surname" to "User",
        "email" to "example@mail.com",
        "password" to "password123"
    ).toMutableMap()

    protected suspend fun ApplicationTestBuilder.register(
        bodyData: Map<String, String>,
        assertion: suspend HttpResponse.() -> Unit
    ) {
        client.post("/v1/auth/register") {
            val body = JSONObject(bodyData)
            setBody(body.toString())
        }.apply { assertion(this) }
    }

    protected suspend fun ApplicationTestBuilder.login(
        nif: String,
        password: String,
        block: suspend HttpResponse.(token: String) -> Unit
    ) {
        val bodyData = jsonOf(
            "nif" to nif,
            "password" to password
        )
        client.post("/v1/auth/login") {
            setBody(bodyData.toString())
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertNotNull(data.getStringOrNull("token"))
                block(data.getString("token"))
            }
        }
    }

    protected suspend fun ApplicationTestBuilder.provideSampleUser(assertion: suspend HttpResponse.() -> Unit) {
        register(registerSampleData, assertion)
    }

    protected suspend fun ApplicationTestBuilder.loginWithSampleUser(assertion: suspend HttpResponse.(token: String) -> Unit) {
        login(registerSampleData.getValue("nif"), registerSampleData.getValue("password"), assertion)
    }
}