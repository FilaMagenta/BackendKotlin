package com.arnyminerz.endpoints.events

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.User
import com.arnyminerz.database.types.EventType
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.arguments.calledOptional
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.security.permissions.Permissions
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object NewEventEndpoint: AuthenticatedEndpoint(Permissions.Events.Create) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val name by called { Arguments.Name }
        val description by called { Arguments.Description }
        val date by called { Arguments.Date }
        val until by calledOptional { Arguments.Until }
        val reservations by calledOptional { Arguments.Reservations }

        val event = EventType(name, description, date, until, reservations)

        ServerDatabase.instance.eventsInterface.new(event)

        call.respondSuccess(status = HttpStatusCode.Created)
    }
}
