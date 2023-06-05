package com.arnyminerz.endpoints.protos

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

fun interface AuthenticatedEndpoint {
    suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String)

    suspend fun run(context: PipelineContext<*, ApplicationCall>, nif: String) {
        context.endpoint(nif)
    }
}
