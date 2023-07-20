package com.arnyminerz.database.dsl

import com.arnyminerz.filamagenta.commons.data.Category
import java.time.Instant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp

object EventPricesTable : LongIdTable() {
    val category: Column<Category> = enumeration<Category>("category").default(Category.UNKNOWN)

    val timestamp: Column<Instant> = timestamp("timestamp").default(Instant.now())
    val price: Column<Double> = double("price")

    val event = reference("event", Events)

    // Declare primary key with id and category so that there are no multiple categories allowed
    init {
        uniqueIndex("indexname", id, event)
    }
}
