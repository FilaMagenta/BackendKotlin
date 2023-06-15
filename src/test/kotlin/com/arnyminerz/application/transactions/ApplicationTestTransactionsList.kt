package com.arnyminerz.application.transactions

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.junit.Test

class ApplicationTestTransactionsList : ApplicationTestProto() {
    @Test
    fun `test getting transactions list`() = testLoggedIn { token ->
        client.get("/v1/transactions") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                val transactions = data.getJSONArray("transactions")
                assertTrue(transactions.isEmpty)
            }
        }
    }
}
