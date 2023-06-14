package com.arnyminerz.endpoints.profile

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.*
import io.ktor.util.pipeline.*

object GetProfileEndpoint: AuthenticatedEndpoint() {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        call.respondSuccess(user)
    }
}
