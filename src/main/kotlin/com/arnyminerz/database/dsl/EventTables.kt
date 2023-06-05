package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable

object EventTables: IntIdTable() {
    val responsible = reference("responsible", Users).uniqueIndex()
    val event = reference("event", Events).uniqueIndex()
}
