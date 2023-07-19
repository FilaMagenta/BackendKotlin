package com.arnyminerz.endpoints.profile

import com.arnyminerz.database.entity.User
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.AuthenticatedEndpoint
import com.arnyminerz.filamagenta.commons.data.security.permissions.Permissions
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.util.getValue
import io.ktor.util.pipeline.PipelineContext

object SetRoleEndpoint : AuthenticatedEndpoint(Permissions.Users.ChangeRole) {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint(user: User) {
        val userId: Long by call.parameters

        val role by called { Arguments.Role }

        val targetUser = usersInterface.get(userId) { it }
            ?: return call.respondFailure(Errors.UserNotFound)

        usersInterface.setRole(targetUser, role)

        call.respondSuccess()
    }
}
