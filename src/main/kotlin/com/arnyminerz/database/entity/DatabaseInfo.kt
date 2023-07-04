package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.DatabaseInfoTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DatabaseInfo(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DatabaseInfo>(DatabaseInfoTable) {
        const val VERSION_ID = 1
    }

    var value by DatabaseInfoTable.value
}
