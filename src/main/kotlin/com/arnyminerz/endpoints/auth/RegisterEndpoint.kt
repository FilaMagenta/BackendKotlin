package com.arnyminerz.endpoints.auth

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.endpoints.arguments.Arguments
import com.arnyminerz.endpoints.arguments.called
import com.arnyminerz.endpoints.protos.Endpoint
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import com.arnyminerz.filamagenta.commons.data.types.UserType
import com.arnyminerz.filamagenta.commons.errors.Errors.EmailInvalid
import com.arnyminerz.filamagenta.commons.errors.Errors.NifAlreadyRegistered
import com.arnyminerz.filamagenta.commons.errors.Errors.NifInvalid
import com.arnyminerz.filamagenta.commons.utils.validation.isValidDni
import com.arnyminerz.filamagenta.commons.utils.validation.isValidEmail
import com.arnyminerz.filamagenta.commons.utils.validation.isValidNie
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

object RegisterEndpoint : Endpoint {
    override suspend fun PipelineContext<*, ApplicationCall>.endpoint() {
        val nif by called { Arguments.Nif }
        val name by called { Arguments.Name }
        val surname by called { Arguments.Surname }
        val email by called { Arguments.Email }
        val password by called { Arguments.Password }

        if (!nif.isValidDni && !nif.isValidNie) return call.respondFailure(NifInvalid)
        if (!email.isValidEmail) return call.respondFailure(EmailInvalid)

        // TODO: Check password security

        // Check if user already exists
        val userExists: Boolean = ServerDatabase.instance.usersInterface.getAll { users ->
            if (users.empty()) {
                false
            } else {
                users.find { it.nif == nif } != null
            }
        }
        if (userExists) {
            return call.respondFailure(NifAlreadyRegistered)
        }

        ServerDatabase.instance.usersInterface.new(
            UserType(nif, Role.DEFAULT, name, surname, email, null),
            "password" to password
        )

        call.respondSuccess(HttpStatusCode.Created)
    }
}
