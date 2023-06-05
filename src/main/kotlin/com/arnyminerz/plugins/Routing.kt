package com.arnyminerz.plugins

import com.arnyminerz.endpoints.BaseEndpoint
import com.arnyminerz.endpoints.auth.LoginEndpoint
import com.arnyminerz.endpoints.auth.RegisterEndpoint
import com.arnyminerz.endpoints.events.GetEventsEndpoint
import com.arnyminerz.endpoints.get
import com.arnyminerz.endpoints.post
import com.arnyminerz.endpoints.profile.GetProfileEndpoint
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate

fun Application.configureRouting() {
    routing {
        get("/v1", BaseEndpoint)
        post("/v1", BaseEndpoint)

        post("/v1/auth/register", RegisterEndpoint)
        post("/v1/auth/login", LoginEndpoint)

        authenticate("auth-jwt") {
            get("/v1/profile", GetProfileEndpoint)
            get("/v1/events", GetEventsEndpoint)
        }
    }
}
