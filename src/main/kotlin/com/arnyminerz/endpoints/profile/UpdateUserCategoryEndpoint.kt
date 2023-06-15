package com.arnyminerz.endpoints.profile

import com.arnyminerz.data.Category
import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.permissions.Permissions
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.util.getValue
import io.ktor.util.pipeline.PipelineContext

object UpdateUserCategoryEndpoint : AuthenticatedEndpoint(Permissions.Users.UpdateCategory) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val userId: Int by call.parameters

        val categoryName by called { Arguments.Category }

        val targetUser = usersInterface.get(userId) { it }
            ?: return call.respondFailure(Errors.UserNotFound)

        val category = try {
            Category.valueOf(categoryName)
        } catch (_: IllegalArgumentException) {
            return call.respondFailure(Errors.CategoryInvalid)
        }

        usersInterface.setCategory(targetUser, category)

        call.respondSuccess()
    }
}
