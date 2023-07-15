package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.TableGuests
import com.arnyminerz.filamagenta.commons.data.types.TableGuestType
import com.arnyminerz.filamagenta.commons.utils.jsonOf
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.json.JSONObject

class TableGuest(id: EntityID<Long>) : DataEntity<TableGuestType>(id) {
    companion object : LongEntityClass<TableGuest>(TableGuests)

    var name by TableGuests.name
    var surname by TableGuests.surname
    var nif by TableGuests.nif

    var responsible by User referencedOn TableGuests.responsible
    var table by EventTable referencedOn TableGuests.table

    /**
     * Fills the instance with the data provided in [type]. **Requires to be in a transaction.**
     */
    override fun fill(type: TableGuestType) {
        name = type.name
        surname = type.surname
        nif = type.nif
        responsible = User[type.responsibleId]
        table = EventTable[type.tableId]
    }

    override fun toJSON(): JSONObject = jsonOf(
        "name" to name,
        "surname" to surname,
        "nif" to nif,
        "responsible" to responsible.id,
        "table" to table.id
    )
}
