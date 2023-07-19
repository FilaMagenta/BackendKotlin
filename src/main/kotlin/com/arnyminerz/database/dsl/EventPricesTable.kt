package com.arnyminerz.database.dsl

import com.arnyminerz.filamagenta.commons.data.Category
import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column

object EventPricesTable : LongIdTable() {
    val category: Column<String> = varchar("category", Category.NAME_MAX_LENGTH)
        .default(Category.UNKNOWN.name)

    val timestamp: Column<String> = varchar("timestamp", DSLConst.DATE_LENGTH)
        .default(ZonedDateTime.now().toString())
    val price: Column<Double> = double("price")

    val event = reference("event", Events)

    // Declare primary key with id and category so that there are no multiple categories allowed
    init {
        uniqueIndex("indexname", id, event)
    }
}
