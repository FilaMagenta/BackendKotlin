package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.TableMembers
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class TableMember(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TableMember>(TableMembers)

    var user by User referencedOn TableMembers.user
    var table by EventTable referencedOn TableMembers.table
}
