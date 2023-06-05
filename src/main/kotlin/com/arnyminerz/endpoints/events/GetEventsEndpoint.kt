package com.arnyminerz.endpoints.events

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONArray
import org.json.JSONObject

object GetEventsEndpoint : AuthenticatedEndpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(nif: String) {
        val eventsList = JSONArray()
        ServerDatabase.instance.eventsInterface.getAll { events ->
            for (event in events)
                eventsList.put(event.toJSON())
        }
        call.respondSuccess(
            JSONObject().apply {
                put("events", eventsList)
            }
        )
    }
}
