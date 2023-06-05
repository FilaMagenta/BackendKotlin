package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Users: IntIdTable() {
    val nif: Column<String> = varchar("nif", 16).uniqueIndex()
    val name: Column<String> = varchar("name", 128)
    val surname: Column<String> = varchar("surname", 256)
    val passwordHash: Column<String> = varchar("password_hash", 4056)
    val passwordSalt: Column<String> = varchar("password_salt", 2048)
    val email: Column<String?> = varchar("email", 256).nullable()
    val birthday: Column<Long?> = long("birthday").nullable()
}
