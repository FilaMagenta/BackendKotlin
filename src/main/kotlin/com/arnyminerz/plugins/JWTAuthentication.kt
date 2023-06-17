package com.arnyminerz.plugins

import com.arnyminerz.errors.Errors
import com.arnyminerz.security.Authentication
import com.arnyminerz.utils.getEnvironmentPropertyOrVariable
import com.arnyminerz.utils.respondFailure
import com.auth0.jwt.JWT
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import java.io.File

fun Application.configureJwt() {
    val secret = getEnvironmentPropertyOrVariable("jwt.secret")
    val issuer = getEnvironmentPropertyOrVariable("jwt.issuer")
    val audience = getEnvironmentPropertyOrVariable("jwt.audience")
    val myRealm = getEnvironmentPropertyOrVariable("jwt.realm")

    val useCustomCerts = getEnvironmentPropertyOrVariable("certs.custom_cert").toBoolean()

    Authentication.secret = secret
    Authentication.issuer = issuer
    Authentication.audience = audience

    // Please read the jwt property from the config file if you are using EngineMain
    authentication {
        jwt("auth-jwt") {
            realm = myRealm
            verifier(
                JWT.require(Authentication.getAlgorithm())
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                println("Validating request. Payload: ${credential.payload}")
                if (credential.payload.getClaim("nif").asString().isNotBlank()) {
                    JWTPrincipal(credential.payload)
                } else {
                    System.err.println("Got invalid credential: ${credential.payload}")
                    null
                }
            }
            challenge { _, _ ->
                println("Authorization failed, responding Unauthorized")
                call.respondFailure(Errors.Unauthorized)
            }
        }
    }
    routing {
        if (useCustomCerts) {
            staticFiles("/certs", File("/certs"))
        } else {
            staticResources("/certs", "certs")
        }
    }
}
