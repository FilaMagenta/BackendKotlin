package com.arnyminerz.database.dsl

import com.arnyminerz.filamagenta.commons.data.Category
import java.time.Instant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.javatime.timestamp

/**
 * Provides different categories that a user might have during a period of time.
 * @see [Category]
 */
object UserCategories : LongIdTable() {
    val timestamp: Column<Instant> = timestamp("timestamp").default(Instant.now())
    val category: Column<Category> = enumeration<Category>("category")

    val user = reference("user", Users)
}
