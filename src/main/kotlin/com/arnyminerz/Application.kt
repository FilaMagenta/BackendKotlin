package com.arnyminerz

import com.arnyminerz.database.DevelopmentDatabase
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.arnyminerz.plugins.*

fun main() {
    DevelopmentDatabase.Instance.set()

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::installModules)
        .start(wait = true)
}

fun Application.installModules() {
    configureStatusPages()
    configureJwt()
    configureHTTP()
    configureSerialization()
    configureRouting()
}
