package com.arnyminerz.application.server

import com.arnyminerz.Information
import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.utils.assertSuccess
import com.arnyminerz.utils.getStringOrNull
import io.ktor.client.request.get
import kotlin.test.assertEquals
import org.junit.Test

class ApplicationTestServerInformation : ApplicationTestProto() {
    @Test
    fun `check there is valid version`() {
        val version = Information.version
        assert(version.isNotBlank())
    }

    @Test
    fun `test server information`() = test {
        client.get("/v1/info").apply {
            assertSuccess {
                val version = it?.getStringOrNull("version")
                assertEquals(Information.version, version)
            }
        }
    }
}
