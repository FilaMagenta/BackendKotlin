package com.arnyminerz.endpoints.events

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.filamagenta.commons.utils.toJSONArray
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONArray
import org.json.JSONObject

object GetEventsEndpoint : AuthenticatedEndpoint() {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val eventsList = JSONArray()
        ServerDatabase.instance.eventsInterface.getAll { events ->
            for (event in events) {
                val json = event.toJSON().apply {
                    // Remove private key, should not be shared
                    val keyPair = getJSONObject("key_pair")
                    put("key_pair", jsonOf("public" to keyPair.getJSONObject("public")))

                    val assistance = event.assistants.find { it.user.id == user.id }
                    put("assists", assistance != null)

                    val tables = event.tables
                    put(
                        "tables",
                        JSONArray().apply {
                            for (table in tables) {
                                put(
                                    jsonOf(
                                        "id" to table.id.value,
                                        "responsible_id" to table.responsible.id,
                                        "members" to table.members.map { it.user.id.value },
                                        "guests" to table.guests.toJSONArray()
                                    )
                                )
                            }
                        }
                    )

                    val prices = event.prices
                    put(
                        "prices",
                        prices.takeIf { it.isNotEmpty() }
                    )
                }
                eventsList.put(json)
            }
        }
        call.respondSuccess(
            JSONObject().apply {
                put("events", eventsList)
            }
        )
    }
}
