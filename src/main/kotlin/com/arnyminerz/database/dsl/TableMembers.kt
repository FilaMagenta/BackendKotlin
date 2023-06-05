package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable

object TableMembers: IntIdTable() {
    val user = reference("user", Users).uniqueIndex()
    val table = reference("table", EventTables).uniqueIndex()
}
