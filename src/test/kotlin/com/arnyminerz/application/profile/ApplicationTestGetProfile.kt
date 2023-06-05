package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.getStringOrNull
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test

class ApplicationTestGetProfile: ApplicationTestProto() {
    @Test
    fun test_getProfileData() = test {
        provideSampleUser {
            loginWithSampleUser { token ->
                client.get("/v1/profile") {
                    header("Authorization", "Bearer $token")
                }.apply {
                    assertSuccess {
                        assertNotNull(it)
                        println("Response: $it")
                        assertEquals(registerSampleData["nif"], it.getStringOrNull("nif"))
                        assertEquals(registerSampleData["name"], it.getStringOrNull("name"))
                        assertEquals(registerSampleData["surname"], it.getStringOrNull("surname"))
                        assertEquals(registerSampleData["email"], it.getStringOrNull("email"))
                        assertEquals(registerSampleData["birthday"], it.getStringOrNull("birthday"))
                    }
                }
            }
        }
    }
}
