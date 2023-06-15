package com.arnyminerz.database.dsl

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.Event
import com.arnyminerz.utils.serialization.JsonSerializer
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.json.JSONObject

private const val EVENT_NAME_LENGTH = 128
private const val EVENT_DESCRIPTION_LENGTH = 1024 * 4

object Events : IntIdTable(), JsonSerializer<Event?> {
    val name: Column<String> = varchar("name", EVENT_NAME_LENGTH)
    val description: Column<String> = varchar("description", EVENT_DESCRIPTION_LENGTH)
    val date: Column<String> = varchar("date", DSLConst.DATE_LENGTH)
    val until: Column<String?> = varchar("until", DSLConst.DATE_LENGTH).nullable()
    val reservations: Column<String?> = varchar("reservations", DSLConst.DATE_LENGTH).nullable()
    val maxGuests: Column<Int?> = integer("max_guests").nullable().default(-1)

    override suspend fun fromJson(json: JSONObject): Event? =
        ServerDatabase.instance.eventsInterface.get(json.getInt("id")) { it }
}
