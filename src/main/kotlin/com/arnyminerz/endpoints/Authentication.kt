package com.arnyminerz.endpoints

import com.arnyminerz.errors.Errors
import com.arnyminerz.utils.respondFailure
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.util.pipeline.PipelineContext
import java.time.Instant

suspend fun PipelineContext<*, ApplicationCall>.requireAuthentication(
    block: suspend PipelineContext<*, ApplicationCall>.(nif: String) -> Unit
) {
    val principal = call.principal<JWTPrincipal>() ?: return call.respondFailure(Errors.Unauthorized)
    val nif = principal.payload.getClaim("nif").asString()
    val expiresAt = principal.expiresAt?.toInstant() ?: return call.respondFailure(Errors.Unauthorized)
    val now = Instant.now()
    if (expiresAt < now)
        call.respondFailure(Errors.Unauthorized)
    else
        block(nif)
}
