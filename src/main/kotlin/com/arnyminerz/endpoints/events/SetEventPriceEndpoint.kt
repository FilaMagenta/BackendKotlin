package com.arnyminerz.endpoints.events

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.filamagenta.commons.data.Category
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.util.getValue
import io.ktor.util.pipeline.PipelineContext

object SetEventPriceEndpoint : AuthenticatedEndpoint() {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val eventId: Long by call.parameters

        val price: Double by called { Arguments.Price }
        val categoryName: String by called { Arguments.Category }

        val category = Category.entries.find { it.name == categoryName }
            ?: return call.respondFailure(Errors.CategoryInvalid)
        val event = eventsInterface.get(eventId) { it }
            ?: return call.respondFailure(Errors.EventNotFound)

        // TODO - IllegalStateException should be caught to be thrown with a custom error
        try {
            eventsInterface.setEventPrice(event, category, price)
            call.respondSuccess(HttpStatusCode.Accepted)
        } catch (_: NullPointerException) {
            call.respondFailure(Errors.EventNotFound)
        }
    }
}
