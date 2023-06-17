package com.arnyminerz.database.connector

import com.arnyminerz.database.ServerDatabase
import com.arnyminerz.database.entity.DataEntity
import com.arnyminerz.database.types.DataType
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.sql.SizedIterable

abstract class DataObjectInterface<T : DataType, E : DataEntity<T>, EClass : IntEntityClass<E>>(
    protected val database: ServerDatabase,
    private val entityClass: EClass
) {
    suspend fun <Result> getAll(block: suspend (list: SizedIterable<E>) -> Result) = database.transaction {
        block(entityClass.all())
    }

    suspend fun <Result> get(id: Int, block: suspend (list: E?) -> Result) = database.transaction {
        block(entityClass.findById(id))
    }

    protected open fun E.processExtras(extras: Map<String, String>) {
        return
    }

    suspend fun new(type: T, vararg extras: Pair<String, String>) = database.transaction {
        entityClass.new {
            fill(type)
            processExtras(extras.toMap())
        }
    }
}
