package com.arnyminerz.endpoints.events

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.calledOptional
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.Encryption
import com.arnyminerz.utils.jsonOf
import com.arnyminerz.utils.respondFailure
import io.github.g0dkar.qrcode.QRCode
import io.ktor.http.ContentType
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.header
import io.ktor.server.response.respondBytes
import io.ktor.server.util.getValue
import io.ktor.util.pipeline.PipelineContext
import java.io.ByteArrayOutputStream
import java.util.Base64

object GetEventQREndpoint : AuthenticatedEndpoint() {
    const val HEADER_QR_SIZE = "QR-Size"

    private const val QR_CELL_SIZE_DEFAULT = 25

    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val size by calledOptional { Arguments.Size }

        val eventId: Int by call.parameters

        val event = eventsInterface.get(eventId) { it }
            ?: return call.respondFailure(Errors.EventNotFound)

        val userAssists = eventsInterface.userAssists(user, event)
        if (!userAssists) {
            return call.respondFailure(Errors.UserNotAssistingEvent)
        }

        val publicKey = event.decodePublicKey()
        val eventJson = event.toJSON()
        val dataJson = jsonOf(
            "event" to eventJson,
            // User information is simplified
            "user" to jsonOf(
                "id" to user.id,
                "name" to user.name,
                "surname" to user.surname,
                "email" to user.email
            )
        )
            .toString()
            .toByteArray(Charsets.UTF_8)

        val encryptedData = Encryption.encrypt(publicKey, dataJson)

        // If the user is assisting to the event, generate the QR code
        val data = Base64.getMimeEncoder().encodeToString(encryptedData)
        val qrCodeRenderer = QRCode(data).render(cellSize = size ?: QR_CELL_SIZE_DEFAULT)
        val qrBytes = ByteArrayOutputStream()
            .also { qrCodeRenderer.writeImage(it) }
            .toByteArray()

        call.response.header(HEADER_QR_SIZE, qrBytes.size)
        call.respondBytes(qrBytes, ContentType.Image.PNG)
    }
}
