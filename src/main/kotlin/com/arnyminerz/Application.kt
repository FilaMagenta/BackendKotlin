package com.arnyminerz

import com.arnyminerz.database.DevelopmentDatabase
import com.arnyminerz.database.ProductionDatabase
import com.arnyminerz.plugins.configureHTTP
import com.arnyminerz.plugins.configureJwt
import com.arnyminerz.plugins.configureRouting
import com.arnyminerz.plugins.configureSerialization
import com.arnyminerz.plugins.configureStatusPages
import com.arnyminerz.utils.getEnvironmentPropertyOrVariable
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.doublereceive.DoubleReceive
import io.sentry.Sentry

fun main() {
    val sentryDsn: String? = System.getenv("SENTRY_DSN")
    if (sentryDsn != null) {
        Sentry.init { options ->
            options.dsn = sentryDsn
            // Set tracesSampleRate to 1.0 to capture 100% of transactions for performance monitoring.
            // We recommend adjusting this value in production.
            options.tracesSampleRate = 1.0
            // When first trying Sentry it's good to see what the SDK is doing:
            options.isDebug = true
        }
    }

    embeddedServer(
        Netty,
        port = 8080,
        host = "0.0.0.0",
        module = {
            val isProduction = getEnvironmentPropertyOrVariable("config.production", "false").toBoolean()

            val databaseInstance = if (isProduction) {
                ProductionDatabase.Instance
            } else {
                DevelopmentDatabase.Instance
            }
            databaseInstance.set()

            installModules()
        }
    ).start(wait = true)
}

fun Application.installModules() {
    install(DoubleReceive)
    configureStatusPages()
    configureJwt()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
