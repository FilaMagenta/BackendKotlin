package com.arnyminerz.application.events

import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.utils.assertSuccess
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import org.junit.Test

class ApplicationTestEventPrices : ApplicationTestEventProto() {
    @Test
    fun `test setting event price`() = testLoggedInAdmin { token ->
        provideSampleEvent(token)

        getAllEvents(token) { events ->
            assertEquals(1, events.length())

            val event = events.getJSONObject(0)
            val eventId = event.getLong("id")

            client.post("/v1/events/$eventId/price") {
                header("Authorization", "Bearer $token")
                setBody(
                    jsonOf(
                        "price" to 10.0,
                        "category" to Category.FESTER
                    ).toString()
                )
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }

            getAllEvents(token) { list ->
                val eventJson = list.getJSONObject(0)
                val prices = eventJson.getJSONObject("prices")
                    .toMap()
                    .map { (cat, price) -> Category.valueOf(cat) to price.toString().toDouble() }
                    .toMap()

                assertEquals(1, prices.size)

                val festerPrice = prices[Category.FESTER]
                assertNotNull(festerPrice)
                assertEquals(10.0, festerPrice)
            }
        }
    }
}
