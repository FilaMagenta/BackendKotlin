package com.arnyminerz.application.transactions

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import java.time.ZoneOffset
import java.time.ZonedDateTime
import junit.framework.TestCase.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.json.JSONObject
import org.junit.Test

class ApplicationTestTransactionsNew: ApplicationTestProto() {
    @Test
    fun `test creating transaction - admin`() = testLoggedInAdmin {  token ->
        client.post("/v1/transactions") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(
                JSONObject().apply {
                    put("amount", 1)
                    put("price", 10)
                    put("description", "Testing transaction")
                    put("date", ZonedDateTime.of(2023, 3, 12, 12, 0, 0, 0, ZoneOffset.UTC).toString())
                }.toString()
            )
        }.apply {
            assertSuccess(HttpStatusCode.Created)
        }

        client.get("/v1/transactions") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.apply {
            assertSuccess { data ->
                assertNotNull(data)
                val transactions = data.getJSONArray("transactions")
                assertEquals(1, transactions.count())
            }
        }
    }

    @Test
    fun `test creating transaction - no permission`() = testLoggedIn {  token ->
        client.post("/v1/transactions") {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(
                JSONObject().apply {
                    put("amount", 1)
                    put("price", 10)
                    put("description", "Testing transaction")
                    put("date", ZonedDateTime.of(2023, 3, 12, 12, 0, 0, 0, ZoneOffset.UTC).toString())
                }.toString()
            )
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}