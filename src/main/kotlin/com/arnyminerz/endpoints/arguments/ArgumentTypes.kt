package com.arnyminerz.endpoints.arguments

import com.arnyminerz.filamagenta.commons.utils.getBooleanOrNull
import com.arnyminerz.filamagenta.commons.utils.getFloatOrNull
import com.arnyminerz.filamagenta.commons.utils.getIntOrNull
import com.arnyminerz.filamagenta.commons.utils.getStringOrNull
import java.time.ZonedDateTime
import org.json.JSONObject

object ArgumentTypes {
    object STRING : ArgumentType<String>() {
        override fun fromString(value: String): String = value

        override fun toString(value: String): String = value

        override fun fromJson(json: JSONObject, key: String): String? = json.getStringOrNull(key)
    }

    object INTEGER : ArgumentType<Int>() {
        override fun fromString(value: String): Int = value.toInt()

        override fun toString(value: Int): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): Int? = json.getIntOrNull(key)
    }

    object SHORT : ArgumentType<Short>() {
        override fun fromString(value: String): Short = value.toShort()

        override fun toString(value: Short): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): Short? = json.getIntOrNull(key)?.toShort()
    }

    object FLOAT : ArgumentType<Float>() {
        override fun fromString(value: String): Float = value.toFloat()

        override fun toString(value: Float): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): Float? = json.getFloatOrNull(key)
    }

    object DATETIME : ArgumentType<ZonedDateTime>() {
        override fun fromString(value: String): ZonedDateTime = ZonedDateTime.parse(value)

        override fun toString(value: ZonedDateTime): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): ZonedDateTime? =
            json.getStringOrNull(key)?.let { ZonedDateTime.parse(it) }
    }

    object BOOLEAN : ArgumentType<Boolean>() {
        override fun fromString(value: String): Boolean = value.toBoolean()

        override fun toString(value: Boolean): String = value.toString()

        override fun fromJson(json: JSONObject, key: String): Boolean? = json.getBooleanOrNull(key)
    }

    class ENUM<T : Enum<T>>(private val values: () -> Array<T>) : ArgumentType<T>() {
        override fun fromString(value: String): T = values().find { it.name == value }!!

        override fun toString(value: T): String = value.name

        override fun fromJson(json: JSONObject, key: String): T? = json.getStringOrNull(key)?.let { fromString(it) }
    }
}
