package com.arnyminerz.application.transactions

import com.arnyminerz.application.ApplicationTestProto
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.filamagenta.commons.utils.getIntOrNull
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
import org.json.JSONObject
import org.junit.Test

class ApplicationTestTransactionsNew : ApplicationTestProto() {
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
                val transaction = transactions.getJSONObject(0)
                assertNotNull(transaction.getIntOrNull("id"))
            }
        }
    }

    @Test
    fun `test creating transaction - no permission`() = testLoggedIn { token ->
        client.post("/v1/transactions") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.apply {
            assertFailure(Errors.MissingPermission)
        }
    }
}
