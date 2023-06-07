package com.arnyminerz.endpoints.protos

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.`interface`.EventsInterface
import com.arnyminerz.database.`interface`.InventoryInterface
import com.arnyminerz.database.`interface`.TransactionsInterface
import com.arnyminerz.database.`interface`.UsersInterface
import com.arnyminerz.endpoints.arguments.MissingArgumentException
import io.ktor.server.application.ApplicationCall
import io.ktor.util.pipeline.PipelineContext

fun interface AuthenticatedEndpoint {
    /**
     * Runs all the logic associated with the endpoint.
     * @throws MissingArgumentException When an argument of the body was not present but was required.
     */
    suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String)

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
            context.endpoint(nif)
        } catch (ignored: MissingArgumentException) {
        }
    }
}
