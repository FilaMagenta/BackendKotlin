package com.arnyminerz.endpoints.inventory

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

object NewInventoryItemEndpoint: AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String) {
        val name by called { Arguments.Name }
        val price by called { Arguments.Price }

        val user = usersInterface.findWithNif(nif) { it }
            ?: return call.respondFailure(Errors.NifNotFound)

        println("Trying to create new inventory item as ${user.name}. Role: ${user.userRole} (${user.userRole.permissions.joinToString(", ")})")
        if (!user.userRole.hasPermission(Permissions.Inventory.CreateNewItem))
            return call.respondFailure(Errors.MissingPermission)

        inventoryInterface.new(
            InventoryItemType(name, price.toFloat())
        )

        call.respondSuccess(HttpStatusCode.Created)
    }
}
