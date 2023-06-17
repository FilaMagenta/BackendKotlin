package com.arnyminerz.plugins

import com.arnyminerz.endpoints.BaseEndpoint
import com.arnyminerz.endpoints.auth.LoginEndpoint
import com.arnyminerz.endpoints.auth.RegisterEndpoint
import com.arnyminerz.endpoints.events.CancelAssistanceEndpoint
import com.arnyminerz.endpoints.events.ConfirmAssistanceEndpoint
import com.arnyminerz.endpoints.events.GetEventQREndpoint
import com.arnyminerz.endpoints.events.GetEventsEndpoint
import com.arnyminerz.endpoints.events.JoinTableEndpoint
import com.arnyminerz.endpoints.events.LeaveTableEndpoint
import com.arnyminerz.endpoints.events.NewEventEndpoint
import com.arnyminerz.endpoints.events.NewTableEndpoint
import com.arnyminerz.endpoints.inventory.GetInventoryItemsEndpoint
import com.arnyminerz.endpoints.inventory.NewInventoryItemEndpoint
import com.arnyminerz.endpoints.profile.GetAllProfilesEndpoint
import com.arnyminerz.endpoints.profile.GetProfileEndpoint
import com.arnyminerz.endpoints.profile.UpdateCategoryDataEndpoint
import com.arnyminerz.endpoints.profile.UpdateUserCategoryEndpoint
import com.arnyminerz.endpoints.routing.delete
import com.arnyminerz.endpoints.routing.get
import com.arnyminerz.endpoints.routing.post
import com.arnyminerz.endpoints.routing.put
import com.arnyminerz.endpoints.transactions.GetTransactionsEndpoint
import com.arnyminerz.endpoints.transactions.NewTransactionEndpoint
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/v1", BaseEndpoint)
        post("/v1", BaseEndpoint)
        get("/v1/", BaseEndpoint)
        post("/v1/", BaseEndpoint)

        post("/v1/auth/register", RegisterEndpoint)
        post("/v1/auth/login", LoginEndpoint)

        authenticate("auth-jwt") {
            get("/v1/profile", GetProfileEndpoint)
            get("/v1/profile/all", GetAllProfilesEndpoint)
            post("/v1/profile/{userId}/category", UpdateUserCategoryEndpoint)
            post("/v1/profile/category/{categoryName}", UpdateCategoryDataEndpoint)

            get("/v1/events", GetEventsEndpoint)
            post("/v1/events", NewEventEndpoint)
            get("/v1/events/{eventId}/qrcode", GetEventQREndpoint)
            put("/v1/events/{eventId}/assistance", ConfirmAssistanceEndpoint)
            delete("/v1/events/{eventId}/assistance", CancelAssistanceEndpoint)
            post("/v1/events/{eventId}/table", NewTableEndpoint)
            put("/v1/events/{eventId}/table/{tableId}", JoinTableEndpoint)
            delete("/v1/events/{eventId}/table", LeaveTableEndpoint)

            get("/v1/inventory", GetInventoryItemsEndpoint)
            post("/v1/inventory", NewInventoryItemEndpoint)

            get("/v1/transactions", GetTransactionsEndpoint)
            post("/v1/transactions", NewTransactionEndpoint)
        }
    }
}
