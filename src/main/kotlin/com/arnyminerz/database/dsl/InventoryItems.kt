package com.arnyminerz.database.dsl

import java.time.ZonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable

private const val INVENTORY_ITEM_NAME_LENGTH = 64

object InventoryItems : LongIdTable() {
    val timestamp = varchar("timestamp", DSLConst.DATE_LENGTH).default(ZonedDateTime.now().toString())
    val name = varchar("name", INVENTORY_ITEM_NAME_LENGTH)
    var unitPrice = double("unit_price")
}
