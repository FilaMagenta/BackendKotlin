package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.Events
import com.arnyminerz.database.dsl.UserAssistances
import com.arnyminerz.database.types.EventType
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class Event(id: EntityID<Int>) : DataEntity<EventType>(id) {
    companion object : IntEntityClass<Event>(Events)

    var name by Events.name
    var description by Events.description
    var date by Events.date
    var until by Events.until
    var reservations by Events.reservations
    var maxGuests by Events.maxGuests

    val assistants by UserAssistance referrersOn UserAssistances.event

    val tables by EventTable referrersOn EventTables.event

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

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("id", id.value)
        put("name", name)
        put("description", description)
        put("date", date)
        put("until", until)
        put("reservations", reservations)
        put("max_guests", maxGuests)
    }

    override fun fill(type: EventType) {
        name = type.name
        description = type.description
        date = type.date.toString()
        until = type.until?.toString()
        reservations = type.reservations?.toString()
        maxGuests = type.maxGuests
    }
}
