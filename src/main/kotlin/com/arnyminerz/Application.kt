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

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::installModules)
        .start(wait = true)
}

fun Application.installModules() {
    val isProduction = getEnvironmentPropertyOrVariable("config.production", "false").toBoolean()

    val databaseInstance = if (isProduction) {
        ProductionDatabase.Instance
    } else {
        DevelopmentDatabase.Instance
    }
    databaseInstance.set()

    install(DoubleReceive)
    configureStatusPages()
    configureJwt()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
