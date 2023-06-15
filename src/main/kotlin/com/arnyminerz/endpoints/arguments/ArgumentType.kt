package com.arnyminerz.endpoints.arguments

import org.json.JSONObject

sealed class ArgumentType<R : Any> {
    abstract fun fromString(value: String): R

    abstract fun toString(value: R): String

    abstract fun fromJson(json: JSONObject, key: String): R?
}
