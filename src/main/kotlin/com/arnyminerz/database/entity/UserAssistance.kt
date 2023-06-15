package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.UserAssistances
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserAssistance(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserAssistance>(UserAssistances)

    var user by User referencedOn UserAssistances.user
    var event by Event referencedOn UserAssistances.event
}
