package com.arnyminerz.endpoints.arguments

import com.arnyminerz.utils.getFloatOrNull
import com.arnyminerz.utils.getIntOrNull
import com.arnyminerz.utils.getStringOrNull
import java.time.ZonedDateTime
import org.json.JSONObject

object ArgumentTypes {
    object STRING: ArgumentType<String>() {
        override fun fromString(value: String): String = value

        override fun toString(value: String): String = value

        override fun fromJson(json: JSONObject, key: String): String? = json.getStringOrNull(key)
    }

    object INTEGER: ArgumentType<Int>() {
        override fun fromString(value: String): Int = value.toInt()

        override fun toString(value: Int): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): Int? = json.getIntOrNull(key)
    }

    object FLOAT: ArgumentType<Float>() {
        override fun fromString(value: String): Float = value.toFloat()

        override fun toString(value: Float): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): Float? = json.getFloatOrNull(key)
    }

    object DATETIME: ArgumentType<ZonedDateTime>() {
        override fun fromString(value: String): ZonedDateTime = ZonedDateTime.parse(value)

        override fun toString(value: ZonedDateTime): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): ZonedDateTime? =
            json.getStringOrNull(key)?.let { ZonedDateTime.parse(it) }
    }
}