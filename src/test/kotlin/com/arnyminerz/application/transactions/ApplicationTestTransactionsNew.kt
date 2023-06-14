package com.arnyminerz.application.transactions

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.*
import io.ktor.http.*
import junit.framework.TestCase.assertEquals
import org.json.JSONObject
import org.junit.Test
import java.time.ZoneOffset
import java.time.ZonedDateTime
import kotlin.test.assertNotNull

class ApplicationTestTransactionsNew: ApplicationTestProto() {
    @Test
    fun `test creating transaction - admin`() = testDoubleLoggedInAdmin { token, tokenAdmin ->
        val user = usersInterface.findWithNif(registerSampleData.getValue("nif")) { it }!!

        client.post("/v1/transactions") {
            header(HttpHeaders.Authorization, "Bearer $tokenAdmin")
            setBody(
                JSONObject().apply {
                    put("amount", 1)
                    put("price", 10)
                    put("description", "Testing transaction")
                    put("date", ZonedDateTime.of(2023, 3, 12, 12, 0, 0, 0, ZoneOffset.UTC).toString())
                    put("user_id", user.id)
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
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}