package com.arnyminerz.utils

import io.ktor.server.application.Application

val String.asEnvironmentVariable: String
    get() = replace(".", "_")
        .map { if (it.isUpperCase()) "_${it.uppercase()}" else it.uppercase() }
        .joinToString("")

fun Application.getEnvironmentPropertyOrVariable(key: String): String =
    environment.config.propertyOrNull(key)?.getString()
        ?: System.getProperty(key)
        ?: System.getenv(key.asEnvironmentVariable)
        ?: throw IllegalArgumentException(
            "Could not find a property called \"%s\" in application.yaml, nor an environment variable called \"%s\""
                .format(key, key.asEnvironmentVariable)
        )
