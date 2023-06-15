package com.arnyminerz.endpoints.profile

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object GetProfileEndpoint : AuthenticatedEndpoint() {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val json = user.toJSON()

        val category = usersInterface.getCategory(user)
        json.put("category", category.name)

        call.respondSuccess(json)
    }
}
