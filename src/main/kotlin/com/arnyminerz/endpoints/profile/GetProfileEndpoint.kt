package com.arnyminerz.endpoints.profile

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.endpoints.protos.Endpoint
import com.arnyminerz.endpoints.requireAuthentication
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object GetProfileEndpoint: AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.call(nif: String) {
        val user = ServerDatabase.instance.usersInterface.findWithNif(nif) { it }
            ?: return call.respondFailure(Errors.NifNotFound)
        call.respondSuccess(user)
    }
}
