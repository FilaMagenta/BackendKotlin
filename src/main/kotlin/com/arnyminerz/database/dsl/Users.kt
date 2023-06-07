package com.arnyminerz.database.dsl

import com.arnyminerz.database.types.UserType
import com.arnyminerz.security.permissions.Role
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

object Users: IntIdTable() {
    val nif: Column<String> = varchar("nif", 16).uniqueIndex()
    val role: Column<String> = varchar("role", 10).default(Role.DEFAULT.name)
    val name: Column<String> = varchar("name", 128)
    val surname: Column<String> = varchar("surname", 256)
    val passwordHash: Column<String> = varchar("password_hash", 4056)
    val passwordSalt: Column<String> = varchar("password_salt", 2048)
    val email: Column<String?> = varchar("email", 256).nullable()
    val birthday: Column<String?> = varchar("birthday", 12).nullable()
}
