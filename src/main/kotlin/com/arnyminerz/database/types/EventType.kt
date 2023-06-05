package com.arnyminerz.database.types

import java.time.ZonedDateTime
import org.json.JSONObject

data class EventType(
    val name: String,
    val description: String,
    val date: ZonedDateTime,
    val until: ZonedDateTime?,
    val reservations: ZonedDateTime?
): DataType {
    constructor(name: String, description: String, date: String, until: String?, reservations: String?): this(
        name,
        description,
        ZonedDateTime.parse(date),
        until?.let { ZonedDateTime.parse(it) },
        reservations?.let { ZonedDateTime.parse(it) }
    )

    override fun toString(): String = toJSON().toString()

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("name", name)
        put("description", description)
        put("date", date.toString())
        put("until", until?.toString())
        put("reservations", reservations?.toString())
    }
}
