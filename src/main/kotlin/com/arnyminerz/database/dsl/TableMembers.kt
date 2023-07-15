package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.LongIdTable

object TableMembers : LongIdTable() {
    val user = reference("user", Users).uniqueIndex()
    val table = reference("table", EventTables).uniqueIndex()
}
