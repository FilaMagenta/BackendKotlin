package com.arnyminerz.database.`interface`

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.DataEntity
import com.arnyminerz.database.types.DataType
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.SizedIterable

abstract class DataObjectInterface <T: DataType, E: DataEntity<T>, EClass: IntEntityClass<E>> (
    protected val database: ServerDatabase,
    private val entityClass: EClass
) {
    suspend fun <Result> getAll(block: (users: SizedIterable<E>) -> Result) = database.transaction {
        entityClass.all().let(block)
    }

    protected abstract fun E.processExtras(extras: Map<String, String>)

    suspend fun new(type: T, vararg extras: Pair<String, String>) = database.transaction {
        entityClass.new {
            fill(type)
            processExtras(extras.toMap())
        }
    }
}