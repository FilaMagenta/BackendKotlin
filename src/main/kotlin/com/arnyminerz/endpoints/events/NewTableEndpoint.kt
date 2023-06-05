package com.arnyminerz.endpoints.events

import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.util.getValue
import io.ktor.util.pipeline.PipelineContext

object NewTableEndpoint: AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String) {
        val eventId: Int by call.parameters

        val user = usersInterface.findWithNif(nif) { it }
            ?: return call.respondFailure(Errors.NifNotFound)
        val event = eventsInterface.get(eventId) { it }
            ?: return call.respondFailure(Errors.EventNotFound)

        if (!eventsInterface.createTable(user, event))
            return call.respondFailure(Errors.UserAlreadyInTable)

        call.respondSuccess(HttpStatusCode.Created)
    }
}