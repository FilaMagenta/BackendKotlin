package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.TableMembers
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TableMember(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<TableMember>(TableMembers)

    var user by User referencedOn TableMembers.user
    var table by EventTable referencedOn TableMembers.table
}
