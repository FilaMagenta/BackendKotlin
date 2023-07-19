package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.LongIdTable

object EventTables : LongIdTable() {
    val responsible = reference("responsible", Users).uniqueIndex()
    val event = reference("event", Events).uniqueIndex()
}
