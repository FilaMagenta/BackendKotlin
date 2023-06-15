package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.EventTables
import com.arnyminerz.database.dsl.TableGuests
import com.arnyminerz.database.dsl.TableMembers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class EventTable(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EventTable>(EventTables)

    var responsible by User referencedOn EventTables.responsible
    var event by Event referencedOn EventTables.event

    val members by TableMember referrersOn TableMembers.table
    val guests by TableGuest referrersOn TableGuests.table
}
