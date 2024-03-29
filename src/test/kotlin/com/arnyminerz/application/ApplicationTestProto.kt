package com.arnyminerz.application

import com.arnyminerz.DatabaseTestProto
import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.utils.getStringOrNull
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.installModules
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.assertNotNull
import org.json.JSONObject

abstract class ApplicationTestProto : DatabaseTestProto() {
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

    protected fun testLoggedInDefault(assertion: suspend ApplicationTestBuilder.(token: String) -> Unit) = test {
        provideSampleUser {
            loginWithDefault {
                assertion(this@test, it)
            }
        }
    }

    protected fun testLoggedInAdmin(assertion: suspend ApplicationTestBuilder.(token: String) -> Unit) = test {
        provideSampleUser {
            loginWithAdminUser {
                assertion(this@test, it)
            }
        }
    }

    protected fun testDoubleLoggedIn(
        assertion: suspend ApplicationTestBuilder.(token1: String, token2: String) -> Unit
    ) = test {
        provideSampleUser {
            provideSampleUser(registerSampleData2) {
                loginWithSampleUser { token1 ->
                    loginWithSampleUser(registerSampleData2) { token2 ->
                        assertion(this@test, token1, token2)
                    }
                }
            }
        }
    }

    protected fun testDoubleLoggedInAdmin(
        assertion: suspend ApplicationTestBuilder.(token: String, tokenAdmin: String) -> Unit
    ) = test {
        provideSampleUser {
            provideSampleUser(registerSampleData2) {
                loginWithSampleUser { token ->
                    loginWithAdminUser(registerSampleData2) { tokenAdmin ->
                        assertion(this@test, token, tokenAdmin)
                    }
                }
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

    protected val registerSampleData2 = mapOf(
        "nif" to "87654321X",
        "name" to "Another",
        "surname" to "User",
        "email" to "example2@mail.com",
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
        role: Role? = Role.MEMBER,
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

                role?.let {
                    usersInterface.findWithNif(nif) { user ->
                        assertNotNull(user)
                        user.role = it
                    }
                    ServerDatabase.instance.flushCache()
                }

                block(data.getString("token"))
            }
        }
    }

    private suspend fun ApplicationTestBuilder.provideSampleUser(
        data: Map<String, String> = registerSampleData,
        assertion: suspend HttpResponse.() -> Unit
    ) {
        register(data, assertion)
    }

    private suspend fun ApplicationTestBuilder.loginWithDefault(
        data: Map<String, String> = registerSampleData,
        assertion: suspend HttpResponse.(token: String) -> Unit
    ) {
        login(data.getValue("nif"), data.getValue("password"), null, assertion)
    }

    private suspend fun ApplicationTestBuilder.loginWithSampleUser(
        data: Map<String, String> = registerSampleData,
        assertion: suspend HttpResponse.(token: String) -> Unit
    ) {
        login(data.getValue("nif"), data.getValue("password"), Role.MEMBER, assertion)
    }

    private suspend fun ApplicationTestBuilder.loginWithAdminUser(
        data: Map<String, String> = registerSampleData,
        assertion: suspend HttpResponse.(token: String) -> Unit
    ) {
        login(data.getValue("nif"), data.getValue("password"), Role.ADMIN, assertion)
    }
}
