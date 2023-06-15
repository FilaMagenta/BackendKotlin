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
    suspend fun <Result> getAll(block: (list: SizedIterable<E>) -> Result) = database.transaction {
        entityClass.all().let(block)
    }

    suspend fun <Result> get(id: Int, block: (list: E?) -> Result) = database.transaction {
        entityClass.findById(id).let(block)
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
