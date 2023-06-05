package com.arnyminerz.plugins

import com.arnyminerz.endpoints.BaseEndpoint
import com.arnyminerz.endpoints.auth.LoginEndpoint
import com.arnyminerz.endpoints.auth.RegisterEndpoint
import com.arnyminerz.endpoints.events.CancelAssistanceEndpoint
import com.arnyminerz.endpoints.events.ConfirmAssistanceEndpoint
import com.arnyminerz.endpoints.events.GetEventsEndpoint
import com.arnyminerz.endpoints.events.NewEventEndpoint
import com.arnyminerz.endpoints.profile.GetProfileEndpoint
import com.arnyminerz.endpoints.routing.delete
import com.arnyminerz.endpoints.routing.get
import com.arnyminerz.endpoints.routing.post
import com.arnyminerz.endpoints.routing.put
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/v1", BaseEndpoint)
        post("/v1", BaseEndpoint)

        post("/v1/auth/register", RegisterEndpoint)
        post("/v1/auth/login", LoginEndpoint)

        authenticate("auth-jwt") {
            get("/v1/profile", GetProfileEndpoint)
            get("/v1/events", GetEventsEndpoint)
            post("/v1/events", NewEventEndpoint)
            put("/v1/events/{eventId}/assistance", ConfirmAssistanceEndpoint)
            delete("/v1/events/{eventId}/assistance", CancelAssistanceEndpoint)
        }
    }
}
