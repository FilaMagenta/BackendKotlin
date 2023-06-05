package com.arnyminerz.endpoints

import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.endpoints.protos.Endpoint
import io.ktor.server.application.ApplicationCall
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.util.pipeline.PipelineContext

fun Route.get(path: String, endpoint: Endpoint) {
    val function: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        endpoint.run(this)
    }
    get(path, function)
}

fun Route.get(path: String, endpoint: AuthenticatedEndpoint) {
    val function: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        requireAuthentication {
            endpoint.run(this, it)
        }
    }
    get(path, function)
}


fun Route.post(path: String, endpoint: Endpoint) {
    val function: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        endpoint.run(this)
    }
    post(path, function)
}

fun Route.post(path: String, endpoint: AuthenticatedEndpoint) {
    val function: suspend PipelineContext<Unit, ApplicationCall>.(Unit) -> Unit = {
        requireAuthentication {
            endpoint.run(this, it)
        }
    }
    post(path, function)
}
