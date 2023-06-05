package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.Events
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class Event(id: EntityID<Int>): IntEntity(id) {
    companion object: IntEntityClass<Event>(Events)

    val name by Events.name
    val description by Events.description
    val date by Events.date
    val until by Events.until
    val reservations by Events.reservations

    /**
     * Gets the event start date.
     */
    fun getStart(): ZonedDateTime = ZonedDateTime.parse(date)

    /**
     * Gets the event end date (might be null).
     */
    fun getEnd(): ZonedDateTime? = until?.let { ZonedDateTime.parse(it) }

    /**
     * Gets the last moment when reservations can be made.
     */
    fun getReservationsEndDate(): ZonedDateTime? = reservations?.let { ZonedDateTime.parse(it) }
}