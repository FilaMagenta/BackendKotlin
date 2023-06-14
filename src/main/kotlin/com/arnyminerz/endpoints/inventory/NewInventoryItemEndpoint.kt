package com.arnyminerz.endpoints.inventory

import com.arnyminerz.database.entity.User
import com.arnyminerz.database.types.InventoryItemType
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.permissions.Permissions
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object NewInventoryItemEndpoint: AuthenticatedEndpoint(Permissions.Inventory.Create) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val name by called { Arguments.Name }
        val price by called { Arguments.Price }

        inventoryInterface.new(
            InventoryItemType(name, price)
        )

        call.respondSuccess(HttpStatusCode.Created)
    }
}
