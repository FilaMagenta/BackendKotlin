package com.arnyminerz.database.dsl

import com.arnyminerz.database.dsl.DSLConst.DATE_LENGTH
import com.arnyminerz.database.dsl.DSLConst.EMAIL_LENGTH
import com.arnyminerz.database.dsl.DSLConst.NIF_LENGTH
import com.arnyminerz.database.dsl.DSLConst.PERSON_NAME_LENGTH
import com.arnyminerz.database.dsl.DSLConst.PERSON_SURNAME_LENGTH
import com.arnyminerz.filamagenta.commons.data.security.permissions.Role
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

private const val USER_ROLE_LENGTH = 10
private const val USER_PASSWORD_HASH_LENGTH = 4056
private const val USER_PASSWORD_SALT_LENGTH = 2048

object Users : LongIdTable() {
    val nif: Column<String> = varchar("nif", NIF_LENGTH).uniqueIndex()
    val role: Column<String> = varchar("role", USER_ROLE_LENGTH).default(Role.DEFAULT.name)
    val name: Column<String> = varchar("name", PERSON_NAME_LENGTH)
    val surname: Column<String> = varchar("surname", PERSON_SURNAME_LENGTH)
    val passwordHash: Column<String> = varchar("password_hash", USER_PASSWORD_HASH_LENGTH)
    val passwordSalt: Column<String> = varchar("password_salt", USER_PASSWORD_SALT_LENGTH)
    val email: Column<String?> = varchar("email", EMAIL_LENGTH).nullable()
    val birthday: Column<String?> = varchar("birthday", DATE_LENGTH).nullable()
}
