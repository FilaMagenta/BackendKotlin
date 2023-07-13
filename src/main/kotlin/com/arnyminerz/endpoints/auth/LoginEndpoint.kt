package com.arnyminerz.endpoints.auth

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.Endpoint
import com.arnyminerz.filamagenta.commons.data.security.Passwords
import com.arnyminerz.filamagenta.commons.errors.Errors
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import com.arnyminerz.security.Authentication
import com.arnyminerz.security.Authentication.TOKEN_EXPIRATION_DAYS
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import java.time.ZonedDateTime

object LoginEndpoint : Endpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint() {
        val nif by called { Arguments.Nif }
        val password by called { Arguments.Password }

        ServerDatabase.instance.usersInterface.findWithNif(nif) { user ->
            if (user == null) return@findWithNif call.respondFailure(Errors.NifNotFound)

            if (!Passwords.isExpectedPassword(password, user.passwordSalt(), user.passwordHash())) {
                return@findWithNif call.respondFailure(Errors.WrongPassword)
            }
        }

        val token = Authentication.newToken(nif, ZonedDateTime.now().plusDays(TOKEN_EXPIRATION_DAYS))
        call.respondSuccess(
            jsonOf("token" to token)
        )
    }
}
