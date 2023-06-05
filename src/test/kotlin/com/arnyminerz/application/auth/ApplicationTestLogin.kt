package com.arnyminerz.application.auth

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import org.junit.Test

class ApplicationTestLogin: ApplicationTestProto() {
    @Test
    fun test_login_correct() = test {
        // First register the user
        register(registerSampleData) { assertSuccess(HttpStatusCode.Created) }

        // Now log in
        login("12345678Z", "password123") {
            assertSuccess()
        }
    }
}