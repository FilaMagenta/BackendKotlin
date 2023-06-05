package com.arnyminerz.endpoints.auth

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.endpoints.protos.Endpoint
import com.arnyminerz.errors.Errors
import com.arnyminerz.security.Authentication
import com.arnyminerz.security.Passwords
import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.jsonOf
import com.arnyminerz.utils.receiveJson
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext
import java.time.ZonedDateTime

object LoginEndpoint: Endpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.call() {
        val body = call.receiveJson()
        val nif = body.getStringOrNull("nif", true) ?: return call.respondFailure(Errors.MissingNifBody)
        val password = body.getStringOrNull("password", true) ?: return call.respondFailure(Errors.MissingPasswordBody)

        ServerDatabase.instance.usersInterface.findWithNif(nif) { user ->
            if (user == null) return@findWithNif call.respondFailure(Errors.NifNotFound)

            if (!Passwords.isExpectedPassword(password, user.passwordSalt(), user.passwordHash()))
                return@findWithNif call.respondFailure(Errors.WrongPassword)
        }

        val token = Authentication.newToken(nif, ZonedDateTime.now().plusDays(15))
        call.respondSuccess(
            jsonOf("token" to token)
        )
    }
}
