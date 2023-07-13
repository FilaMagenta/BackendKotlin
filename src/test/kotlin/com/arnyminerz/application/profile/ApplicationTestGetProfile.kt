package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.utils.getStringOrNull
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test

class ApplicationTestGetProfile : ApplicationTestProto() {
    @Test
    fun test_getProfileData() = testLoggedIn { token ->
        client.get("/v1/profile") {
            header("Authorization", "Bearer $token")
        }.apply {
            assertSuccess {
                assertNotNull(it)
                println("Response: $it")
                assertEquals(registerSampleData["nif"], it.getStringOrNull("nif"))
                assertEquals(Role.MEMBER.name, it.getStringOrNull("role"))
                assertEquals(registerSampleData["name"], it.getStringOrNull("name"))
                assertEquals(registerSampleData["surname"], it.getStringOrNull("surname"))
                assertEquals(registerSampleData["email"], it.getStringOrNull("email"))
                assertEquals(registerSampleData["birthday"], it.getStringOrNull("birthday"))
                assertEquals(Category.UNKNOWN.name, it.getStringOrNull("category"))
            }
        }
    }
}
