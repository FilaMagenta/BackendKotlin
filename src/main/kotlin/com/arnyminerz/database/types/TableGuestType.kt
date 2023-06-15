package com.arnyminerz.database.types

import com.arnyminerz.database.entity.EventTable
import com.arnyminerz.database.entity.User
import org.json.JSONObject

data class TableGuestType(
    val name: String,
    val surname: String,
    val nif: String,
    val responsible: User,
    val table: EventTable
) : DataType {
    override fun toJSON(): JSONObject = JSONObject().apply {
        put("name", name)
        put("surname", surname)
        put("nif", nif)
        put("responsible", responsible.id)
        put("table", table.id)
    }
}
