package com.arnyminerz.database.types

import java.time.ZonedDateTime

data class EventType(
    val name: String,
    val description: String,
    val date: ZonedDateTime,
    val until: ZonedDateTime?,
    val reservations: ZonedDateTime?
): DataType
