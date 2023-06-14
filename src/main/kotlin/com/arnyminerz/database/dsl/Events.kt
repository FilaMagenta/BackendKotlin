package com.arnyminerz.database.dsl

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.Event
import com.arnyminerz.utils.serialization.JsonSerializer
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.json.JSONObject

object Events: IntIdTable(), JsonSerializer<Event?> {
    val name: Column<String> = varchar("name", 128)
    val description: Column<String> = varchar("description", 4056)
    val date: Column<String> = varchar("date", 64)
    val until: Column<String?> = varchar("until", 64).nullable()
    val reservations: Column<String?> = varchar("reservations", 64).nullable()
    val maxGuests: Column<Int?> = integer("max_guests").nullable().default(-1)

    override suspend fun fromJson(json: JSONObject): Event? =
        ServerDatabase.instance.eventsInterface.get(json.getInt("id")) { it }
}
