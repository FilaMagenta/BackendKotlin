package com.arnyminerz.endpoints.server

import com.arnyminerz.Information
import com.arnyminerz.endpoints.protos.Endpoint
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object ServerInformationEndpoint : Endpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint() {
        call.respondSuccess(
            jsonOf(
                "version" to Information.version
            )
        )
    }
}
