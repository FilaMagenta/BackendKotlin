package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.permissions.Role
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.getIntOrNull
import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.jsonOf
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test

class ApplicationTestSetRole : ApplicationTestProto() {
    @Test
    fun `test changing role`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        var userId: Int? = null
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                userId = it.getIntOrNull("id")
                assertEquals(Role.MEMBER.name, it.getStringOrNull("role"))
            }
        }
        assertNotNull(userId)

        client.post("/v1/profile/$userId/role") {
            header("Authorization", "Bearer $tokenAdmin")
            setBody(
                jsonOf("role" to Role.ADMIN.name).toString()
            )
        }.apply {
            assertSuccess()
        }

        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                assertEquals(Role.ADMIN.name, it.getStringOrNull("role"))
            }
        }
    }

    @Test
    fun `test changing role forbidden`() = testLoggedIn { token ->
        var userId: Int? = null
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                userId = it.getIntOrNull("id")
            }
        }
        assertNotNull(userId)

        client.post("/v1/profile/$userId/role") {
            header("Authorization", "Bearer $token")
            setBody(
                jsonOf("role" to Role.ADMIN.name).toString()
            )
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}
