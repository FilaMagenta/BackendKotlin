package com.arnyminerz.database.types

import com.arnyminerz.security.RSAKeyPairGenerator
import com.arnyminerz.utils.toJSON
import java.security.KeyPair
import java.time.ZonedDateTime
import org.json.JSONObject

data class EventType(
    val name: String,
    val description: String,
    val date: ZonedDateTime,
    val until: ZonedDateTime?,
    val reservations: ZonedDateTime?,
    val maxGuests: Int? = MAX_GUESTS_DEFAULT,
    val keyPair: KeyPair = RSAKeyPairGenerator.newKey()
) : DataType {
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
        put(
            "key_pair",
            JSONObject().apply {
                put("public", keyPair.public.toJSON())
                put("private", keyPair.private.toJSON())
            }
        )
    }
}
