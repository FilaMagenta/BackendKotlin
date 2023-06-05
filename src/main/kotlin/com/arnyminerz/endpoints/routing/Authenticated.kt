package com.arnyminerz.endpoints.routing

import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.endpoints.requireAuthentication
import io.ktor.server.application.ApplicationCall
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.util.pipeline.PipelineContext

private fun Route.queryAuthenticatedEndpoint(endpoint: AuthenticatedEndpoint, block: Route.(suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit) -> Unit) {
    val function: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        requireAuthentication { nif ->
            endpoint.run(this, nif)
        }
    }
    block(function)
}

fun Route.get(path: String, endpoint: AuthenticatedEndpoint) = queryAuthenticatedEndpoint(endpoint) {
    get(path, it)
}

fun Route.post(path: String, endpoint: AuthenticatedEndpoint) = queryAuthenticatedEndpoint(endpoint) {
    post(path, it)
}

fun Route.put(path: String, endpoint: AuthenticatedEndpoint) = queryAuthenticatedEndpoint(endpoint) {
    put(path, it)
}

fun Route.delete(path: String, endpoint: AuthenticatedEndpoint) = queryAuthenticatedEndpoint(endpoint) {
    delete(path, it)
}
