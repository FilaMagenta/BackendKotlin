package com.arnyminerz.endpoints.profile

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.endpoints.requireAuthentication
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<*, ApplicationCall>.getProfileEndpoint() = requireAuthentication { nif ->
    val user = ServerDatabase.instance.getUserWithNif(nif) { it }
        ?: return@requireAuthentication call.respondFailure(Errors.NifNotFound)
    call.respondSuccess(user)
}
