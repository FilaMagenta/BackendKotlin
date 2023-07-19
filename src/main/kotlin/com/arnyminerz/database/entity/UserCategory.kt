package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.UserCategories
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UserCategory(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<UserCategory>(UserCategories)

    var timestamp by UserCategories.timestamp
    var category by UserCategories.category

    var user by User referencedOn UserCategories.user
}
