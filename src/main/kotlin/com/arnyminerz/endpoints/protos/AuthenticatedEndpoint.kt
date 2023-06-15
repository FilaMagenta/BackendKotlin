package com.arnyminerz.endpoints.protos

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.connector.EventsInterface
import com.arnyminerz.database.connector.InventoryInterface
import com.arnyminerz.database.connector.TransactionsInterface
import com.arnyminerz.database.connector.UsersInterface
import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.MissingArgumentException
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.permissions.Permission
import com.arnyminerz.utils.respondFailure
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

/**
 * Defines an endpoint that requires that the user has logged in.
 */
abstract class AuthenticatedEndpoint(
    /**
     * A list of the permissions that the user requires to use the endpoint.
     */
    private vararg val requiredPermissions: Permission
) {
    /**
     * Runs all the logic associated with the endpoint.
     * @throws MissingArgumentException When an argument of the body was not present but was required.
     */
    abstract suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User)

    val usersInterface: UsersInterface
        get() = ServerDatabase.instance.usersInterface

    val eventsInterface: EventsInterface
        get() = ServerDatabase.instance.eventsInterface

    val transactionsInterface: TransactionsInterface
        get() = ServerDatabase.instance.transactionsInterface

    val inventoryInterface: InventoryInterface
        get() = ServerDatabase.instance.inventoryInterface

    suspend fun run(context: PipelineContext<*, ApplicationCall>, nif: String) {
        try {
            val user = usersInterface.findWithNif(nif) { it }
                ?: return context.call.respondFailure(Errors.NifNotFound)

            println(
                "Endpoint required permissions (%d): %s"
                    .format(
                        requiredPermissions.size,
                        requiredPermissions.joinToString { it.toString() }
                    )
            )
            val allPermissionsGranted = requiredPermissions
                // Only iterate if there are permissions required
                .takeIf { it.isNotEmpty() }
                // Check that all permissions are granted
                ?.all(user.userRole::hasPermission)
            if (allPermissionsGranted != true) {
                return context.call.respondFailure(Errors.MissingPermission)
            }

            context.endpoint(user)
        } catch (ignored: MissingArgumentException) {
        }
    }
}
