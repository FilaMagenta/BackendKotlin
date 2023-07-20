package com.arnyminerz.endpoints.events

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.arguments.calledOptional
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.filamagenta.commons.data.security.RSAKeyPairGenerator
import com.arnyminerz.filamagenta.commons.data.security.permissions.Permissions
import com.arnyminerz.filamagenta.commons.data.types.EventType
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import java.time.Instant

object NewEventEndpoint : AuthenticatedEndpoint(Permissions.Events.Create) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val name by called { Arguments.Name }
        val description by called { Arguments.Description }
        val date by called { Arguments.Date }
        val until by calledOptional { Arguments.Until }
        val reservations by calledOptional { Arguments.Reservations }
        val maxGuests by calledOptional { Arguments.MaxGuests }

        // TODO: Check "until" is before "date"
        // TODO: Check "reservations" is before "date"

        val keyPair = RSAKeyPairGenerator.newKey()
        val event = EventType(
            0,
            Instant.now(),
            name,
            description,
            date,
            until,
            reservations,
            maxGuests,
            keyPair.public,
            keyPair.private
        )

        ServerDatabase.instance.eventsInterface.new(event)

        call.respondSuccess(status = HttpStatusCode.Created)
    }
}
