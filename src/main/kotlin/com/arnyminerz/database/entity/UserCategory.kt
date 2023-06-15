package com.arnyminerz.database.entity

import com.arnyminerz.database.dsl.UserCategories
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.id.EntityID

class UserCategory(id: EntityID<Int>) : IntEntity(id) {
    companion object : EntityClass<Int, UserCategory>(UserCategories)

    var timestamp by UserCategories.timestamp
    var category by UserCategories.category

    var user by User referencedOn UserCategories.user
}
