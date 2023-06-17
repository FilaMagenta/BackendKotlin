package com.arnyminerz.endpoints.profile

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.security.permissions.Permissions
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import org.json.JSONArray
import org.json.JSONObject

object GetAllProfilesEndpoint : AuthenticatedEndpoint(Permissions.Users.List) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val users = usersInterface.getAll { users ->
            val json = JSONArray()
            for (u in users) {
                val userJson = u.toJSON()
                val category = usersInterface.getCategory(u)
                userJson.put("category", category.name)
                val categoryInformation = usersInterface.getCategoryInformation(category)
                userJson.put("category_information", categoryInformation?.toJSON())
                json.put(userJson)
            }
            json
        }

        call.respondSuccess(
            JSONObject().apply {
                put("users", users)
            }
        )
    }
}
