package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object DatabaseInfoTable : IntIdTable() {
    private const val VALUE_LENGTH = 256

    val value: Column<String> = varchar("value", VALUE_LENGTH)
}
