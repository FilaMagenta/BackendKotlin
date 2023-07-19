package com.arnyminerz.database.dsl

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.Event
import com.arnyminerz.filamagenta.commons.utils.serialization.JsonSerializer
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.json.JSONObject

private const val EVENT_NAME_LENGTH = 128
private const val EVENT_DESCRIPTION_LENGTH = 1024 * 4
private const val EVENT_KEY_LENGTH = 1024 * 16

object Events : LongIdTable(), JsonSerializer<Event?> {
    val timestamp: Column<String> = varchar("timestamp", DSLConst.DATE_LENGTH).default(ZonedDateTime.now().toString())
    val name: Column<String> = varchar("name", EVENT_NAME_LENGTH)
    val description: Column<String> = varchar("description", EVENT_DESCRIPTION_LENGTH)
    val date: Column<String> = varchar("date", DSLConst.DATE_LENGTH)
    val until: Column<String?> = varchar("until", DSLConst.DATE_LENGTH).nullable()
    val reservations: Column<String?> = varchar("reservations", DSLConst.DATE_LENGTH).nullable()
    val maxGuests: Column<Long?> = long("max_guests").nullable().default(-1)
    val publicKey: Column<String> = varchar("public_key", EVENT_KEY_LENGTH)
    val privateKey: Column<String> = varchar("private_key", EVENT_KEY_LENGTH)

    override suspend fun fromJson(json: JSONObject): Event? =
        ServerDatabase.instance.eventsInterface.get(json.getLong("id")) { it }
}
