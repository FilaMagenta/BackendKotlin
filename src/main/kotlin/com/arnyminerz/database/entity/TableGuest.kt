package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.TableGuests
import com.arnyminerz.database.types.TableGuestType
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class TableGuest(id: EntityID<Int>) : DataEntity<TableGuestType>(id) {
    companion object : IntEntityClass<TableGuest>(TableGuests)

    var name by TableGuests.name
    var surname by TableGuests.surname
    var nif by TableGuests.nif

    var responsible by User referencedOn TableGuests.responsible
    var table by EventTable referencedOn TableGuests.table

    override fun fill(type: TableGuestType) {
        name = type.name
        surname = type.surname
        nif = type.nif
        responsible = type.responsible
        table = type.table
    }

    override fun toJSON(): JSONObject = JSONObject().apply {
        put("name", name)
        put("surname", surname)
        put("nif", nif)
        put("responsible", responsible.id)
        put("table", table.id)
    }
}
