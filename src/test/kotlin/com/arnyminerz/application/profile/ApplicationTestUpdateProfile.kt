package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.filamagenta.commons.utils.getStringOrNull
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
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

    @Test
    fun `test changing to an invalid category`() = testLoggedInAdmin { token ->
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
                    "category" to "invalid-category"
                ).toString()
            )
        }.apply {
            assertFailure(Errors.CategoryInvalid)
        }
    }

    @Test
    fun `test changing category of unknown user`() = testLoggedInAdmin { token ->
        client.post("/v1/profile/1000/category") {
            header("Authorization", "Bearer $token")
            setBody(
                jsonOf(
                    "category" to "invalid-category"
                ).toString()
            )
        }.apply {
            assertFailure(Errors.UserNotFound)
        }
    }
}
