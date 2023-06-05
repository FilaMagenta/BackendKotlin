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

object LeaveTableEndpoint: AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String) {
        val eventId: Int by call.parameters

        val user = usersInterface.findWithNif(nif) { it }
            ?: return call.respondFailure(Errors.NifNotFound)

        var leftTable = false

        eventsInterface.getResponsibleTable(user.id.value, eventId) { table ->
            if (table == null) return@getResponsibleTable

            if (!eventsInterface.deleteTable(user, table))
                return@getResponsibleTable call.respondFailure(Errors.UserNotInTable)

            leftTable = true
        }

        if (!leftTable)
            eventsInterface.getMemberTable(user.id.value) { table ->
                if (table == null) return@getMemberTable

                if (!eventsInterface.leaveTable(user, table))
                    return@getMemberTable call.respondFailure(Errors.UserNotInTable)

                leftTable = true
            }

        if (!leftTable)
            return call.respondFailure(Errors.UserNotInTable)

        call.respondSuccess(HttpStatusCode.Accepted)
    }
}