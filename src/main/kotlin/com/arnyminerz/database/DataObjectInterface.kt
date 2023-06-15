package com.arnyminerz.database

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.sql.SizedIterable

abstract class DataObjectInterface<Id : Comparable<Id>, E : Entity<Id>, EClass : EntityClass<Id, E>>(
    private val database: ServerDatabase,
    private val entityClass: EClass
) {
    suspend fun <Result> getAll(block: (users: SizedIterable<E>) -> Result) = database.transaction {
        entityClass.all().let(block)
    }
}
