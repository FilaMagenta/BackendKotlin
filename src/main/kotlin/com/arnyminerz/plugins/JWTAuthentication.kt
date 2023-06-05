package com.arnyminerz.plugins

import com.arnyminerz.errors.Errors
import com.arnyminerz.security.Authentication
import com.arnyminerz.utils.getEnvironmentPropertyOrVariable
import com.arnyminerz.utils.respondFailure
import com.auth0.jwk.Jwk
import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwk.UrlJwkProvider
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import java.net.URL
import java.util.concurrent.TimeUnit

fun Application.configureJwt() {
    val privateKeyString = getEnvironmentPropertyOrVariable("jwt.privateKey")
    val issuer = getEnvironmentPropertyOrVariable("jwt.issuer")
    val audience = getEnvironmentPropertyOrVariable("jwt.audience")
    val myRealm = getEnvironmentPropertyOrVariable("jwt.realm")

    val jwkProvider = JwkProviderBuilder(URL(issuer))
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.HOURS)
        .build()

    Authentication.privateKeyString = privateKeyString
    Authentication.issuer = issuer
    Authentication.jwkProvider = jwkProvider
    Authentication.audience = audience

    // Please read the jwt property from the config file if you are using EngineMain
    authentication {
        jwt {
            realm = myRealm
            // verifier(jwkProvider, issuer) { acceptLeeway(3) }
            validate { credential ->
                if (credential.payload.getClaim("nif").asString().isNotBlank())
                    JWTPrincipal(credential.payload)
                else
                    null
            }
            challenge { _, _ ->
                call.respondFailure(Errors.Unauthorized)
            }
        }
    }
    routing {
        staticResources("/certs", "certs")
    }
}
