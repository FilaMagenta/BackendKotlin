package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestGetAllProfiles : ApplicationTestProto() {
    @Test
    fun `test getting users list as admin - double`() = testDoubleLoggedInAdmin { _, tokenAdmin ->
        client.get("/v1/profile/all") {
            header("Authorization", "Bearer $tokenAdmin")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                assertTrue(it.has("users"))
                val users = it.getJSONArray("users")
                assertEquals(2, users.length())
            }
        }
    }

    @Test
    fun `test getting users list as admin - single`() = testLoggedInAdmin { tokenAdmin ->
        client.get("/v1/profile/all") {
            header("Authorization", "Bearer $tokenAdmin")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                assertTrue(it.has("users"))
                val users = it.getJSONArray("users")
                assertEquals(1, users.length())
            }
        }
    }

    @Test
    fun `test getting users list forbidden`() = testLoggedIn { token ->
        client.get("/v1/profile/all") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}
