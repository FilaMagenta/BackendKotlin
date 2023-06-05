package com.arnyminerz.endpoints.protos

import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

fun interface Endpoint {
    suspend fun PipelineContext<*, ApplicationCall>.endpoint()

    suspend fun run(context: PipelineContext<*, ApplicationCall>) {
        context.endpoint()
    }
}
