package com.arnyminerz.plugins

import com.arnyminerz.endpoints.auth.loginEndpoint
import com.arnyminerz.endpoints.auth.registerEndpoint
import com.arnyminerz.endpoints.profile.getProfileEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import com.arnyminerz.utils.serialization.JsonSerializable
import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        get("/v1") {
            call.respondSuccess(null)
        }
        post("/v1/auth/register") { registerEndpoint() }
        post("/v1/auth/login") { loginEndpoint() }
        get("/v1/profile") { getProfileEndpoint() }
    }
}
