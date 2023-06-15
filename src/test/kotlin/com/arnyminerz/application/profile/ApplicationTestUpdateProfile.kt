package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.data.Category
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.jsonOf
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestUpdateProfile : ApplicationTestProto() {
    @Test
    fun `test changing user category`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        // Make sure at first the user doesn't have a category, and store the user id
        var targetUserId = 0
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertEquals(Category.UNKNOWN.name, data.getStringOrNull("category"))
                targetUserId = data.getInt("id")
            }
        }
        assertTrue(targetUserId > 0)

        // Now update the user's category
        client.post("/v1/profile/$targetUserId/category") {
            header("Authorization", "Bearer $tokenAdmin")
            setBody(
                jsonOf(
                    "category" to Category.FESTER.name
                ).toString()
            )
        }.apply {
            assertSuccess()
        }

        // And check again for the category
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertEquals(Category.FESTER.name, data.getStringOrNull("category"))
            }
        }

        // Change to a different category
        client.post("/v1/profile/$targetUserId/category") {
            header("Authorization", "Bearer $tokenAdmin")
            setBody(
                jsonOf(
                    "category" to Category.JUBILAT
                ).toString()
            )
        }.apply {
            assertSuccess()
        }

        // And check again for the category
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                assertEquals(Category.JUBILAT.name, data.getStringOrNull("category"))
            }
        }
    }

    @Test
    fun `test that regular users cannot change categories`() = testLoggedIn { token ->
        var targetUserId = 0
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                targetUserId = data.getInt("id")
            }
        }
        assertTrue(targetUserId > 0)

        client.post("/v1/profile/$targetUserId/category") {
            header("Authorization", "Bearer $token")
            setBody(
                jsonOf(
                    "category" to Category.FESTER.name
                ).toString()
            )
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}
