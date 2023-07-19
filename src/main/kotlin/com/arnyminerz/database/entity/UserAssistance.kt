package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.UserAssistances
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserAssistance(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserAssistance>(UserAssistances)

    var user by User referencedOn UserAssistances.user
    var event by Event referencedOn UserAssistances.event
}
