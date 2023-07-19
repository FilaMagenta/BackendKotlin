package com.arnyminerz.endpoints.inventory

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.filamagenta.commons.data.security.permissions.Permissions
import com.arnyminerz.filamagenta.commons.data.types.InventoryItemType
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import java.time.ZonedDateTime

object NewInventoryItemEndpoint : AuthenticatedEndpoint(Permissions.Inventory.Create) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val name by called { Arguments.Name }
        val price by called { Arguments.Price }

        inventoryInterface.new(
            InventoryItemType(0, ZonedDateTime.now(), name, price)
        )

        call.respondSuccess(HttpStatusCode.Created)
    }
}
