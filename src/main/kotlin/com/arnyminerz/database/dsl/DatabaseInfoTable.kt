package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object DatabaseInfoTable : LongIdTable() {
    private const val VALUE_LENGTH = 256

    val value: Column<String> = varchar("value", VALUE_LENGTH)
}
