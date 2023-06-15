package com.arnyminerz.database.dsl

import com.arnyminerz.data.Category
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column

/**
 * Provides different categories that a user might have during a period of time.
 * @see [Category]
 */
object UserCategories : IntIdTable() {
    val timestamp: Column<Long> = long("timestamp")
    val category: Column<String> = varchar("category", Category.NAME_MAX_LENGTH)

    val user = reference("user", Users)
}
