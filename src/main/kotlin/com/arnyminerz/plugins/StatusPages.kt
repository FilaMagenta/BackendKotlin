package com.arnyminerz.plugins

import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondFailure(Errors.Internal, cause, null)
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respondFailure(Errors.EndpointNotFound)
        }
    }
}
