package com.arnyminerz.endpoints.events

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.calledOptional
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
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

        // If the user is assisting to the event, generate the QR code
        val data = "testing data"
        val qrCodeRenderer = QRCode(data).render(cellSize = size ?: QR_CELL_SIZE_DEFAULT)
        val qrBytes = ByteArrayOutputStream()
            .also { qrCodeRenderer.writeImage(it) }
            .toByteArray()

        call.response.header(HEADER_QR_SIZE, qrBytes.size)
        call.respondBytes(qrBytes, ContentType.Image.PNG)
    }
}
