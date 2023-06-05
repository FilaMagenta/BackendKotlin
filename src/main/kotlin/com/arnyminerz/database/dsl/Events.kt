package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Events: IntIdTable() {
    val name: Column<String> = varchar("name", 128)
    val description: Column<String> = varchar("description", 4056)
    val date: Column<String> = varchar("date", 64)
    val until: Column<String?> = varchar("until", 64).nullable()
    val reservations: Column<String?> = varchar("reservations", 64).nullable()
}
