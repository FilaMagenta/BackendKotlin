package com.arnyminerz.database.types

import java.time.ZonedDateTime

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
}
