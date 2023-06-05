package com.arnyminerz.endpoints.protos

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.`interface`.EventsInterface
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

    suspend fun run(context: PipelineContext<*, ApplicationCall>, nif: String) {
        try {
            context.endpoint(nif)
        } catch (ignored: MissingArgumentException) {
        }
    }
}
