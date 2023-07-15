package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.LongIdTable

object UserAssistances : LongIdTable() {
    val user = reference("user", Users).uniqueIndex()
    val event = reference("event", Events).uniqueIndex()
}
