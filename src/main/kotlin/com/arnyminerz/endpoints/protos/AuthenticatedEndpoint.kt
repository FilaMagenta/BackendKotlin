package com.arnyminerz.endpoints.protos

import com.arnyminerz.endpoints.arguments.MissingArgumentException
import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

fun interface AuthenticatedEndpoint {
    /**
     * Runs all the logic associated with the endpoint.
     * @throws MissingArgumentException When an argument of the body was not present but was required.
     */
    suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String)

    suspend fun run(context: PipelineContext<*, ApplicationCall>, nif: String) {
        try {
            context.endpoint(nif)
        } catch (ignored: MissingArgumentException) {
        }
    }
}
