package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.DatabaseInfoTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DatabaseInfo(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<DatabaseInfo>(DatabaseInfoTable) {
        const val VERSION_ID = 1
    }

    var value by DatabaseInfoTable.value
}
