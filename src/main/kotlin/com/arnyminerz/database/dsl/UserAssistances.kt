package com.arnyminerz.database.dsl

import org.jetbrains.exposed.dao.id.IntIdTable

object UserAssistances: IntIdTable() {
    val user = reference("user", Users)
    val event = reference("event", Events)
}