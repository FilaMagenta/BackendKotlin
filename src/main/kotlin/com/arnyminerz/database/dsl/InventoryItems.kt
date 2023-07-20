package com.arnyminerz.database.dsl

import java.time.Instant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.timestamp

private const val INVENTORY_ITEM_NAME_LENGTH = 64

object InventoryItems : LongIdTable() {
    val timestamp = timestamp("timestamp").default(Instant.now())
    val name = varchar("name", INVENTORY_ITEM_NAME_LENGTH)
    var unitPrice = double("unit_price")
}
