package com.arnyminerz.utils

import io.ktor.server.application.Application

val String.asEnvironmentVariable: String
    get() = replace(".", "_")
        .map { if (it.isUpperCase()) "_${it.uppercase()}" else it.uppercase() }
        .joinToString("")

/**
 * Retrieves a configuration parameter provided by the `application.yaml` in resources, or if not present, takes from
 * environment variable/property.
 *
 * Environment is considered before `application.yaml`, and naming follows:
 * 1. For `application.yaml`, sub-properties are indicated with a `.`. For example: property `value` inside `config` is
 *    obtained with `config.value`.
 * 2. For environment values, the dot is substituted with a `_`, and all characters must be capital letters/numbers. For
 *    example, the same case as before, as environment variable would be `CONFIG_VALUE`.
 *
 *    Also, capital letters are separated using `_`. This is because names are all case, naming can be confusing, so
 *    `config.propertyValue` corresponds to `CONFIG_PROPERTY_VALUE`.
 * @param key The key to fetch. See KtDoc for more information.
 * @param default The default value to return if [key] is not present. If null, [IllegalArgumentException] will be
 * thrown if [key] was not found.
 * @throws IllegalArgumentException When [default] is null, and [key] was not found anywhere.
 */
fun Application.getEnvironmentPropertyOrVariable(key: String, default: String? = null): String =
    System.getProperty(key)
        ?: System.getenv(key.asEnvironmentVariable)
        ?: environment.config.propertyOrNull(key)?.getString()
        ?: default
        ?: throw IllegalArgumentException(
            "Could not find a property called \"%s\" in application.yaml, nor an environment variable called \"%s\""
                .format(key, key.asEnvironmentVariable)
        )
