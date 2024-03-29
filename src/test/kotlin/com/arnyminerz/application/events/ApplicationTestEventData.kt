package com.arnyminerz.application.events

import com.arnyminerz.endpoints.events.GetEventQREndpoint.HEADER_QR_SIZE
import com.arnyminerz.filamagenta.commons.data.security.Encryption
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.utils.assertEqualsJson
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

        val user = usersInterface.findWithNif(registerSampleData.getValue("nif")) { it!! }

        getAllEvents(token) { events ->
            assertEquals(1, events.length())

            val eventId = events.getJSONObject(0).getLong("id")

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
                val privateKey = event.decodePrivateKey()
                println("Text result: $text")
                val jsonStr = text
                    // Text is split in chunks of 256 bytes, separated with line breaks
                    .split("\n")
                    // Decode each chunk in Base64
                    .map { Base64.getDecoder().decode(it) }
                    // Decrypt each chunk, and join without any characters in between
                    .joinToString("") {
                        Encryption.decrypt(privateKey, it).toString(Charsets.UTF_8)
                    }
                val json = JSONObject(jsonStr)

                val decodedImage = File.createTempFile("fmb", ".png")
                decodedImage.outputStream().use {
                    it.write(qrBytes)
                }
                println("Received QR code: ${decodedImage.absolutePath}")

                val eventJson = json.getJSONObject("event")
                val userJson = json.getJSONObject("user")

                assertEqualsJson(event.toJSON(), eventJson)
                assertEqualsJson(userJson, user.toJSON(), true)
            }
        }
    }
}
