package com.arnyminerz.endpoints.auth

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.types.UserType
import com.arnyminerz.errors.Errors.EmailInvalid
import com.arnyminerz.errors.Errors.MissingEmailBody
import com.arnyminerz.errors.Errors.MissingNameBody
import com.arnyminerz.errors.Errors.MissingNifBody
import com.arnyminerz.errors.Errors.MissingPasswordBody
import com.arnyminerz.errors.Errors.MissingSurnameBody
import com.arnyminerz.errors.Errors.NifAlreadyRegistered
import com.arnyminerz.errors.Errors.NifInvalid
import com.arnyminerz.utils.getStringOrNull
import com.arnyminerz.utils.validation.isValidDni
import com.arnyminerz.utils.validation.isValidNie
import com.arnyminerz.utils.receiveJson
import com.arnyminerz.utils.respondFailure
import com.arnyminerz.utils.respondSuccess
import com.arnyminerz.utils.validation.isValidEmail
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.util.pipeline.PipelineContext

suspend fun PipelineContext<*, ApplicationCall>.registerEndpoint() {
    val body = call.receiveJson()
    val nif = body.getStringOrNull("nif", true) ?: return call.respondFailure(MissingNifBody)
    val name = body.getStringOrNull("name", true) ?: return call.respondFailure(MissingNameBody)
    val surname = body.getStringOrNull("surname", true) ?: return call.respondFailure(MissingSurnameBody)
    val email = body.getStringOrNull("email", true) ?: return call.respondFailure(MissingEmailBody)
    val password = body.getStringOrNull("password", true) ?: return call.respondFailure(MissingPasswordBody)

    if (!nif.isValidDni && !nif.isValidNie) return call.respondFailure(NifInvalid)
    if (!email.isValidEmail) return call.respondFailure(EmailInvalid)

    // TODO: Check password security

    // Check if user already exists
    val userExists: Boolean = ServerDatabase.instance.usersInterface.getAll { users ->
        if (users.empty()) false
        else users.find { it.nif == nif } != null
    }
    if (userExists) return call.respondFailure(NifAlreadyRegistered)

    ServerDatabase.instance.usersInterface.new(UserType(nif, name, surname, email, null), "password" to password)

    call.respondSuccess()
}
