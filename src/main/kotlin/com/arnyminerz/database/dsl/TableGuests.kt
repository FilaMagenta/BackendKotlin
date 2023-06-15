package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object TableGuests: IntIdTable() {
    val name: Column<String> = varchar("name", 128)
    val surname: Column<String> = varchar("surname", 256)
    val nif: Column<String> = varchar("nif", 16)

    val responsible = reference("responsible", Users)
    val table = reference("table", EventTables)
}
