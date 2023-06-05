package com.arnyminerz.endpoints

import com.arnyminerz.endpoints.protos.Endpoint
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object BaseEndpoint: Endpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.call() {
        call.respondSuccess()
    }
}
