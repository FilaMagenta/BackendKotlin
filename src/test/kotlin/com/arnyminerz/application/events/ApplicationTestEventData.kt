package com.arnyminerz.application.events

import com.arnyminerz.endpoints.events.GetEventQREndpoint.HEADER_QR_SIZE
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.Encryption
import com.arnyminerz.utils.assertFailure
import com.arnyminerz.utils.assertSuccess
import com.google.zxing.BinaryBitmap
import com.google.zxing.MultiFormatReader
import com.google.zxing.client.j2se.BufferedImageLuminanceSource
import com.google.zxing.common.HybridBinarizer
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpStatusCode
import java.io.File
import java.util.Base64
import javax.imageio.ImageIO
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import org.json.JSONObject
import org.junit.Test

class ApplicationTestEventData : ApplicationTestEventProto() {

    @Test
    fun `test event data - qrcode`() = testLoggedInAdmin { token ->
        provideSampleEvent(token)

        getAllEvents(token) { events ->
            assertEquals(1, events.length())

            val eventJson = events.getJSONObject(0)
            val eventId = eventJson.getInt("id")

            // At first the user is not assisting, so an error is thrown
            client.get("/v1/events/$eventId/qrcode") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertFailure(Errors.UserNotAssistingEvent)
            }

            // Confirm assistance
            client.put("/v1/events/$eventId/assistance") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertSuccess(HttpStatusCode.Accepted)
            }

            val event = eventsInterface.get(eventId) { it }!!

            // Now the event can be fetched
            client.get("/v1/events/$eventId/qrcode") {
                header("Authorization", "Bearer $token")
            }.apply {
                assertEquals(HttpStatusCode.OK, call.response.status)

                val contentLength = call.response.headers[HEADER_QR_SIZE]?.toIntOrNull()
                assertNotNull(contentLength)
                assertTrue(contentLength > 0)

                // Decode the QR code
                val body = call.response.bodyAsChannel()
                val qrBytes = ByteArray(contentLength)
                body.readFully(qrBytes, 0, contentLength)

                // Now check the contents
                val binaryBitmap = BinaryBitmap(
                    HybridBinarizer(
                        BufferedImageLuminanceSource(
                            qrBytes.inputStream().use { ImageIO.read(it) }
                        )
                    )
                )
                val result = MultiFormatReader().decode(binaryBitmap)
                val text = result.text
                val bytes = Base64.getMimeDecoder().decode(text)
                val decodedEventJsonStr = Encryption.decrypt(event.decodePrivateKey(), bytes).toString(Charsets.UTF_8)
                val decodedEventJson = JSONObject(decodedEventJsonStr)

                val decodedImage = File.createTempFile("fmb", ".png")
                decodedImage.outputStream().use {
                    it.write(qrBytes)
                }
                println("Received QR code: ${decodedImage.absolutePath}")

                assertEquals(event.toJSON().toString(), decodedEventJson.toString())
            }
        }
    }
}
