package com.arnyminerz.database.types

import org.json.JSONObject
import java.time.ZonedDateTime

data class EventType(
    val name: String,
    val description: String,
    val date: ZonedDateTime,
    val until: ZonedDateTime?,
    val reservations: ZonedDateTime?,
    val maxGuests: Int? = MAX_GUESTS_DEFAULT
): DataType {
    companion object {
        /**
         * The default value for the maximum allowed amount of guests
         */
        val MAX_GUESTS_DEFAULT: Int? = null
    }

    override fun toString(): String = toJSON().toString()

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("name", name)
        put("description", description)
        put("date", date.toString())
        put("until", until?.toString())
        put("reservations", reservations?.toString())
        put("max_guests", maxGuests)
    }
}
