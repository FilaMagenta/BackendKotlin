package com.arnyminerz

import com.arnyminerz.database.DevelopmentDatabase
import com.arnyminerz.plugins.configureHTTP
import com.arnyminerz.plugins.configureJwt
import com.arnyminerz.plugins.configureRouting
import com.arnyminerz.plugins.configureSerialization
import com.arnyminerz.plugins.configureStatusPages
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.doublereceive.DoubleReceive

fun main() {
    DevelopmentDatabase.Instance.set()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::installModules)
        .start(wait = true)
}

fun Application.installModules() {
    install(DoubleReceive)
    configureStatusPages()
    configureJwt()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
