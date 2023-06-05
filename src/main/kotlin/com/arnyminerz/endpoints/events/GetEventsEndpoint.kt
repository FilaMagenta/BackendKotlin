package com.arnyminerz.endpoints.events

import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

object GetEventsEndpoint : AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.call(nif: String) {
        TODO("Not yet implemented")
    }
}
