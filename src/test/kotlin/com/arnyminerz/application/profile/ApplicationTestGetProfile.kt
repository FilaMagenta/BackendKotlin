package com.arnyminerz.application.profile

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import org.junit.Test

class ApplicationTestGetProfile: ApplicationTestProto() {
    @Test
    fun test_getProfileData() = test {
        provideSampleUser {
            loginWithSampleUser {
                client.get("/v1/profile") {
                    assertSuccess()
                }
            }
        }
    }
}