package com.arnyminerz.endpoints.inventory

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.*
import io.ktor.util.pipeline.*
import org.json.JSONArray
import org.json.JSONObject

object GetInventoryItemsEndpoint : AuthenticatedEndpoint() {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val result = JSONArray()
        inventoryInterface.getAll { items ->
            items.forEach { item ->
                result.put(item.toJSON())
            }
        }
        call.respondSuccess(
            JSONObject().apply {
                put("items", result)
            }
        )
    }
}
