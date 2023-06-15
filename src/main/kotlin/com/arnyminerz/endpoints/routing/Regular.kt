package com.arnyminerz.endpoints.routing

import com.arnyminerz.endpoints.protos.Endpoint
import io.ktor.server.application.ApplicationCall
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.util.pipeline.PipelineContext

private fun Route.queryEndpoint(
    endpoint: Endpoint,
    block: Route.(suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit) -> Unit
) {
    val function: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        endpoint.run(this)
    }
    block(function)
}

fun Route.get(path: String, endpoint: Endpoint) = queryEndpoint(endpoint) {
    get(path, it)
}

fun Route.post(path: String, endpoint: Endpoint) = queryEndpoint(endpoint) {
    post(path, it)
}
